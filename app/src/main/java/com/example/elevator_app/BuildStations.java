package com.example.elevator_app;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class BuildStations extends AsyncTask<String, Void, Void> {

    private WeakReference<Activity> wactivity;

    public BuildStations(Activity activity){
        this.wactivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(String... urls) {
        MainActivity activity = ((MainActivity)wactivity.get());

        try {
            URL url = new URL(urls[0]);
            Scanner scan = new Scanner(url.openStream());
            String str = "";
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();

            JSONArray arr = new JSONArray(str);
            HashMap<String, Station> allStations = activity.getAllStations();

            //JSON tags for the lines
            String[] tagNames = new String[]{"red", "blue", "g", "brn", "p", "pexp", "y", "pnk", "o"};

            for (int i=0; i<arr.length();i++){
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                if (allStations.keySet().contains(mapID)) continue;

                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));

                //fill routes array with true or false
                boolean[] routes = new boolean[tagNames.length];
                Arrays.fill(routes, Boolean.FALSE);
                for(int j = 0; j < tagNames.length; j++){
                    if(obj.getString(tagNames[j]).equals("true")){routes[j] = true; }
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
