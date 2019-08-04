package com.github.cta_elevator_alerts.activities;

import android.os.Bundle;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.SpecificLineAdapter;
import com.github.cta_elevator_alerts.adapters.SpecificLineAlertsAdapter;
import com.github.cta_elevator_alerts.viewmodelfactories.SpecificLineViewModelFactory;
import com.github.cta_elevator_alerts.viewmodels.AllLinesViewModel;
import com.github.cta_elevator_alerts.viewmodels.SpecificLineViewModel;

import java.util.List;

public class SpecificLineActivity extends AppCompatActivity {

    private SpecificLineViewModel mSpecificLineViewModel;
    private AllLinesViewModel mAllLinesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_line);
        mSpecificLineViewModel = ViewModelProviders.of(this, new SpecificLineViewModelFactory(this.getApplication(), getIntent().getStringExtra("line"))).get(SpecificLineViewModel.class);
        List<String> lineAlertIDs = mSpecificLineViewModel.getAllLineAlerts();

        if(lineAlertIDs.size() > 0){
            RecyclerView lineAlertsRecyclerView = findViewById(R.id.recycler_specific_line_alert_stations);
            final SpecificLineAlertsAdapter lineAlertsAdapter = new SpecificLineAlertsAdapter(this, lineAlertIDs);
            lineAlertsRecyclerView.setAdapter(lineAlertsAdapter);
            lineAlertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else{
            NestedScrollView svLineAlerts = findViewById(R.id.sv_specific_alert_stations);
            ((ViewGroup)svLineAlerts.getParent()).removeView(svLineAlerts);
        }

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
