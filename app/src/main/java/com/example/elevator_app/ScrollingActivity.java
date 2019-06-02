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

import org.w3c.dom.*;
import org.xml.sax.SAXException;
import javax.xml.parsers.*;
import android.widget.LinearLayout.LayoutParams;

public class ScrollingActivity extends AppCompatActivity {

    private TextView stationsTempOut;
    private LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearLayout = findViewById(R.id.linearLayout);
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
                for (int i = 0; i < nList.getLength(); i++) {
                    Element alertElem = (Element) nList.item(i);
                    if (alertElem.getElementsByTagName("Impact").item(0).getTextContent().equals("Elevator Status")) {
                        //Create new ElevatorAlert object for each elevator alert
                        ElevatorAlert newalert = new ElevatorAlert();
                        elevatorAlerts.add(newalert);

                        newalert.setHeadline(alertElem.getElementsByTagName("Headline").item(0).getTextContent());
                        newalert.setShortDesc(alertElem.getElementsByTagName("ShortDescription").item(0).getTextContent());
                        newalert.setFullDesc(alertElem.getElementsByTagName("FullDescription").item(0).getTextContent());

                        //Add station and line info to ElevatorAlert
                        NodeList impactedService = alertElem.getElementsByTagName("ImpactedService");
                        NodeList service = ((Element) impactedService.item(0)).getElementsByTagName("Service");

                        for (int j = 0; j < service.getLength(); j++) {
                            Element elem = (Element) service.item(j);
                            NodeList serviceType = elem.getElementsByTagName("ServiceType");
                            NodeList serviceName = elem.getElementsByTagName("ServiceName");
                            NodeList backColor = elem.getElementsByTagName("ServiceBackColor");
                            NodeList textColor = elem.getElementsByTagName("ServiceTextColor");

                            if (serviceType.item(0).getTextContent().equals("T")) {
                                newalert.setStation(serviceName.item(0).getTextContent());
                            }
                            else if (serviceType.item(0).getTextContent().equals("R")) {
                                String[] str = new String[3];
                                str[0] = serviceName.item(0).getTextContent();
                                str[1] = backColor.item(0).getTextContent();
                                str[2] = textColor.item(0).getTextContent();
                                newalert.addRoutesWithColors(str);
                            }

                        }
                    }
                }
            } catch (IOException | ParserConfigurationException | SAXException e) {
                Log.d("Exception", e.toString());
            }
            return elevatorAlerts;
        }

        protected void onPostExecute(ArrayList<ElevatorAlert> alerts) {
            stationsTempOut.setTextColor(Color.BLACK);
            stationsTempOut.setTextSize(20);
            stationsTempOut.append("ELEVATORS TEMPORARILY DOWN");

            for (ElevatorAlert elevAlert : alerts) {
                for (String[] str : elevAlert.getRoutesWithColors()) {
                    // Add textview for each individual alert
                    TextView textView1 = new TextView(ScrollingActivity.this);
                    textView1.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                    textView1.setTextSize(15);
                    textView1.append("Route: " + str[0] + "\n");
                    textView1.append("Station: " + elevAlert.getStation() + "\n");
                    textView1.append(elevAlert.getShortDesc() + "\n");
                    textView1.setBackgroundColor(Color.parseColor("#" + str[1]));
                    textView1.setTextColor(Color.parseColor("#" + str[2]));
                    linearLayout.addView(textView1);
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
