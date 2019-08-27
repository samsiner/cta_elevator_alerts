package com.github.cta_elevator_alerts.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.github.cta_elevator_alerts.R;

/**
 * AboutActivity shows information about the app.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class AboutActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public void onBackPressed(View v){
        finish();
    }
}
