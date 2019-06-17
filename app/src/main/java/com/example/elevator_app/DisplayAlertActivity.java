package com.example.elevator_app;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

public class DisplayAlertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_alert);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        Station s = (Station) intent.getSerializableExtra("Station");

        TextView display = findViewById(R.id.displayAlert);
        display.setTextSize(15);
        display.setTextColor(Color.BLACK);
        display.append(s.getName() + "\n\n");
        for (ElevatorAlert alert : s.getAlerts()){
            display.append("Starting " + alert.getBeginDateTime() + "\n\n");
            display.append(alert.getHeadline() + "\n\n");
            display.append(Html.fromHtml(alert.getFullDesc() + "\n\n"));
            display.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

}
