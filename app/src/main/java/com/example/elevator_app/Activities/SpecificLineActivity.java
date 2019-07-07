package com.example.elevator_app.Activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elevator_app.Models.Stations.AllStations;
import com.example.elevator_app.Models.Stations.Station;
import com.example.elevator_app.R;

public class SpecificLineActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_line);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        buildStationViews();

    }

    private void buildStationViews(){
        LinearLayout stationLayout = findViewById(R.id.linear_line_stations);
        stationLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        String currentLine = getIntent().getStringExtra("CurrentLine");
        TextView title = findViewById(R.id.text_line_header);
        title.setText(currentLine);
        String[] currLineStations = getIntent().getStringArrayExtra("LineStations");

        AllStations allStations = (AllStations) getIntent().getSerializableExtra("allStations");
        for (String str : currLineStations)
        {
            try{
                View myLayout = inflater.inflate(R.layout.specific_line_station, stationLayout, false);
                TextView stationView = myLayout.findViewById(R.id.text_line_station);
                //ImageView statusView = myLayout.findViewById(R.id.image_elev_status);

                final Station s = allStations.getStation(str);
                final boolean hasElev = s.getElevator();
                final boolean elevWorking = s.getAlerts().isEmpty();

                stationView.setText(s.getName());
                //statusView.setImageResource(status_red);

                myLayout.setOnClickListener(v -> {
                    Intent intent1 = new Intent(SpecificLineActivity.this, DisplayAlertActivity.class);
                    intent1.putExtra("Station", s);
                    //If there is no elevator or it works, add different text
                    if (!hasElev) intent1.putExtra("Text", "No elevator present at station.");
                    else if (elevWorking) intent1.putExtra("Text", "Elevator is present and working at station.");
                    startActivity(intent1);
                });
                stationLayout.addView(myLayout);

            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
