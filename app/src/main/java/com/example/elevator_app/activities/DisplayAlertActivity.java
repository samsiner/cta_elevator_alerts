package com.example.elevator_app.activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.widget.TextView;

import com.example.elevator_app.models.alerts.ElevatorAlert;
import com.example.elevator_app.R;
import com.example.elevator_app.models.database.Station;

public class DisplayAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        Toolbar toolbar = findViewById(R.id.toolbar_old);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Station s = (Station) intent.getSerializableExtra("Station");
        String str = intent.getStringExtra("Text");

        TextView display = findViewById(R.id.displayAlert);
        display.setTextSize(15);
        display.setTextColor(Color.BLACK);
        //display.append(s.getName() + "\n\n");

//        if (str != null) display.append(str);
//        else {
//            for (ElevatorAlert alert : s.getAlerts()) {
//                display.append("Starting " + alert.getBeginDateTime() + "\n\n");
//                display.append(alert.getHeadline() + "\n\n");
//                display.append(alert.getShortDesc() + "\n\n");
//            }
//        }
    }
}
