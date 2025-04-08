import java.util.HashMap;

public class Regione {
    public String nome;
    private HashMap<String, Provincia> province = new HashMap<>();

    public Regione(String nome) {
        this.nome = nome;
    }

    public HashMap<String, Provincia> getProvince() {
        return province;
    }
}
