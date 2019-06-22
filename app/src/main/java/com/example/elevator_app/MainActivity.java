package com.example.elevator_app;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.LinearLayout.LayoutParams;

public class MainActivity extends AppCompatActivity {
    private TextView stationsTempOut, favoriteAlerts;
    private LinearLayout linearLayout;
    private ArrayList<String[]> favorites;
    private Button addFavorite;
    //TODO: check for duplicate key entry from user
    private ArrayList<String> elevatorOutStationIDs;
    final private HashMap<String, Station> allStations = new HashMap<>();
    //TODO: figure out how to store people's favorites in local memory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stationsTempOut = findViewById(R.id.text_elevDownTempList);
        favoriteAlerts = findViewById(R.id.text_favoritesList);
        linearLayout = findViewById(R.id.LinearLayout);
        addFavorite = findViewById(R.id.button_addFavorite);
        elevatorOutStationIDs = new ArrayList<>();
        favorites = new ArrayList<>();

        //TODO: Replace with functionality to add favorites
        //temporary data for testing
        favorites.add(new String[]{"Home", "40780"});
        favorites.add(new String[]{"Work", "41140"});

        buildButtonClickable();
        buildStationsAlerts();
        buildFavorites();
    }

    @Override
    protected void onResume(){
        super.onResume();
        try {
            Intent intent = getIntent();
            String nickname = intent.getStringExtra("Nickname");
            String stationID = intent.getStringExtra("stationID");
            favorites.add(new String[]{nickname, stationID});
            buildFavorites();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public void buildButtonClickable(){
        addFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddFavorite.class);
                intent.putExtra("AllStations", allStations);
                startActivity(intent);
            }
        });
    }

    public void buildFavorites(){
        favoriteAlerts.setText("");
        favoriteAlerts.setTextColor(Color.BLACK);
        favoriteAlerts.setTextSize(16);
        favoriteAlerts.append("Favorite Stations - Elevator Status\n\n");

        for (String[] favorite : favorites){
            favoriteAlerts.append(favorite[0] + "\n");
            try{
                String favoriteStationID = favorite[1];
                Station favoriteStation = allStations.get(favoriteStationID);
                favoriteAlerts.append(favoriteStation.getName() + "\n");
                boolean hasElevator = favoriteStation.getElevator();

                //Print elevator status
                if (!hasElevator){
                    favoriteAlerts.append("Status: No elevator present\n\n");
                }
                else if (elevatorOutStationIDs.contains(favoriteStationID)){
                    favoriteAlerts.append("Status: Elevator temporarily not working\n\n");
                }
                else {
                    favoriteAlerts.append("Status: All elevators working\n\n");
                }
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }


    public void buildStationsAlerts(){
        //TODO: pull in real stations from URL
        //TODO: organize routes to reflect order on CTA site
        try{
            new BuildStationsAlerts().execute("https://data.cityofchicago.org/resource/8pix-ypme.json", "http://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");
        } catch (Exception e){
            e.printStackTrace();
        }
    }
/*
    public void buildAlerts(){
        try{
            new BuildAllStations().execute("https://data.cityofchicago.org/api/views/8pix-ypme/rows.xml?accessType=DOWNLOAD").get();
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }
    }
*/
    class BuildStationsAlerts extends AsyncTask<String, Void, Void>{

        protected Void doInBackground(String... urls) {
            //BUILD STATIONS
            try {
                URL url = new URL(urls[0]);
                Scanner scan = new Scanner(url.openStream());
                String str = "";
                while (scan.hasNext())
                    str += scan.nextLine();
                scan.close();

                JSONArray arr = new JSONArray(str);

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

            //BUILD ALERTS
            try {
                URL url = new URL(urls[1]);
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
                Log.d("allIDs", elevatorOutStationIDs.toString());
                Log.d("size", Integer.toString(allStations.size()));
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        return null;
        }

        public String convertDateTime(String s){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd HH:mm");
            Date convertedDate = new Date();
            try {
                convertedDate = dateFormat.parse(s);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return convertedDate.toString();
        }
        protected void onPostExecute(Void v) {
            stationsTempOut.setTextColor(Color.BLACK);
            stationsTempOut.setTextSize(20);
            stationsTempOut.append("ELEVATORS TEMPORARILY DOWN");

            for (String str : elevatorOutStationIDs) {
                //TODO: check to make sure station still exists in allStations
                //Create new TextView with the alert
                try {
                    final Station s = allStations.get(str);
                    TextView textView1 = new TextView(MainActivity.this);
                    textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    textView1.setTextSize(15);
                    textView1.append("Station: " + s.getName() + "\n");
                    textView1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
                            intent.putExtra("Station", s);
                            startActivity(intent);
                        }
                    });
                    linearLayout.addView(textView1);

                } catch (Exception e){
                    Log.d("Exception", e.toString());
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scrolling, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
