package com.example.elevator_app;

import android.app.Activity;
import android.os.AsyncTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class AllAlerts extends AsyncTask<String, Void, Void> {

    private WeakReference<Activity> wactivity;

    public AllAlerts(Activity activity){
        this.wactivity = new WeakReference<>(activity);
    }

    @Override
    protected Void doInBackground(String... urls) {
        MainActivity activity = ((MainActivity)wactivity.get());
        ArrayList<String> elevatorOutStationIDs = activity.getElevatorOutStationIDs();
        HashMap<String, Station> allStations = activity.getAllStations();

        try {
            URL url = new URL(urls[0]);
            Scanner scan = new Scanner(url.openStream());
            String str = "";
            while (scan.hasNext())
                str += scan.nextLine();
            scan.close();

            JSONObject outer = new JSONObject(str);
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
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String convertDateTime(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss");
        try {
            Date convertedDate = dateFormat.parse(s);
            return convertedDate.toString();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
