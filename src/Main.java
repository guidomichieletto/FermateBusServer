import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static final String FILE_NAME = "file.csv";
    public static final int PORT = 3030;
    public static ArrayList<Fermata> fermate = new ArrayList<>();
    public static HashMap<String, Comune> comuni = new HashMap<>();
    public static HashMap<String, Provincia> province = new HashMap<>();
    public static HashMap<String, Regione> regioni = new HashMap<>();
    private static TCPServer server;

    public static void main(String[] args) {
        importData();
        startServer();
    }

    public static void importData() {
        System.out.print("Importo dati... ");
        try {
            DataImport.loadData(FILE_NAME);
            System.out.println(fermate.size() + " records caricati!");
        } catch (FileNotFoundException ex) {
            System.out.println("File non trovato!");
        }
    }

    public static void startServer() {
        server = new TCPServer(PORT);
        server.start();
    }
}