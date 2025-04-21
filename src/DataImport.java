import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DataImport {
    public static void loadData(String fileName) throws FileNotFoundException {
        Scanner rowScanner = new Scanner(new FileInputStream(fileName));

        // Salto la prima riga di intestazione
        rowScanner.nextLine();

        while(rowScanner.hasNextLine()) {
            String[] fields = rowScanner.nextLine().split(";");

            if(fields.length != 7) continue;
            float lon = Float.parseFloat(fields[5]);
            float lat = Float.parseFloat(fields[6]);

            Comune comune = getComune(fields[0]);
            Provincia provincia = getProvincia(fields[1]);
            Regione regione = getRegione(fields[2]);

            Fermata fermata = new Fermata(
                    comune,
                    provincia,
                    regione,
                    fields[3],
                    Long.parseLong(fields[4]),
                    lon,
                    lat
            );

            Main.fermate.add(fermata);
            comune.getFermate().add(fermata);
            if(!regione.getProvince().containsKey(provincia.nome)) regione.getProvince().put(provincia.nome, provincia);
            if(!provincia.getComuni().containsKey(comune.nome)) provincia.getComuni().put(comune.nome, comune);
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
