package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.elevator_app.models.database.AllLinesAdapter;
import com.example.elevator_app.models.database.StationAlertsAdapter;
import com.example.elevator_app.models.lines.AllLines;
import com.example.elevator_app.R;

public class AllLinesActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lines);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView linesRecyclerView = findViewById(R.id.recycler_all_lines);
        final AllLinesAdapter linesAdapter = new AllLinesAdapter(this);
        linesRecyclerView.setAdapter(linesAdapter);
        linesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
