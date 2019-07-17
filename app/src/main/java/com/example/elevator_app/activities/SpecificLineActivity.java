package com.example.elevator_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.adapters.SpecificLineAdapter;
import com.example.elevator_app.model.Station;
import com.example.elevator_app.viewmodelfactories.SpecificLineViewModelFactory;
import com.example.elevator_app.viewmodels.SpecificLineViewModel;

import java.util.List;

public class SpecificLineActivity extends AppCompatActivity {

    SpecificLineViewModel mSpecificLineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_line);
        mSpecificLineViewModel = ViewModelProviders.of(this, new SpecificLineViewModelFactory(this.getApplication(), getIntent().getStringExtra("line"))).get(SpecificLineViewModel.class);

        RecyclerView specificLineRecyclerView = findViewById(R.id.recycler_specific_line);
        final SpecificLineAdapter specificLineAdapter = new SpecificLineAdapter(this, mSpecificLineViewModel.getLine());
        specificLineAdapter.setToolbar(getIntent().getStringExtra("line"));
        specificLineRecyclerView.setAdapter(specificLineAdapter);
        specificLineRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //buildStationViews();

    }

    public String getStationName(String stationID){
        return mSpecificLineViewModel.getStationName(stationID);
    }

    public Boolean getHasElevator(String stationID){
        return mSpecificLineViewModel.getHasElevator(stationID);
    }

    public Boolean getHasElevatorAlert(String stationID){
        return mSpecificLineViewModel.getHasElevatorAlert(stationID);
    }
//
//    private void buildStationViews(){
//        LinearLayout stationLayout = findViewById(R.id.linear_line_stations);
//        stationLayout.removeAllViews();
//        LayoutInflater inflater = getLayoutInflater();
//
//        String currentLine = getIntent().getStringExtra("CurrentLine");
//        TextView title = findViewById(R.id.text_line_header);
//        title.setText(currentLine);
//        String[] currLineStations = getIntent().getStringArrayExtra("LineStations");
//
//        AllStations allStations = (AllStations) getIntent().getSerializableExtra("allStations");
//        for (String stationID : currLineStations)
//        {
//            try{
//                View myLayout = inflater.inflate(R.layout.specific_line_station, stationLayout, false);
//                TextView stationView = myLayout.findViewById(R.id.text_line_station);
//                //ImageView statusView = myLayout.findViewById(R.id.image_elev_status);
//
//                final Station s = allStations.getStation(stationID);
//
//                //stationView.setText(s.getName());
//                //statusView.setImageResource(status_red);
//
//                myLayout.setOnClickListener(v -> {
//                    Intent intent;
//                    if(getIntent().getBooleanExtra("fromFavorites", false)){
//                        intent = new Intent(SpecificLineActivity.this, AddFavoriteActivity.class);
//                        intent.putExtra("stationID", stationID);
//                        intent.putExtra("allStations", allStations);
//                    } else{
//                        intent = new Intent(SpecificLineActivity.this, DisplayAlertActivity.class);
//                        //intent.putExtra("Station", s);
//                        //If there is no elevator or it works, add different text
//                        //if (!s.getElevator()) intent.putExtra("Text", "No elevator present at station.");
//                        //else if (s.getAlerts().isEmpty()) intent.putExtra("Text", "Elevator is present and working at station.");
//                    }
//                    startActivity(intent);
//                });
//                stationLayout.addView(myLayout);
//
//            } catch (NullPointerException e){
//                e.printStackTrace();
//            }
//        }
//    }
}
