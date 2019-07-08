package com.example.elevator_app.Activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elevator_app.HttpsRequest.HTTPSRequest;
import com.example.elevator_app.Models.Alerts.AllAlerts;
import com.example.elevator_app.Models.Stations.AllStations;
import com.example.elevator_app.R;
import com.example.elevator_app.Models.Stations.Station;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    //TODO: Figure out what an adapter is. Do we need to create one?
    //TODO: Fragments?
    //TODO: Proguard?
    //TODO: Instabug?
    //TODO: Refresh?
    //TODO: Lines Activity
    //TODO: Make error catching more specific

    private SharedPreferences sharedPref;
    private AllAlerts allAlerts;
    private AllStations allStations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);

        boolean buildSuccessful = false;
        try{
            //Pull in savedInstanceState containing stations and alerts
            AllStations tempAllStations = (AllStations) savedInstanceState.getSerializable("allStations");
            AllAlerts tempAllAlerts = (AllAlerts) savedInstanceState.getSerializable("allAlerts");
            allStations = tempAllStations;
            allAlerts = tempAllAlerts;
        } catch (NullPointerException e){
            buildSuccessful = buildStationsAlerts();
        }

        if (buildSuccessful) {
            //Set up SharedPreferences to save favorites locally
            sharedPref = this.getSharedPreferences("name", MODE_PRIVATE);
            buildFavoriteViews();
        }
    }

    private boolean buildStationsAlerts(){
        //Try to build stations; if connection error, show dialog.
        allStations = new AllStations();
        String stationsJSON = HTTPSRequest.pullJSONFromHTTPSRequest("https://data.cityofchicago.org/resource/8pix-ypme.json");
        boolean stationBuildWorked = allStations.buildStations(stationsJSON);

        if (stationBuildWorked){
            //Try to build alerts; if connection error, show dialog.
            allAlerts = new AllAlerts(allStations.getAllStations());
            String alertsJSON = HTTPSRequest.pullJSONFromHTTPSRequest("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");
            boolean alertBuildWorked = allAlerts.buildAlerts(alertsJSON);

            if (alertBuildWorked){
                buildAlertViews();
                return true;
            }
        }
        dialogPositiveButton("Error", "Connection did not work. Please refresh to try again.");
        return false;
    }

    private void dialogPositiveButton(String title, String message){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("OK", null);
        alert.show();
    }

    public void toAddFavoriteActivity(View v){
        Intent intent = new Intent(MainActivity.this, AddFavoriteActivity.class);
        intent.putExtra("allStations", allStations);
        startActivity(intent);
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(MainActivity.this, AllLinesActivity.class);
        intent.putExtra("allStations", allStations);
        startActivity(intent);
    }

    private void buildAlertViews(){
        LinearLayout alertLayout = findViewById(R.id.linear_elevDownTempList);
        alertLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        for (String stationID : allAlerts.getElevatorOutStationIDs())
        {
            try{
                View myLayout = inflater.inflate(R.layout.alert_station, alertLayout, false);
                TextView stationView = myLayout.findViewById(R.id.text_favorite_station);
                //ImageView statusView = myLayout.findViewById(R.id.image_elev_status);

                final Station s = allStations.getStation(stationID);
                stationView.setText(s.getName());
                //statusView.setImageResource(status_red);

                myLayout.setOnClickListener(v -> {
                    Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
                    intent.putExtra("Station", s);
                    startActivity(intent);
                });
                alertLayout.addView(myLayout);
            } catch (NullPointerException e){
                dialogPositiveButton("Alerts Error", "Alerts not loading correctly! Please refresh to try again.");
            }
        }
    }

    private void buildFavoriteViews(){
        //TODO: check for duplicate key entry from user
        //TODO: consider visitor design pattern for duplicate code (updateFavorites() and updateAlerts())
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("Nickname");
        String stationID = intent.getStringExtra("stationID");
        if(nickname != null){
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString(nickname, stationID);
            editor.apply();
        }
        LinearLayout favoriteLayout = findViewById(R.id.linear_favorite_stations);
        favoriteLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        @SuppressWarnings("unchecked")
        HashMap<String, String> favorites = (HashMap) sharedPref.getAll();
        for (String str : favorites.keySet())
        {
            try{
                View myLayout = inflater.inflate(R.layout.favorite_station, favoriteLayout, false);
                TextView nicknameView = myLayout.findViewById(R.id.text_favorite_nickname);
                TextView station = myLayout.findViewById(R.id.text_favorite_station);
                ImageView status = myLayout.findViewById(R.id.image_elev_status);

                nicknameView.setText(str);
                String id = favorites.get(str);
                final Station s = allStations.getStation(id);
                station.setText(s.getName());

                //Check if elevator is out or doesn't exist
                final boolean hasElev = s.getElevator();
                final boolean outElev = allAlerts.getElevatorOutStationIDs().contains(id);
                if (hasElev && !outElev){
                    status.setImageResource(R.drawable.status_green);
                }
                //TODO: add red button
//                else{
//                    //Add red button
//                    status.setImageResource(R.drawable.status_red);
//                }


                myLayout.setOnClickListener(v -> {
                    Intent intent1 = new Intent(MainActivity.this, DisplayAlertActivity.class);
                    intent1.putExtra("Station", s);
                    //If there is no elevator or it works, add different text
                    if (!hasElev) intent1.putExtra("Text", "No elevator present at station.");
                    else if (!outElev) intent1.putExtra("Text", "Elevator is present and working at station.");
                    startActivity(intent1);
                });

                favoriteLayout.addView(myLayout);

            } catch (NullPointerException e){
                dialogPositiveButton("Favorites Error", "Favorites not loading correctly! Please refresh to try again.");
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putSerializable("allStations", allStations);
        savedInstanceState.putSerializable("allAlerts", allAlerts);
    }
}