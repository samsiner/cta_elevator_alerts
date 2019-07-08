//TODO: Add JavaDoc comments to all classes

package com.example.elevator_app.models.stations;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;

public class AllStations implements Serializable {

    private final HashMap<String, Station> allStations;
    private static String[] lineTagNames;

    public AllStations(){
        allStations = new HashMap<>();
        lineTagNames = new String[]{"red", "blue", "g", "brn", "p", "pexp", "y", "pnk", "o"};
    }

    public boolean buildStations(String JSONString) {
        try {
            JSONArray arr = new JSONArray(JSONString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");

                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));

                if (allStations.keySet().contains(mapID)){
                    boolean[] routes = allStations.get(mapID).getRoutes();
                    for(int j = 0; j < lineTagNames.length; j++){
                        if(obj.getString(lineTagNames[j]).equals("true") && !routes[j]){
                            routes[j] = true;
                        }
                    }
                } else{
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
            }
            return true;
        } catch (JSONException e){
            return false;
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
