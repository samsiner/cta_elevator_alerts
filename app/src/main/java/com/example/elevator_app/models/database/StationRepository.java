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
    private LiveData<List<Station>> mAllFavorites;

    public StationRepository(Application application) {
        StationRoomDatabase db = StationRoomDatabase.getDatabase(application);
        mStationDao = db.stationDao();
        buildStations();
        buildAlerts();
        mAllAlertStations = mStationDao.getAllAlertStation();
        mAllFavorites = mStationDao.getAllFavorites();
    }

    LiveData<List<Station>> mGetAllAlertStations() {
        return mAllAlertStations;
    }
    LiveData<List<Station>> mGetAllFavorites() {
        return mAllFavorites;
    }

    public void insert(Station station) {
        Thread thread = new Thread() {
            public void run() {
                mStationDao.insert(station);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void addAlert(Station station, String headline, String shortDesc, String beginDateTime){
        Thread thread = new Thread() {
            public void run() {
                station.addAlert(headline, shortDesc, beginDateTime);
                mStationDao.update(station);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void addFavorite(String stationID, String nickname){
        Thread thread = new Thread() {
            public void run() {
                mStationDao.addFavorite(stationID, nickname);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void removeFavorite(Station station){
        Thread thread = new Thread() {
            public void run() {
                station.removeFavorite();
                mStationDao.update(station);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }


    private void buildStations(){
        String JSONString = pullJSONFromWebService("https://data.cityofchicago.org/resource/8pix-ypme.json");
        try {
            JSONArray arr = new JSONArray(JSONString);

            for (int i = 0; i < arr.length(); i++) {
                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                String stationName = obj.getString("station_name");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));
                Station newStation = new Station(mapID);
                newStation.setName(stationName);
                newStation.setHasElevator(ada);
                insert(newStation);
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    private void buildAlerts(){
        String JSONString = pullJSONFromWebService("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");

        try {
            JSONObject outer = new JSONObject(JSONString);
            JSONObject inner = outer.getJSONObject("CTAAlerts");
            JSONArray arrAlerts = inner.getJSONArray("Alert");

            for (int i=0;i<arrAlerts.length();i++){
                JSONObject alert = (JSONObject) arrAlerts.get(i);
                String impact = alert.getString("Impact");
                if (!impact.equals("Elevator Status")) continue;

                JSONObject impactedService = alert.getJSONObject("ImpactedService");
                JSONArray service = impactedService.getJSONArray("Service");

                for (int j=0;j<service.length();j++){
                    JSONObject serviceInstance = (JSONObject) service.get(j);
                    if (serviceInstance.getString("ServiceType").equals("T")) {
                        String id = serviceInstance.getString("ServiceId");
                        String headline = alert.getString("Headline");

                        //Eliminates "back in service" alerts
                        if (headline.contains("Back in Service")) continue;

                        String shortDesc = alert.getString("ShortDescription");

                        String beginDateTime = alert.getString("EventStart");

                        Thread thread = new Thread() {
                            public void run() {
                                   Station station = mStationDao.getStation(id);
                                   if (station == null) Log.d("NULL", "NULL");
                                   else addAlert(station, headline, shortDesc, beginDateTime);
                            }
                        };
                        thread.start();
                        break;
                    }
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    private String pullJSONFromWebService(String url){
        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread() {
            public void run() {
                try {
                    URL urlStations = new URL(url);
                    Scanner scan = new Scanner(urlStations.openStream());
                    while (scan.hasNext()) sb.append(scan.nextLine());
                    scan.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    sb.append("");
                }
            }
        };

        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return sb.toString();
    }
}
