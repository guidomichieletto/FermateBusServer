import java.io.*;
import java.net.Socket;

public class TCPServerConnection extends Thread {
    private Socket socket;

    public TCPServerConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8")), true);

            while(true) {
                String command = in.readLine();
                System.out.println(command);
                if(command.equals("bye")) break;

                RequestWorker rw = new RequestWorker(command, out);
                rw.start();
            }

            System.out.println("Closed connection: " + socket);
            socket.close();
        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

    }
}
