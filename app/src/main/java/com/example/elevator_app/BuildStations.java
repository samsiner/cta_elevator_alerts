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

            for (int i=0; i<arr.length();i++){
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                if (allStations.keySet().contains(mapID)) continue;

                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));
                boolean[] routes = new boolean[9];
                Arrays.fill(routes, Boolean.FALSE);

                if(obj.getString("red").equals("true")){ routes[0] = true; }
                if(obj.getString("blue").equals("true")){ routes[1] = true; }
                if(obj.getString("g").equals("true")){ routes[2] = true; }
                if(obj.getString("brn").equals("true")){ routes[3] = true; }
                if(obj.getString("p").equals("true")){ routes[4] = true; }
                if(obj.getString("pexp").equals("true")){ routes[5] = true; }
                if(obj.getString("y").equals("true")){ routes[6] = true; }
                if(obj.getString("pnk").equals("true")){ routes[7] = true; }
                if(obj.getString("o").equals("true")){ routes[8] = true; }

                Station currStation = new Station(stationName, ada, routes);
                allStations.put(mapID, currStation);
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
