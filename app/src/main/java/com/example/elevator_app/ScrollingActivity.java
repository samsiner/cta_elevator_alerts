package com.example.elevator_app;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;

public class ScrollingActivity extends AppCompatActivity {

    private TextView stationsTempOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        stationsTempOut = findViewById(R.id.text_elevDownTempList);
        new displayTemporaryElevatorOutAlerts().execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx");
    }

    class displayTemporaryElevatorOutAlerts extends AsyncTask<String, Void, ArrayList<ElevatorAlert>> {

        protected ArrayList<ElevatorAlert> doInBackground(String... urls) {
            ArrayList<ElevatorAlert> elevatorAlerts = new ArrayList<>();

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
                Log.d("NumberAlerts", Integer.toString(nList.getLength()));
                for (int i = 0; i < nList.getLength(); i++) {
                    Element alertElem = (Element) nList.item(i);
                    if (alertElem.getElementsByTagName("Impact").item(0).getTextContent().equals("Elevator Status")) {
                        Log.d("Elevator?", "YES");

                        //Create new ElevatorAlert object for each elevator alert
                        ElevatorAlert newalert = new ElevatorAlert();
                        elevatorAlerts.add(newalert);

                        newalert.setHeadline(alertElem.getElementsByTagName("Headline").item(0).getTextContent());
                        newalert.setShortDesc(alertElem.getElementsByTagName("ShortDescription").item(0).getTextContent());
                        newalert.setFullDesc(alertElem.getElementsByTagName("FullDescription").item(0).getTextContent());

                        //Add station and line info to ElevatorAlert
                        NodeList impactedService = alertElem.getElementsByTagName("ImpactedService");
                        NodeList service = ((Element) impactedService.item(0)).getElementsByTagName("Service");
                        Log.d("NumberService", Integer.toString(service.getLength()));

                        for (int j = 0; j < service.getLength(); j++) {
                            NodeList serviceType = ((Element) service.item(j)).getElementsByTagName("ServiceType");
                            NodeList serviceName = ((Element) service.item(j)).getElementsByTagName("ServiceName");

                            if (serviceType.item(0).getTextContent().equals("T")) {
                                newalert.setStation(serviceName.item(0).getTextContent());
                                Log.d("Station", newalert.getStation());
                            }
                            if (serviceType.item(0).getTextContent().equals("R")) {
                                newalert.addRoutes(serviceName.item(0).getTextContent());
                            }
                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                Log.d("Exception", e.toString());
            } finally {
                Log.d("NumberElevatorAlerts", Integer.toString(elevatorAlerts.size()));
                return elevatorAlerts;
            }
        }

        protected void onPostExecute(ArrayList<ElevatorAlert> alerts) {
            for (ElevatorAlert elevAlert : alerts) {
                stationsTempOut.append("Station: ");
                stationsTempOut.append(elevAlert.getStation() + "\n");

                for (String s : elevAlert.getRoutes()) {
                    stationsTempOut.append("Route: ");
                    stationsTempOut.append(s + "\n");
                }
                stationsTempOut.append(elevAlert.getShortDesc() + "\n\n");
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
