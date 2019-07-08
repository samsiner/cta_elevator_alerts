package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elevator_app.models.stations.AllStations;
import com.example.elevator_app.models.stations.Station;
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
        for (String stationID : currLineStations)
        {
            try{
                View myLayout = inflater.inflate(R.layout.specific_line_station, stationLayout, false);
                TextView stationView = myLayout.findViewById(R.id.text_line_station);
                //ImageView statusView = myLayout.findViewById(R.id.image_elev_status);

                final Station s = allStations.getStation(stationID);

                stationView.setText(s.getName());
                //statusView.setImageResource(status_red);

                myLayout.setOnClickListener(v -> {
                    Intent intent;
                    if(getIntent().getBooleanExtra("fromFavorites", false)){
                        intent = new Intent(SpecificLineActivity.this, AddFavoriteActivity.class);
                        intent.putExtra("stationID", stationID);
                        intent.putExtra("allStations", allStations);
                    } else{
                        intent = new Intent(SpecificLineActivity.this, DisplayAlertActivity.class);
                        intent.putExtra("Station", s);
                        //If there is no elevator or it works, add different text
                        if (!s.getElevator()) intent.putExtra("Text", "No elevator present at station.");
                        else if (s.getAlerts().isEmpty()) intent.putExtra("Text", "Elevator is present and working at station.");
                    }
                    startActivity(intent);
                });
                stationLayout.addView(myLayout);

            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
