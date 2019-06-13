package com.example.elevator_app;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import android.widget.LinearLayout.LayoutParams;

public class ScrollingActivity extends AppCompatActivity {
    private TextView stationsTempOut;
    private LinearLayout linearLayout;
    private HashMap<String, String> favorites;
    //TODO: check for duplicate key entry from user
    private ArrayList<String> elevatorOutStationIDs;
    private HashMap<String, Station> allStations;
    //TODO: figure out how to store people's favorites in local memory

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stationsTempOut = findViewById(R.id.text_elevDownTempList);
        linearLayout = findViewById(R.id.linearLayout);
        elevatorOutStationIDs = new ArrayList<>();
        favorites = new HashMap<>();
        buildStations();
        buildAlerts();
    }

    public void buildStations(){
        //TODO: pull in real stations from URL
        allStations = new HashMap<>();
        allStations.put("1000", new Station("State/Lake", false, new String[]{"Brown", "Green", "Orange", "Pink", "Purple"}));
        //TODO: organize routes to reflect order on CTA site
        allStations.put("1001", new Station("Lake", true, new String[]{"Red"}));
        allStations.put("40780", new Station("Central Park", true, new String[]{"Pink"}));
    }

    public void buildAlerts(){
        new CurrentElevatorAlerts().execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx");
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
                        String beginDateTime = alertElem.getElementsByTagName("EventStart").item(0).getTextContent();
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
                                elevatorOutStationIDs.add(serviceId.item(0).getTextContent());
                                Station currentStation = allStations.get(serviceId);
                                currentStation.addAlert(currentAlert);
                            }
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                Log.d("Exception", e.toString());
            }
            return null;
        }

        protected void onPostExecute(Void v) {
            stationsTempOut.setTextColor(Color.BLACK);
            stationsTempOut.setTextSize(20);
            stationsTempOut.append("ELEVATORS TEMPORARILY DOWN");

            for (String str : elevatorOutStationIDs) {
                    //TODO: check to make sure station still exists in allStations
                    Station s = allStations.get(str);
                    TextView textView1 = new TextView(ScrollingActivity.this);
                    textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    textView1.setTextSize(15);
                    textView1.append("Station: " + s.getName() + "\n");
                    for (ElevatorAlert alert : s.getAlerts()) {
                        textView1.append(alert.getShortDesc() + "\n");
                    }
                    linearLayout.addView(textView1);
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
