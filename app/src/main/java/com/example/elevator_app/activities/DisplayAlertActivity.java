package com.example.elevator_app.activities;

import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.widget.TextView;

import com.example.elevator_app.R;
import com.example.elevator_app.models.database.DisplayAlertViewModel;
import com.example.elevator_app.models.database.DisplayAlertViewModelFactory;

public class DisplayAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);

        String stationID = getIntent().getStringExtra("stationID");

        //Get ViewModel
        DisplayAlertViewModel mDisplayAlertViewModel = ViewModelProviders.of(this, new DisplayAlertViewModelFactory(this.getApplication(), stationID)).get(DisplayAlertViewModel.class);
        String headline = mDisplayAlertViewModel.getHeadline();
        String shortDesc = mDisplayAlertViewModel.getShortDesc();
        String beginDateTime = mDisplayAlertViewModel.getBeginDateTime();
        String stationName = mDisplayAlertViewModel.getStationName();

        TextView display = findViewById(R.id.txt_display_alert);
        display.setTextSize(15);
        display.setTextColor(Color.BLACK);
        display.append(stationName + "\n\n");
        display.append("Starting " + beginDateTime + "\n\n");
        display.append(headline + "\n\n");
        display.append(shortDesc + "\n\n");
    }
}
