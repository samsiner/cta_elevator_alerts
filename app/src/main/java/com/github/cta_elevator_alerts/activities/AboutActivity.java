package com.github.cta_elevator_alerts.activities;

import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

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

        TextView t2 = findViewById(R.id.txt_privacy);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void onBackPressed(View v){
        finish();
    }


}
