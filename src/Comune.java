import java.util.ArrayList;

public class Comune {
    public String nome;
    private ArrayList<Fermata> fermate = new ArrayList<>();

    public Comune(String nome) {
        this.nome = nome;
    }

    public ArrayList<Fermata> getFermate() {
        return fermate;
    }
}
