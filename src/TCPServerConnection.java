import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TCPServerConnection extends Thread {
    private final Socket socket;
    private final boolean ws;
    private BufferedReader in;
    private PrintWriter out;

    public TCPServerConnection(Socket socket, boolean ws) {
        this.socket = socket;
        this.ws = ws;
    }

    @Override
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);

            if(ws) wsHandshake();

            while(true) {
                String command = readMessage();
                System.out.println(command);
                if(command == null || command.equals("bye")) break;

                RequestWorker rw = new RequestWorker(command, this);
                rw.start();
            }

            System.out.println("Closed connection: " + socket);
            socket.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private String readMessage() throws IOException {
        if(ws) return wsReadMessage(); else return in.readLine();
    }

    public void sendMessage(String message) {
        try {
            if(ws) wsSendMessage(message); else out.println(message);
            out.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private void wsHandshake() throws IOException, NoSuchAlgorithmException {
        Scanner s = new Scanner(socket.getInputStream());
        String line = s.useDelimiter("\\r\\n\\r\\n").next();
        Matcher get = Pattern.compile("^GET").matcher(line);

        if(get.find()) {
            Matcher match = Pattern.compile("Sec-WebSocket-Key: (.*)").matcher(line);
            if(match.find()) {;
                byte[] response = ("HTTP/1.1 101 Switching Protocols\r\n"
                        + "Connection: Upgrade\r\n"
                        + "Upgrade: websocket\r\n"
                        + "Sec-WebSocket-Accept: "
                        + Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-1").digest((match.group(1) + "258EAFA5-E914-47DA-95CA-C5AB0DC85B11").getBytes("UTF-8")))
                        + "\r\n\r\n").getBytes("UTF-8");

                socket.getOutputStream().write(response, 0, response.length);
                return;
            }
        }

        throw new IOException("WebSocket handshake failed");
    }

    private void wsSendMessage(String message) throws IOException {
        OutputStream out = socket.getOutputStream();

        byte[] payloadBytes = message.getBytes(StandardCharsets.UTF_8);
        long payloadLength = payloadBytes.length;
        byte opcode = (byte) 0x81; // Text frame

        out.write(opcode);
        if (payloadLength <= 125) {
            out.write((byte) payloadLength);
        } else if (payloadLength <= 65535) {
            out.write((byte) 126);
            out.write((byte) (payloadLength >> 8));
            out.write((byte) (payloadLength));
        } else {
            out.write((byte) 127);
            out.write((byte) (payloadLength >> 56));
            out.write((byte) (payloadLength >> 48));
            out.write((byte) (payloadLength >> 40));
            out.write((byte) (payloadLength >> 32));
            out.write((byte) (payloadLength >> 24));
            out.write((byte) (payloadLength >> 16));
            out.write((byte) (payloadLength >> 8));
            out.write((byte) (payloadLength));
        }

        out.write(payloadBytes);
        out.flush();
    }

    private String wsReadMessage() throws IOException {
        InputStream inputStream = socket.getInputStream();

        int firstByte = inputStream.read();
        if (firstByte == -1) {
            return null; // Connessione chiusa
        }

        int secondByte = inputStream.read();
        if (secondByte == -1) {
            return null; // Connessione chiusa
        }
        boolean isMasked = (secondByte & 0x80) != 0;
        int payloadLength = secondByte & 0x7F;

        if (!isMasked) {
            throw new IOException("Frame non mascherato ricevuto dal server. Questo non è previsto.");
        }

        byte[] maskingKey = new byte[4];
        if (inputStream.read(maskingKey) != 4) {
            return null; // Errore nella lettura della masking key
        }

        if (payloadLength <= 125) {
            // Lunghezza del payload già letta
        } else if (payloadLength == 126) {
            payloadLength = (inputStream.read() << 8) | inputStream.read();
        } else if (payloadLength == 127) {
            long extendedLength = 0;
            for (int i = 0; i < 8; i++) {
                extendedLength |= (long) inputStream.read() << (56 - i * 8);
            }
            payloadLength = (int) extendedLength; // Assumiamo che la lunghezza rientri in un int per semplicità
        }

        byte[] payloadData = new byte[payloadLength];
        int bytesRead = 0;
        while (bytesRead < payloadLength) {
            int read = inputStream.read(payloadData, bytesRead, payloadLength - bytesRead);
            if (read == -1) {
                return null; // Connessione chiusa durante la lettura del payload
            }
            bytesRead += read;
        }

        // Demaschera il payload
        byte[] unmaskedPayload = new byte[payloadLength];
        for (int i = 0; i < payloadLength; i++) {
            unmaskedPayload[i] = (byte) (payloadData[i] ^ maskingKey[i % 4]);
        }

        return new String(unmaskedPayload, StandardCharsets.UTF_8);
    }
}
