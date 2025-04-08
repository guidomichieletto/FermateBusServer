import java.util.HashMap;

public class Provincia {
    public String nome;
    private HashMap<String, Comune> comuni = new HashMap<>();

    public Provincia(String nome) {
        this.nome = nome;
    }

    public HashMap<String, Comune> getComuni() {
        return comuni;
    }
}
