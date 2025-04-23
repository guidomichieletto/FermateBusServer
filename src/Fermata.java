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

    public double calculateDistance(double lat, double lon) {
        double lat1Rad = Math.toRadians(this.lat);
        double lon1Rad = Math.toRadians(this.lon);
        double lat2Rad = Math.toRadians(lat);
        double lon2Rad = Math.toRadians(lon);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return 6371 * c;
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
