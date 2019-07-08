package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elevator_app.models.lines.AllLines;
import com.example.elevator_app.R;

public class AllLinesActivity extends AppCompatActivity {

    private AllLines allLines;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lines);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        allLines = new AllLines();

        LinearLayout lineLayout = findViewById(R.id.linear_display_lines);
        lineLayout.removeAllViews();
        LayoutInflater inflater = getLayoutInflater();

        String[] allLineNames = new String[]{"Red Line", "Blue Line", "Brown Line", "Green Line", "Orange Line", "Pink Line", "Purple Line", "Yellow Line"};

        for (String str : allLineNames)
        {
            try{
                View myLayout = inflater.inflate(R.layout.alert_station, lineLayout, false);
                TextView lineView = myLayout.findViewById(R.id.text_favorite_station);
                //Add line image
                //ImageView status = myLayout.findViewById(R.id.image_elev_status);
                lineView.setText(str);

                myLayout.setOnClickListener(v -> {
                    Intent intent1 = new Intent(AllLinesActivity.this, SpecificLineActivity.class);
                    intent1.putExtra("CurrentLine", str);
                    intent1.putExtra("LineStations", allLines.getLine(str));
                    intent1.putExtra("allStations", getIntent().getSerializableExtra("allStations"));
                    intent1.putExtra("fromFavorites", getIntent().getBooleanExtra("fromFavorites", false));
                    startActivity(intent1);
                });

                lineLayout.addView(myLayout);
            } catch (NullPointerException e){
                e.printStackTrace();
            }
        }
    }
}
