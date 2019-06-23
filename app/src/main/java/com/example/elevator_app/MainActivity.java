package com.example.elevator_app;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private TextView stationsTempOut, favoriteAlerts;
    private LinearLayout linearLayout;
    private ArrayList<String[]> favorites;
    private Button addFavorite;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    //TODO: check for duplicate key entry from user
    private ArrayList<String> elevatorOutStationIDs;
    private HashMap<String, Station> allStations = new HashMap<>();
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

        //Set up SharedPreferences
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();

        //TODO: Replace with functionality to add favorites
        //temporary data for testing
        favorites.add(new String[]{"Home", "40780"});
        favorites.add(new String[]{"Work", "41140"});

        buildButtonClickable();
        buildStations();
        buildAlerts();
        buildFavorites();
    }

    public HashMap<String, Station> getAllStations(){ return allStations; }
    public ArrayList<String> getElevatorOutStationIDs(){ return elevatorOutStationIDs;}

    @Override
    protected void onResume(){
        super.onResume();
        try {
            Intent intent = getIntent();
            String nickname = intent.getStringExtra("Nickname");
            String stationID = intent.getStringExtra("stationID");
            favorites.add(new String[]{nickname, stationID});
            if (nickname != null) {
                favoriteAlerts.append(nickname + "\n");
                favoriteAlerts.append(allStations.get(stationID).getName() + "\n");
                favoriteAlerts.append(elevatorStatus(stationID));
            }
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

        for (String[] favorite : favorites)
        {
            try{
                favoriteAlerts.append(favorite[0] + "\n");
                String favoriteStationID = favorite[1];
                Station favoriteStation = allStations.get(favoriteStationID);
                String name = favoriteStation.getName() + "\n";
                favoriteAlerts.append(name);
                favoriteAlerts.append(elevatorStatus(favoriteStationID));
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public String elevatorStatus(String ID){
        boolean hasElevator = allStations.get(ID).getElevator();

        if (!hasElevator){
            return "Status: No elevator present\n\n";
        }
        else if (elevatorOutStationIDs.contains(ID)){
            return "Status: Elevator temporarily not working\n\n";
        }
        else {
            return "Status: All elevators working\n\n";
        }
    }

    public void buildStations(){
        //TODO: build stations into local database
        //TODO: organize routes to reflect order on CTA site
        try{
            BuildStations bs = new BuildStations(this);
            bs.execute("https://data.cityofchicago.org/resource/8pix-ypme.json").get();
            bs.cancel(true);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void buildAlerts(){
        try{
            BuildAlerts ba = new BuildAlerts(this);
            ba.execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON").get();
            ba.cancel(true);
            displayAlerts();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void displayAlerts() {
        stationsTempOut.setTextColor(Color.BLACK);
        stationsTempOut.setTextSize(20);
        stationsTempOut.append("ELEVATORS TEMPORARILY DOWN");

        for (String str : elevatorOutStationIDs) {
            //TODO: check to make sure station still exists in allStations
            //Create new TextView with the alert
            try {
                final Station s = allStations.get(str);
                TextView textView1 = new TextView(this);
                textView1.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
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

            } catch (Exception e) {
                Log.d("Exception", e.toString());
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
