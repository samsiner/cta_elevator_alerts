package com.example.elevator_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.util.Log;
import android.widget.TextView;

import com.example.elevator_app.R;
import com.example.elevator_app.viewmodels.DisplayAlertViewModel;
import com.example.elevator_app.viewmodelfactories.DisplayAlertViewModelFactory;

public class DisplayAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        TextView toolbarText = findViewById(R.id.txt_toolbar);

        String stationID = getIntent().getStringExtra("stationID");

        //Get ViewModel
        DisplayAlertViewModel mDisplayAlertViewModel = ViewModelProviders.of(this, new DisplayAlertViewModelFactory(this.getApplication(), stationID)).get(DisplayAlertViewModel.class);
        String headline = mDisplayAlertViewModel.getHeadline();
        String shortDesc = mDisplayAlertViewModel.getShortDesc();
        String beginDateTime = mDisplayAlertViewModel.getBeginDateTime();
        String stationName = mDisplayAlertViewModel.getStationName();

        toolbarText.setText(stationName);
        TextView display = findViewById(R.id.txt_display_alert);
        display.setTextSize(15);
        display.setTextColor(Color.BLACK);
        display.append(stationName + "\n\n");
        if (!beginDateTime.equals("")){
            display.append("Starting ");
            display.append(beginDateTime + "\n\n");
            display.append(headline + "\n\n");
            display.append(shortDesc + "\n\n");
        } else{
            boolean hasElevator = mDisplayAlertViewModel.getHasElevator();
            if (hasElevator) {
                display.append("Elevator is present at this station and working properly.");
            } else{
                display.append("No elevator is present at this station.");
            }
        }

    }
}
