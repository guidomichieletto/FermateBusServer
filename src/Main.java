import java.io.FileNotFoundException;
import java.util.ArrayList;

public class Main {
    public static final String FILE_NAME = "file.csv";
    public static final int PORT = 3030;
    public static ArrayList<Fermata> fermate = new ArrayList<>();
    public static ArrayList<String> comuni = new ArrayList<>();
    public static ArrayList<String> province = new ArrayList<>();
    public static ArrayList<String> regioni = new ArrayList<>();
    private static TCPServer server;

    public static void main(String[] args) {
        importData();
        startServer();
    }

    public static void importData() {
        System.out.print("Importo dati... ");
        try {
            DataImport.loadData(FILE_NAME);
            System.out.println(fermate.size() + " records caricate!");
        } catch (FileNotFoundException ex) {
            System.out.println("File non trovato!");
        }
    }

    public static void startServer() {
        server = new TCPServer(PORT);
        server.start();
    }
}