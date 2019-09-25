package com.github.cta_elevator_alerts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
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

        TextView contactEmail = findViewById(R.id.txt_contact_email);
        contactEmail.setMovementMethod((LinkMovementMethod.getInstance()));

        ImageView about = findViewById(R.id.img_home_icon);
        about.setVisibility(View.INVISIBLE);
    }

    public void toMainActivity(View v) {
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public void onBackPressed(View v){ this.onBackPressed(); }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        Intent intent = new Intent(AboutActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
