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
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
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
        favorites.add(new String[]{"Friend", "1000"});
        favorites.add(new String[]{"Mom", "1001"});

        buildButtonClickable();
        buildStations();
        buildAlerts();
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

    public void buildStations(){
        //TODO: pull in real stations from URL
        allStations.put("1000", new Station("State/Lake", false, new String[]{"Brown", "Green", "Orange", "Pink", "Purple"}));
        //TODO: organize routes to reflect order on CTA site
        allStations.put("1001", new Station("Lake", true, new String[]{"Red"}));
        allStations.put("40780", new Station("Central Park", true, new String[]{"Pink"}));
        allStations.put("41140", new Station("King Drive", true, new String[]{"Green"}));
    }

    public void buildAlerts(){
        try{
            new CurrentElevatorAlerts().execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx").get();
        } catch (ExecutionException | InterruptedException e){
            e.printStackTrace();
        }

    }

    class CurrentElevatorAlerts extends AsyncTask<String, Void, Void> {

        protected Void doInBackground(String... urls) {
            try {
                //Call API and create document to parse XML using DOM
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream is = urlConnection.getInputStream();

                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docbuilder = factory.newDocumentBuilder();
                Document doc = docbuilder.parse(is);
                doc.getDocumentElement().normalize();
                is.close();

                //Pull out elevator alerts
                NodeList nList = doc.getElementsByTagName("Alert");
                for (int i = 0; i < nList.getLength(); i++) {
                    Element alertElem = (Element) nList.item(i);
                    if (alertElem.getElementsByTagName("Impact").item(0).getTextContent().equals("Elevator Status")) {
                        //Create new ElevatorAlert object for each elevator alert

                        String headline = alertElem.getElementsByTagName("Headline").item(0).getTextContent();
                        String shortDesc = alertElem.getElementsByTagName("ShortDescription").item(0).getTextContent();
                        String longDesc = alertElem.getElementsByTagName("FullDescription").item(0).getTextContent();
                        String beginDateTime = convertDateTime(alertElem.getElementsByTagName("EventStart").item(0).getTextContent());
                        //TODO: convert date time to readable format

                        ElevatorAlert currentAlert = new ElevatorAlert(headline, shortDesc, longDesc, beginDateTime);

                        //Add station and line info to ElevatorAlert
                        NodeList impactedService = alertElem.getElementsByTagName("ImpactedService");
                        NodeList services = ((Element) impactedService.item(0)).getElementsByTagName("Service");

                        for (int j = 0; j < services.getLength(); j++) {
                            Element elem = (Element) services.item(j);
                            NodeList serviceType = elem.getElementsByTagName("ServiceType");
                            NodeList serviceId = elem.getElementsByTagName("ServiceId");

                            if (serviceType.item(0).getTextContent().equals("T")) {
                                //TODO: error checking for station existing in allStations
                                try {
                                    String currStationID = serviceId.item(0).getTextContent();
                                    elevatorOutStationIDs.add(currStationID);
                                    Log.d("IDs", elevatorOutStationIDs.toString());
                                    //Add alert into associated Station
                                    if (allStations.containsKey(currStationID)) {
                                        Station currentStation = allStations.get(currStationID);
                                        currentStation.addAlert(currentAlert);
                                    }
                                } catch (NullPointerException e){
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                e.printStackTrace();
            }
            return null;
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
//                        for (ElevatorAlert alert : s.getAlerts()) {
//                            textView1.append(alert.getShortDesc() + "\n");
//                        }
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
