import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataImport {
    public static void loadData(String fileName) throws FileNotFoundException {
        Scanner rowScanner = new Scanner(new File(fileName));

        // Salto la prima riga di intestazione
        rowScanner.nextLine();

        while(rowScanner.hasNextLine()) {
            String[] fields = rowScanner.nextLine().split(";");

            float lon = Float.parseFloat(fields[5].substring(0, 2) + "," + fields[5].substring(3));
            float lat = Float.parseFloat(fields[6].substring(0, 2) + "," + fields[6].substring(3));

            Main.fermate.add(new Fermata(
                    fields[0],
                    fields[1],
                    fields[2],
                    fields[3],
                    Integer.parseInt(fields[4]),
                    lon,
                    lat
            ));

            aggiungiComune(fields[0]);
            aggiungiProvincia(fields[1]);
            aggiungiRegione(fields[2]);
        }
    }

    private static void aggiungiComune(String nome) {
        if(Main.comuni.contains(nome)) return;
        Main.comuni.add(nome);
    }

    private static void aggiungiProvincia(String nome) {
        if(Main.province.contains(nome)) return;
        Main.province.add(nome);
    }

    private static void aggiungiRegione(String nome) {
        if(Main.regioni.contains(nome)) return;
        Main.regioni.add(nome);
    }
}
