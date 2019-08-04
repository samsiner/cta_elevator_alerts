package com.github.cta_elevator_alerts.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.AllLinesAdapter;
import com.github.cta_elevator_alerts.viewmodels.AllLinesViewModel;

public class AllLinesActivity extends AppCompatActivity {
    private AllLinesViewModel mAllLinesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_lines);

        mAllLinesViewModel = ViewModelProviders.of(this).get(AllLinesViewModel.class);

        RecyclerView linesRecyclerView = findViewById(R.id.recycler_all_lines);
        final AllLinesAdapter linesAdapter = new AllLinesAdapter(this);
        linesAdapter.setToolbarTextView();
        linesRecyclerView.setAdapter(linesAdapter);
        linesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public AllLinesViewModel getAllLinesViewModel(){ return mAllLinesViewModel; }
}
