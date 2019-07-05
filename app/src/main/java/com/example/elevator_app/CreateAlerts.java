package com.example.elevator_app;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class CreateAlerts {

    private HashMap<String, Station> allStations;
    private ArrayList<String> elevatorOutStationIDs;

    public CreateAlerts(HashMap<String, Station> allStations){
        this.allStations = allStations;
        elevatorOutStationIDs = new ArrayList<>();
    }

    public ArrayList<String> buildAlerts(){
        Thread thread = new Thread(){
            public void run(){
                try {
                    URL url = new URL("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");
                    Scanner scan = new Scanner(url.openStream());
                    String sb = "";
                    while (scan.hasNext()) sb += scan.nextLine();
                    scan.close();

                    JSONObject outer = new JSONObject(sb);
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
                } catch (IOException | JSONException | NullPointerException e) {
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
