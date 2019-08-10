package com.github.cta_elevator_alerts.activities;

import android.graphics.Typeface;
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
        TextView tv_beginDateTime = findViewById(R.id.txt_alert_date_time);
        TextView tv_shortDesc = findViewById(R.id.txt_alert_shortDesc);
        TextView toolbarText = findViewById(R.id.txt_toolbar);

        String stationID = getIntent().getStringExtra("stationID");

        //Get ViewModel
        DisplayAlertViewModel mDisplayAlertViewModel = ViewModelProviders.of(this).get(DisplayAlertViewModel.class);
        mDisplayAlertViewModel.setStationID(stationID);
        String shortDesc = mDisplayAlertViewModel.getShortDesc();
        String beginDateTime = mDisplayAlertViewModel.getBeginDateTime();
        String stationName = mDisplayAlertViewModel.getStationName();

        toolbarText.setText(stationName);

        if (!beginDateTime.equals("")){
            tv_beginDateTime.setText(beginDateTime);
            tv_shortDesc.setText(shortDesc);
        } else{
            tv_beginDateTime.setBackgroundResource(0);
            tv_beginDateTime.setTypeface(null, Typeface.NORMAL);
            boolean hasElevator = mDisplayAlertViewModel.getHasElevator();
            if(hasElevator){
                tv_beginDateTime.setText(R.string.present_elevator);
            } else{
                tv_beginDateTime.setText(R.string.no_elevator);
            }
        }
    }
}
