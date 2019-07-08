package com.example.elevator_app.Models.Alerts;

import com.example.elevator_app.Models.Stations.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class AllAlerts implements Serializable {

    private final ArrayList<String> elevatorOutStationIDs;
    private final HashMap<String, Station> allStations;

    public AllAlerts(HashMap<String, Station> a) {
        elevatorOutStationIDs = new ArrayList<>();
        allStations = a;
    }

    public boolean buildAlerts(String JSONString) {
        elevatorOutStationIDs.clear();

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

                        String beginDateTime = convertDateTime(alert.getString("EventStart"));
                        addAlert(id, headline, shortDesc, beginDateTime);
                        break;
                    }
                }
            }
            return true;
        } catch (JSONException | NullPointerException e) {
            return false;
        }
    }

    public void addAlert(String id, String headline, String shortdesc, String beginDateTime){
        elevatorOutStationIDs.add(id);
        Station s = allStations.get(id);
        try{
            s.addAlert(new ElevatorAlert(headline, shortdesc, beginDateTime));
        } catch (NullPointerException e){
            e.printStackTrace();
        }
    }

    public ArrayList<String> getElevatorOutStationIDs(){
        return elevatorOutStationIDs;
    }

    private String convertDateTime(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss", Locale.US);
        try {
            Date originalDate = dateFormat.parse(s);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US);
            return dateFormat2.format(originalDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}