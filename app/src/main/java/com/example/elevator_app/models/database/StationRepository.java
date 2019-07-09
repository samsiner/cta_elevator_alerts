package com.example.elevator_app.models.database;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class StationRepository {
    private StationDao mStationDao;
    private LiveData<List<Station>> mAllAlertStations;

    StationRepository(Application application) {
        StationRoomDatabase db = StationRoomDatabase.getDatabase(application);
        mStationDao = db.stationDao();
        mAllAlertStations = mStationDao.getAllAlertStation();
        //buildStations();
    }

    LiveData<List<Station>> mGetAllAlertStations() {
        return mAllAlertStations;
    }

    public void insert(Station station) {
        Thread thread = new Thread() {
            public void run() {
                mStationDao.insert(station);
            }
        };
        thread.start();
    }

    public void addAlert(Station station, String headline, String shortDesc, String beginDateTime){
        Thread thread = new Thread() {
            public void run() {
                station.addAlert(headline, shortDesc, beginDateTime);
                mStationDao.update(station);
            }
        };
        thread.start();
    }

    private void buildStations(){
        String JSONString = pullStations();
        try {
            JSONArray arr = new JSONArray(JSONString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));
                Station newStation = new Station(mapID, stationName, ada);
                insert(newStation);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private String pullStations(){
        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread() {
            public void run() {
                try {
                    URL urlStations = new URL("https://data.cityofchicago.org/resource/8pix-ypme.json");
                    Scanner scan = new Scanner(urlStations.openStream());
                    Log.d("1", "1");
                    while (scan.hasNext()) sb.append(scan.nextLine());
                    Log.d("JSON", sb.toString());
                    scan.close();
                } catch (IOException e) {
                    sb.append("");
                }
            }
        };

        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){ e.printStackTrace();}

        return sb.toString();
    }
}
