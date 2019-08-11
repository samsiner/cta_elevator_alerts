package com.github.cta_elevator_alerts.activities;

import android.os.Bundle;
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
}
