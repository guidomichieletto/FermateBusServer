import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;

public class RequestWorker extends Thread {
    private String command;
    private PrintWriter out;
    private JSONObject request;
    private JSONObject response;

    public RequestWorker(String command, PrintWriter out) {
        this.command = command;
        this.out = out;
        response = new JSONObject();
    }

    @Override
    public void run() {
        try {
            request = new JSONObject(command);
            if(!request.has("request")){
                errorResponse("campo request non presente");
                return;
            }
            if(!request.has("id")){
                errorResponse("campo id richiesta non presente");
                return;
            }

            response.put("inResponseTo", request.get("id"));

            switch ((String) request.get("request")) {
                case "ping" -> ping();
                case "query" -> query();
                case "query_stops_coords" -> queryStopsCoords();
                default -> errorResponse("richiesta non valida");
            }

            out.println(response);
        } catch (JSONException ex) {
            errorResponse(ex.getMessage());
        }
    }

    private void ping() {
        response.put("response", "pong");
        out.println(response);
    }

    private void query() {
        if(!request.has("type")) {
            errorResponse("richiesta query non corretta: campo type non presente");
            return;
        }

        switch((String) request.get("type")) {
            case "fermate" -> queryFermate();
            case "comuni" -> queryComuni();
            case "province" -> queryProvince();
            case "regioni" -> queryRegioni();
            default -> errorResponse("oggetto richiesto non valido");
        }
    }

    private void queryFermate() {
        JSONArray list = new JSONArray();

        if(request.has("comune")) {
            System.out.println(Main.comuni.get(request.getString("comune")));
            for(Fermata fermata : Main.comuni.get(request.getString("comune")).getFermate()) {
                list.put(fermata.jsonObject());
            }
        } else {
            for(Fermata fermata : Main.fermate) {
                list.put(fermata.jsonObject());
            }
        }

        response.put("response", "list");
        response.put("list", list);
    }

    private void queryComuni() {
        JSONArray list = new JSONArray();

        if(request.has("provincia")) {
            for(Comune comune : Main.province.get(request.getString("provincia")).getComuni().values()) {
                list.put(comune.nome);
            }
        } else if(request.has("regione")) {
            for(Provincia provincia : Main.regioni.get(request.getString("regione")).getProvince().values()) {
                for(Comune comune : provincia.getComuni().values()) {
                    list.put(comune.nome);
                }
            }
        } else {
            for (Comune comune : Main.comuni.values()) {
                list.put(comune.nome);
            }
        }

        response.put("response", "list");
        response.put("list", list);
    }

    private void queryProvince() {
        JSONArray list = new JSONArray();
        HashMap<String, String> where = parseWhere();

        if(request.has("regione")) {
            for(Provincia provincia : Main.regioni.get(request.getString("regione")).getProvince().values()) {
                list.put(provincia.nome);
            }
        } else {
            for(Provincia provincia : Main.province.values()) {
                list.put(provincia.nome);
            }
        }

        response.put("response", "list");
        response.put("list", list);
    }

    private void queryRegioni() {
        JSONArray list = new JSONArray();

        for(Regione regione : Main.regioni.values()) {
            list.put(regione.nome);
        }

        response.put("response", "list");
        response.put("list", list);
    }

    private void queryStopsCoords() {

    }

    private HashMap<String, String> parseWhere() {
        HashMap<String, String> where = new HashMap<>();

        if(!request.has("where")) return null;

        try {
            JSONArray jarray = request.getJSONArray("where");
            Iterator<Object> arrayIterator = jarray.iterator();

            while(arrayIterator.hasNext()) {
                JSONObject arrayObj = (JSONObject) arrayIterator.next();
                String key = arrayObj.keys().next();
                where.put(key, (String) arrayObj.get(key));
            }
        } catch (Exception ex) {
            errorResponse(ex.getMessage());
        }

        return where;
    }

    private void errorResponse(String description) {
        response.put("response", "error");
        response.put("description", description);
        out.println(response);
    }
}
