package com.github.cta_elevator_alerts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.SpecificLineAdapter;
import com.github.cta_elevator_alerts.adapters.SpecificLineAlertsAdapter;
import com.github.cta_elevator_alerts.viewmodels.SpecificLineViewModel;

import java.util.List;

/**
 * SpecificLineActivity displays all current elevator
 * outages at the top, then all stations in the line,
 * in order, with their accessibility status
 * and any elevator outages.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class SpecificLineActivity extends AppCompatActivity {

    private SpecificLineViewModel mSpecificLineViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_line);
        mSpecificLineViewModel = ViewModelProviders.of(this).get(SpecificLineViewModel.class);
        String line = getIntent().getStringExtra("line");
        mSpecificLineViewModel.setLine(line);
        List<String> lineAlertIDs = mSpecificLineViewModel.getAllLineAlerts();

        if(lineAlertIDs.size() > 0 && !getIntent().getBooleanExtra("fromFavorites", false)){
            RecyclerView lineAlertsRecyclerView = findViewById(R.id.recycler_specific_line_alert_stations);
            final SpecificLineAlertsAdapter lineAlertsAdapter = new SpecificLineAlertsAdapter(this, lineAlertIDs);
            lineAlertsRecyclerView.setAdapter(lineAlertsAdapter);
            lineAlertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else{
            ViewGroup alerts = (ViewGroup)findViewById(R.id.tv_elevator_alerts).getParent();
            alerts.removeView(findViewById(R.id.tv_all_stations));
            alerts.removeView(findViewById(R.id.recycler_specific_line_alert_stations));
            alerts.removeView(findViewById(R.id.tv_elevator_alerts));
        }

        RecyclerView specificLineRecyclerView = findViewById(R.id.recycler_specific_line);
        final SpecificLineAdapter specificLineAdapter = new SpecificLineAdapter(this, mSpecificLineViewModel.getLine());
        specificLineAdapter.setToolbar(getIntent().getStringExtra("line"));
        specificLineRecyclerView.setAdapter(specificLineAdapter);
        specificLineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void toMainActivity(View v) {
        Intent intent = new Intent(SpecificLineActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public String getStationName(String stationID){
        return mSpecificLineViewModel.getStationName(stationID);
    }

    public Boolean getHasElevator(String stationID){
        return mSpecificLineViewModel.getHasElevator(stationID);
    }

    public Boolean getIsFavorite(String stationID){
        return mSpecificLineViewModel.getIsFavorite(stationID);
    }

    public Boolean getHasElevatorAlert(String stationID){
        return mSpecificLineViewModel.getHasElevatorAlert(stationID);
    }

    public void onBackPressed(View v){
        finish();
    }
}