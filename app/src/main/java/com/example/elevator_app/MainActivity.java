package com.example.elevator_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    AllAlerts allAlerts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);

        //Set up SharedPreferences to save favorites locally
        sharedPref = this.getSharedPreferences("name", MODE_PRIVATE);

        updateAlerts();
        updateFavorites();
    }

    public void toAddFavoriteActivity(View v){
        startActivity(new Intent(MainActivity.this, AddFavoriteActivity.class));
    }

    public void toAllLinesActivity(View v){
        startActivity(new Intent(MainActivity.this, AllLinesActivity.class));
    }

    public void updateFavorites(){
        //TODO: check for duplicate key entry from user
        Intent intent = getIntent();
        String nickname = intent.getStringExtra("Nickname");
        String stationID = intent.getStringExtra("stationID");
        if(nickname != null){
            editor = sharedPref.edit();
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
                final Station s = AllStationsSingleton.getInstance().getStation(id);
                station.setText(s.getName());

                //Check if elevator is out or doesn't exist
                final boolean hasElev = s.getElevator();
                final boolean outElev = allAlerts.getElevatorOutStationIDs().contains(id);
                if (hasElev && !outElev){
                    status.setImageResource(R.drawable.status_green);
                } else{
                    //Add red button
                    //status.setImageResource(R.drawable.status_red);
                }

                myLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
                        intent.putExtra("Station", s);
                        //If there is no elevator or it works, add different text
                        if (!hasElev) intent.putExtra("Text", "No elevator present at station.");
                        else if (!outElev) intent.putExtra("Text", "Elevator is present and working at station.");
                        startActivity(intent);
                    }
                });

                favoriteLayout.addView(myLayout);

            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }

    public void updateAlerts(){
        try{
            allAlerts = new AllAlerts();

            try{
                allAlerts.execute("http://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON").get();
            } catch (Exception e){
                e.printStackTrace();
            }

            LinearLayout alertLayout = findViewById(R.id.linear_elevDownTempList);
            alertLayout.removeAllViews();
            LayoutInflater inflater = getLayoutInflater();

            for (String str : allAlerts.getElevatorOutStationIDs())
            {
                try{
                    View myLayout = inflater.inflate(R.layout.alert_station, alertLayout, false);
                    TextView stationView = myLayout.findViewById(R.id.text_favorite_station);
                    ImageView statusView = myLayout.findViewById(R.id.image_elev_status);

                    final Station s = AllStationsSingleton.getInstance().getStation(str);
                    stationView.setText(s.getName());
                    //statusView.setImageResource(status_red);
                    myLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(MainActivity.this, DisplayAlertActivity.class);
                            intent.putExtra("Station", s);
                            startActivity(intent);
                        }
                    });
                    alertLayout.addView(myLayout);

                } catch (NullPointerException e){
                    e.printStackTrace();
                }
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}