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

    private SpecificLineViewModel vm;
    private SpecificLineAlertsAdapter lineAlertsAdapter;
    private SpecificLineAdapter specificLineAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_line);
        vm = ViewModelProviders.of(this).get(SpecificLineViewModel.class);
        String line = getIntent().getStringExtra("line");
        vm.setLine(line);

        addFavoritesObserver();

        List<String> lineAlertIDs = vm.getAllLineAlerts();
        lineAlertsAdapter = new SpecificLineAlertsAdapter(this, lineAlertIDs);
        specificLineAdapter = new SpecificLineAdapter(this, vm.getLine());

        if(lineAlertIDs.size() > 0){
            RecyclerView lineAlertsRecyclerView = findViewById(R.id.recycler_specific_line_alert_stations);
            lineAlertsRecyclerView.setAdapter(lineAlertsAdapter);
            lineAlertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            ViewGroup alerts = (ViewGroup)findViewById(R.id.tv_elevator_alerts).getParent();
            alerts.removeView(findViewById(R.id.tv_all_stations));
            alerts.removeView(findViewById(R.id.recycler_specific_line_alert_stations));
            alerts.removeView(findViewById(R.id.tv_elevator_alerts));
        }

        RecyclerView specificLineRecyclerView = findViewById(R.id.recycler_specific_line);
        specificLineAdapter.setToolbar(getIntent().getStringExtra("line"));
        specificLineRecyclerView.setAdapter(specificLineAdapter);
        specificLineRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void addFavoritesObserver(){
        vm.getFavorites().observe(this, stations -> {
            lineAlertsAdapter.notifyDataSetChanged();
            specificLineAdapter.notifyDataSetChanged();
        });
    }

    public void toMainActivity(View v) {
        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        finish();
        startActivity(i);
    }

    public String getStationName(String stationID){
        return vm.getStationName(stationID);
    }

    public boolean getHasElevator(String stationID){
        return vm.getHasElevator(stationID);
    }

    public boolean getIsFavorite(String stationID){
        return vm.getIsFavorite(stationID);
    }

    public boolean getHasElevatorAlert(String stationID){
        return vm.getHasElevatorAlert(stationID);
    }

    public void onBackPressed(View v){
        finish();
    }
}