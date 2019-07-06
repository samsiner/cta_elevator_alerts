//TODO: Add JavaDoc comments to all classes

package com.example.elevator_app.Models.Stations;

import com.example.elevator_app.HttpsRequest.HTTPSRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;

public class AllStations implements Serializable {

    private HashMap<String, Station> allStations;
    private static String[] lineTagNames;

    public AllStations(){
        allStations = new HashMap<>();
        lineTagNames = new String[]{"red", "blue", "g", "brn", "p", "pexp", "y", "pnk", "o"};
    }

    public void buildStations(URL url) {
        String JSONString = HTTPSRequest.pullJSONFromHTTPSRequest(url);

        try {
            JSONArray arr = new JSONArray(JSONString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                if (allStations.keySet().contains(mapID)) continue;

                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));

                //fill routes array with true or false
                boolean[] routes = new boolean[lineTagNames.length];
                Arrays.fill(routes, Boolean.FALSE);
                for (int j = 0; j < lineTagNames.length; j++) {
                    if (obj.getString(lineTagNames[j]).equals("true")) {
                        routes[j] = true;
                    }
                }
                addStation(mapID, stationName, ada, routes);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void addStation(String mapID, String name, boolean ada, boolean[] routes){
        Station s = new Station(name, ada, routes);
        allStations.put(mapID, s);
    }

    public Station getStation(String s){
        try{
            return allStations.get(s);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Station> getAllStations(){
        return allStations;
    }
}
