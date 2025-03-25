public class Fermata {
    public String comune;
    public String provincia;
    public String regione;
    public String nome;
    public int idOSM;
    public float lon;
    public float lat;

    public Fermata(String comune, String provincia, String regione, String nome, int idOSM, float lon, float lat) {
        this.comune = comune;
        this.provincia = provincia;
        this.regione = regione;
        this.nome = nome;
        this.idOSM = idOSM;
        this.lon = lon;
        this.lat = lat;
    }
}
