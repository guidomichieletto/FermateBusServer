public class Fermata {
    public Comune comune;
    public Provincia provincia;
    public Regione regione;
    public String nome;
    public int idOSM;
    public float lon;
    public float lat;

    public Fermata(Comune comune, Provincia provincia, Regione regione, String nome, int idOSM, float lon, float lat) {
        this.comune = comune;
        this.provincia = provincia;
        this.regione = regione;
        this.nome = nome;
        this.idOSM = idOSM;
        this.lon = lon;
        this.lat = lat;
    }
}
