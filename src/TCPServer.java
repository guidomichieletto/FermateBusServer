import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread {
    private final int port;
    private final ArrayList<TCPServerConnection> connections = new ArrayList<>();

    public TCPServer(int port) {
        this.port = port;
    }

    @Override
    public void run() {
        try(ServerSocket ss = new ServerSocket(this.port)) {
            System.out.println("Server started at port " + this.port);

            while(true) {
                try(Socket socket = ss.accept()) {
                    System.out.println("Accepted new connection: " + socket);

                    TCPServerConnection connection = new TCPServerConnection(socket);
                    connection.start();
                    connections.add(connection);
                }
            }
        } catch (IOException ex) {

        }
    }
}
