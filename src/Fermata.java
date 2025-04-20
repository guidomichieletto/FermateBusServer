import org.json.JSONObject;

public class Fermata {
    public Comune comune;
    public Provincia provincia;
    public Regione regione;
    public String nome;
    public long idOSM;
    public float lon;
    public float lat;

    public Fermata(Comune comune, Provincia provincia, Regione regione, String nome, long idOSM, float lon, float lat) {
        this.comune = comune;
        this.provincia = provincia;
        this.regione = regione;
        this.nome = nome;
        this.idOSM = idOSM;
        this.lon = lon;
        this.lat = lat;
    }

    public JSONObject jsonObject() {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("nome", nome);
        jsonObject.put("comune", comune.nome);
        jsonObject.put("provincia", provincia.nome);
        jsonObject.put("regione", regione.nome);
        jsonObject.put("lon", lon);
        jsonObject.put("lat", lat);

        return jsonObject;
    }
}
