package com.github.cta_elevator_alerts.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.viewmodels.DisplayAlertViewModel;

/**
 * DisplayAlertActivity shows the details of a
 * specific elevator outage alert
 * (description and date/time it began).
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class DisplayAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        TextView tv_shortDesc = findViewById(R.id.txt_alert_shortDesc);
        TextView toolbarText = findViewById(R.id.txt_toolbar);

        String stationID = getIntent().getStringExtra("stationID");

        //Get ViewModel
        DisplayAlertViewModel mDisplayAlertViewModel = ViewModelProviders.of(this).get(DisplayAlertViewModel.class);
        mDisplayAlertViewModel.setStationID(stationID);

        toolbarText.setText(mDisplayAlertViewModel.getStationName()); //Set Station Name

        //Set alert description
        if (!mDisplayAlertViewModel.getHasElevator()) tv_shortDesc.setText(R.string.no_elevator);
        else if (!mDisplayAlertViewModel.getHasAlert())  tv_shortDesc.setText(R.string.present_elevator);
        else tv_shortDesc.setText(mDisplayAlertViewModel.getShortDesc());
    }

    public void toMainActivity(View v) {
        Intent intent = new Intent(DisplayAlertActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View v){ this.onBackPressed(); }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(getIntent().getBooleanExtra("fromMain", false)){
            Intent intent = new Intent(DisplayAlertActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
