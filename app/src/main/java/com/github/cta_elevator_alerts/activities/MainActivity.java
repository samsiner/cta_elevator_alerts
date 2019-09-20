package com.github.cta_elevator_alerts.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.StationListAdapter;
import com.github.cta_elevator_alerts.utils.NetworkWorker;
import com.github.cta_elevator_alerts.utils.NotificationPusher;
import com.github.cta_elevator_alerts.viewmodels.MainViewModel;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity displays favorite stations and their
 * elevator status, all current alerts,
 * the most updated date/time, and the privacy policy.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class MainActivity extends AppCompatActivity {

    //TODO: For later: Check on simplifying layouts, use executor instead of threads in repository

    private MainViewModel vm;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private StationListAdapter favoritesAdapter, alertsAdapter;
    private SharedPreferences sharedPreferences;
    private TextView tv_alertsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ImageView about = findViewById(R.id.img_home_icon);
        about.setImageResource(R.drawable.icon_info);
        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toAboutActivity(v);
            }
        });

        ImageView backArrow = findViewById(R.id.img_back_arrow);
        backArrow.setVisibility(View.INVISIBLE);

        vm = ViewModelProviders.of(this).get(MainViewModel.class);

        //Create adapter to display favorites
        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        favoritesAdapter = new StationListAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create adapter to display alerts
        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        alertsAdapter = new StationListAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create SharedPreferences for last updated date/time
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tv_alertsTime = findViewById(R.id.txt_update_alert_time);
        String time = sharedPreferences.getString("LastUpdatedTime", "");
        if (time != null && !time.equals("")) tv_alertsTime.setText(time);

        //TODO: Testing (remove before deploy)
        vm.addFavoriteKing();
        vm.addFavoriteKimball();
        addTestButtons();

        addSwipeRefresh();
        addAlertsObserver();
        addFavoritesObserver();
        addLastUpdatedObserver();
        addConnectionStatusObserver();
        addPeriodicWorker();
    }

    @Override
    protected void onResume(){
        super.onResume();
        addOneTimeWorker();
    }

    private void addSwipeRefresh(){
        mSwipeRefreshLayout = findViewById(R.id.swipe_main_activity);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            addOneTimeWorker();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void addAlertsObserver(){
        vm.getStationAlerts().observe(this, alerts -> {
                alertsAdapter.updateStationList(alerts);

                //If no alerts
                TextView tv = findViewById(R.id.noStationAlerts);
                if (alerts.size() < 1) {
                    tv.setVisibility(View.VISIBLE);
                } else {
                    tv.setVisibility(View.GONE);
                }
            }
        );
    }

    private void addFavoritesObserver(){
        vm.getFavorites().observe(this, stations -> {
            favoritesAdapter.updateStationList(stations);

            //If no favorites
            TextView tv = findViewById(R.id.noFavoritesAdded);
            if (stations.size() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });
    }

    private void addLastUpdatedObserver(){
        vm.getUpdateAlertsTime().observe(this, text -> {
            tv_alertsTime.setText(text);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("LastUpdatedTime", text);
            editor.apply();
        });
    }

    private void addConnectionStatusObserver() {
        vm.getConnectionStatus().observe(this, isConnected -> {
            if (!isConnected) {
                Toast toast = Toast.makeText(this, "Not connected - please refresh!", Toast.LENGTH_SHORT);
                toast.show();
            }
            vm.setConnectionStatus(true);
        });
    }

    private void addPeriodicWorker(){
        PeriodicWorkRequest request = new PeriodicWorkRequest.Builder(NetworkWorker.class, 1, TimeUnit.HOURS)
                .addTag("PeriodicWork")
                .setConstraints(new Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("PeriodicWork", ExistingPeriodicWorkPolicy.KEEP, request);
    }

    private void addOneTimeWorker(){
        OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NetworkWorker.class)
                .addTag("OneTimeWork")
                .setConstraints(new Constraints.Builder()
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueue(request);
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(MainActivity.this, AllLinesActivity.class);
        startActivity(intent);
    }

    public void toAboutActivity(View v){
        Intent intent = new Intent(MainActivity.this, AboutActivity.class);
        startActivity(intent);
    }

    public MainViewModel getStationAlertsViewModel(){ return vm; }

    private void addTestButtons(){
        Button b = new Button(this);
        b.setText("Remove alert King");
        LinearLayout l = findViewById(R.id.LinearLayout);
        b.setOnClickListener(v -> {
            ArrayList<String> past = (ArrayList<String>) vm.mGetStationAlertIDs();
            vm.removeAlertKing();
            ArrayList<String> curr = (ArrayList<String>) vm.mGetStationAlertIDs();
            NotificationPusher.createAlertNotifications(this, past, curr);
        });
        l.addView(b);

        Button b1 = new Button(this);
        b1.setText("Add alert Howard");
        b1.setOnClickListener(v -> {
            ArrayList<String> past = (ArrayList<String>) vm.mGetStationAlertIDs();
            vm.addAlertHoward();
            ArrayList<String> curr = (ArrayList<String>) vm.mGetStationAlertIDs();
            NotificationPusher.createAlertNotifications(this, past, curr);
        });
        l.addView(b1);
    }
}