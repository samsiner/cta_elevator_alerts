package com.github.cta_elevator_alerts.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.AllLinesAdapter;
import com.github.cta_elevator_alerts.viewmodels.AllLinesViewModel;

/**
 * AllLinesActivity displays all CTA L lines
 * so user can click on them to go to a specific line.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

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

        ImageView about = findViewById(R.id.img_home_icon);
        about.setVisibility(View.INVISIBLE);
    }

    public void toMainActivity(View v) {
        Intent intent = new Intent(AllLinesActivity.this, MainActivity.class);
        startActivity(intent);
    }

    public AllLinesViewModel getAllLinesViewModel(){ return mAllLinesViewModel; }

    public void onBackPressed(View v){ this.onBackPressed(); }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        if(!getIntent().getBooleanExtra("fromFavorites", false)){
            Intent intent = new Intent(AllLinesActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }
}
