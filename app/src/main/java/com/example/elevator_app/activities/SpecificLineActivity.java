package com.example.elevator_app.activities;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.R;
import com.example.elevator_app.adapters.SpecificLineAdapter;
import com.example.elevator_app.viewmodelfactories.SpecificLineViewModelFactory;
import com.example.elevator_app.viewmodels.SpecificLineViewModel;

public class SpecificLineActivity extends AppCompatActivity {

    private SpecificLineViewModel mSpecificLineViewModel;

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
}
