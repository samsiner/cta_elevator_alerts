package com.example.elevator_app;

import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class AllStations extends AsyncTask<String, Void, Void> {

    private String[] lineTagNames;
    HashMap<String, Station> allStations;

    public AllStations(){
        this.lineTagNames = new String[]{"red", "blue", "g", "brn", "p", "pexp", "y", "pnk", "o"};
        allStations = new HashMap<>();
    }

    public HashMap<String, Station> getAllStations(){ return allStations; }

    @Override
    protected Void doInBackground(String... urls) {
        try {
            URL url = new URL(urls[0]);
            Scanner scan = new Scanner(url.openStream());
            StringBuilder str = new StringBuilder();
            while (scan.hasNext()) str.append(scan.nextLine());
            scan.close();

            JSONArray arr = new JSONArray(str.toString());

            for (int i=0; i<arr.length();i++){
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                if (allStations.keySet().contains(mapID)) continue;

                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));

                //fill routes array with true or false
                boolean[] routes = new boolean[lineTagNames.length];
                Arrays.fill(routes, Boolean.FALSE);
                for(int j = 0; j < lineTagNames.length; j++){
                    if(obj.getString(lineTagNames[j]).equals("true")){routes[j] = true; }
                }
                Station currStation = new Station(stationName, ada, routes);
                allStations.put(mapID, currStation);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
