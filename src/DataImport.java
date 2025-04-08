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
                    getComune(fields[0]),
                    getProvincia(fields[1]),
                    getRegione(fields[2]),
                    fields[3],
                    Integer.parseInt(fields[4]),
                    lon,
                    lat
            ));
        }
    }

    private static Comune getComune(String nome) {
        if(Main.comuni.containsKey(nome)) return Main.comuni.get(nome);
        Comune comune = new Comune(nome);
        Main.comuni.put(nome, comune);

        return comune;
    }

    private static Provincia getProvincia(String nome) {
        if(Main.province.containsKey(nome)) return Main.province.get(nome);
        Provincia provincia = new Provincia(nome);
        Main.province.put(nome, provincia);

        return provincia;
    }

    private static Regione getRegione(String nome) {
        if(Main.regioni.containsKey(nome)) return Main.regioni.get(nome);
        Regione regione = new Regione(nome);
        Main.regioni.put(nome, regione);

        return regione;
    }
}
