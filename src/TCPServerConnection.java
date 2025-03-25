import java.net.Socket;

public class TCPServerConnection extends Thread {
    private Socket socket;

    public TCPServerConnection(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

    }
}
