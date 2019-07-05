package com.example.elevator_app.Models.Alerts;

import com.example.elevator_app.HttpsRequest.HTTPSRequest;
import com.example.elevator_app.Models.Stations.Station;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class AllAlerts {

    private ArrayList<String> elevatorOutStationIDs;
    private HashMap<String, Station> allStations;

    public AllAlerts(HashMap<String, Station> a) {
        elevatorOutStationIDs = new ArrayList<>();
        allStations = a;
    }

    public void buildAlerts(URL url) {
        String JSONString = HTTPSRequest.pullJSONFromHTTPSRequest(url);
        buildAlerts(JSONString);
    }

    //Created for unit testing
    public void buildAlerts(String JSONString){
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

                        String shortdesc = alert.getString("ShortDescription");
                        //TODO: Clean up full description
                        String fulldesc = alert.getString("FullDescription");
                        //TODO: Clean up date/time conversion
                        String beginDateTime = convertDateTime(alert.getString("EventStart"));
                        elevatorOutStationIDs.add(id);
                        Station s = allStations.get(id);
                        s.addAlert(new ElevatorAlert(headline, shortdesc, fulldesc, beginDateTime));
                        break;
                    }
                }
            }
        } catch (JSONException | NullPointerException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<String> getElevatorOutStationIDs(){
        return elevatorOutStationIDs;
    }

    public String convertDateTime(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss");
        try {
            Date convertedDate = dateFormat.parse(s);
            return convertedDate.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }
}
