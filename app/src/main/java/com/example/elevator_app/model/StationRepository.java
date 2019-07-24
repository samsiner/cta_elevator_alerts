package com.example.elevator_app.model;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class StationRepository {

    private final StationDao mStationDao;
    private boolean duplicateStation;
    private ArrayList<String> favoriteElevatorNewlyWorking = new ArrayList<>();
    private ArrayList<String> favoriteElevatorNewlyOut = new ArrayList<>();

    private static volatile StationRepository INSTANCE;

    public static StationRepository getInstance(Application application) {
        if (INSTANCE == null) {
            synchronized (StationRepository.class) {
                if (INSTANCE == null) {
                    INSTANCE = new StationRepository(application);
                }
            }
        }
        return INSTANCE;
    }

    private StationRepository(Application application) {
        StationRoomDatabase db = StationRoomDatabase.getDatabase(application);
        mStationDao = db.stationDao();
        buildStations();
//        buildAlerts();
    }

    public LiveData<List<Station>> mGetAllAlertStations() {
        return mStationDao.getAllAlertStations();
    }
    public LiveData<List<Station>> mGetAllFavorites() {
        return mStationDao.getAllFavorites();
    }

    private String s;
    public String mGetStationName(String stationID){
        Thread thread = new Thread() {
            public void run() {
                s = mStationDao.getName(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return s;
    }

    private void insert(Station station) {
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

    private int count = 0;
    public int getFavoritesCount() {
        Thread thread = new Thread() {
            public void run() {
                count = mStationDao.getFavoritesCount();
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return count;
    }

    private int countAlerts = 0;
    public int getAlertsCount() {
        Thread thread = new Thread() {
            public void run() {
                countAlerts = mStationDao.getAlertsCount();
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return countAlerts;
    }

    private boolean hasElevator = false;
    public boolean mGetHasElevator(String stationID) {
        Thread thread = new Thread() {
            public void run() {
                hasElevator = mStationDao.getHasElevator(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return hasElevator;
    }

    private boolean hasElevatorAlert = false;
    public boolean mGetHasElevatorAlert(String stationID){
        Thread thread = new Thread() {
            public void run() {
                hasElevatorAlert = mStationDao.getHasElevatorAlert(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return hasElevatorAlert;
    }

    private void addAlert(Station station, String shortDesc, String beginDateTime){
        Thread thread = new Thread() {
            public void run() {
                station.addAlert(shortDesc, beginDateTime);
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

    public List<String> getAlertDetails(String stationID){
        final List<String> list2 = new ArrayList<>();

        Thread thread = new Thread() {
            public void run() {
                list2.add(0, mStationDao.getName(stationID));
                list2.add(1, mStationDao.getShortDescription(stationID));
                list2.add(2, mStationDao.getBeginDateTime(stationID));
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return list2;
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

    public void removeFavorite(String stationID){
        Thread thread = new Thread() {
            public void run() {
                mStationDao.removeFavorite(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    boolean isFavorite = false;
    public boolean isFavorite(String stationID){
        Thread thread = new Thread() {
            public void run() {
                isFavorite = mStationDao.isFavoriteStation(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        return isFavorite;
    }

    public void removeAlert(String stationID){
        Thread thread = new Thread() {
            public void run() {
                mStationDao.removeAlert(stationID);
            }
        };
        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    private ArrayList<String> allAlertStationIDs;
    private void getAllAlertStationIDs(){
        Thread thread = new Thread() {
            public void run() {
                allAlertStationIDs = (ArrayList) mStationDao.getAllAlertStationIDs();
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
                duplicateStation = false;

                JSONObject obj = (JSONObject) arr.get(i);
                String mapID = obj.getString("map_id");
                boolean ada = Boolean.parseBoolean(obj.getString("ada"));
                boolean red = Boolean.parseBoolean(obj.getString("red"));
                boolean blue = Boolean.parseBoolean(obj.getString("blue"));
                boolean brown = Boolean.parseBoolean(obj.getString("brn"));
                boolean green = Boolean.parseBoolean(obj.getString("g"));
                boolean orange = Boolean.parseBoolean(obj.getString("o"));
                boolean pink = Boolean.parseBoolean(obj.getString("pnk"));
                boolean purple = Boolean.parseBoolean(obj.getString("p")) || Boolean.parseBoolean(obj.getString("pexp"));
                boolean yellow = Boolean.parseBoolean(obj.getString("y"));

                Thread thread = new Thread() {
                    public void run() {
                        Station station = mStationDao.getStation(mapID);
                        if (station != null){
                            duplicateStation = true;
                            station.updateLines(red, blue, brown, green, orange, pink, purple, yellow);
                            mStationDao.update(station);
                        }
                    }
                };
                thread.start();
                try{
                    thread.join();
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                if(!duplicateStation){
                    String stationName = obj.getString("station_name");
                    //name length is too long for this station
                    if(stationName.equals("Harold Washington Library-State/Van Buren")){
                        stationName = "Harold Washington Library";
                    }

                    Station newStation = new Station(mapID);
                    newStation.setName(stationName);
                    newStation.setHasElevator(ada);
                    newStation.setRoutes(red, blue, brown, green, orange, pink, purple, yellow);
                    insert(newStation);
                }
            }
        } catch (JSONException e){
            e.printStackTrace();
        }
    }

    public void buildAlerts(String JSONString){
        //String JSONString = pullJSONFromWebService("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");

        getAllAlertStationIDs();
        ArrayList<String> afterStationsOut = new ArrayList<>();

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

                        if (headline.contains("Back in Service")) continue;

                        afterStationsOut.add(id);
                        if (mGetHasElevatorAlert(id)) continue;

                        String shortDesc = alert.getString("ShortDescription");
                        String beginDateTime = alert.getString("EventStart");

                        Thread thread = new Thread() {
                            public void run() {
                               try{
                                   Station station = mStationDao.getStation(id);
                                   addAlert(station, shortDesc, beginDateTime);
                               } catch(NullPointerException e){
                                   e.printStackTrace();
                               }
                            }
                        };
                        thread.start();
                        try{
                            thread.join();
                        } catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        break;
                    }
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }

        for (String id : allAlertStationIDs){
            if (!afterStationsOut.contains(id)){
                removeAlert(id);
                if (isFavorite(id)) favoriteElevatorNewlyWorking.add(id);
            }
        }

        for (String id : afterStationsOut){
            if (!allAlertStationIDs.contains(id)){
                if (isFavorite(id)) favoriteElevatorNewlyOut.add(id);
            }
        }
    }

    public List<String> getFavoriteElevatorNewlyWorking(){ return favoriteElevatorNewlyWorking; }
    public List<String> getFavoriteElevatorNewlyOut(){ return favoriteElevatorNewlyOut; }

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