import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class TCPServer extends Thread {
    private final int port;
    private final ArrayList<TCPServerConnection> connections = new ArrayList<>();
    private final boolean wsSupport;

    public TCPServer(int port) {
        this.port = port;
        this.wsSupport = false;
    }

    public TCPServer(int port, boolean wsSupport) {
        this.port = port;
        this.wsSupport = wsSupport;
    }

    @Override
    public void run() {
        try(ServerSocket ss = new ServerSocket(this.port)) {
            System.out.println("Server started at port " + this.port + (wsSupport ? " with WebSocket support" : ""));

            while(true) {
                Socket socket = ss.accept();
                System.out.println("Accepted new connection: " + socket);

                TCPServerConnection connection = new TCPServerConnection(socket, wsSupport);
                connection.start();
                connections.add(connection);
            }
        } catch (IOException ex) {

        }
    }
}
