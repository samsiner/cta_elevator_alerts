package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.elevator_app.adapters.FavoritesAdapter;
import com.example.elevator_app.model.APIWorker;
import com.example.elevator_app.viewmodels.FavoritesViewModel;
import com.example.elevator_app.adapters.StationAlertsAdapter;
import com.example.elevator_app.viewmodels.StationAlertsViewModel;
import com.example.elevator_app.R;
import com.example.elevator_app.model.Station;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //TODO: Figure out what an adapter is. Do we need to create one?
    //TODO: Fragments?
    //TODO: Proguard?
    //TODO: Instabug?
    //TODO: Refresh?
    //TODO: Make error catching more specific - or throw instead of catch?
    //TODO: Persist alertsout within SharedPrefs (in case of no internet)
    //TODO: Database for stations

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        final StationAlertsAdapter alertsAdapter = new StationAlertsAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Get ViewModel
        mStationAlertsViewModel = ViewModelProviders.of(this).get(StationAlertsViewModel.class);
        mStationAlertsViewModel.getStationAlerts().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(List<Station> stations) {
                alertsAdapter.setStations(stations);
            }
        });

        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        mFavoritesViewModel.getFavorites().observe(this, new Observer<List<Station>>() {
            @Override
            public void onChanged(List<Station> stations) {
                favoritesAdapter.setFavorites(stations);
            }
        });

        //Build Alerts API work request
        PeriodicWorkRequest apiAlertsWorkRequest = new PeriodicWorkRequest.Builder(APIWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqueAPIAlertsWork", ExistingPeriodicWorkPolicy.REPLACE, apiAlertsWorkRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(apiAlertsWorkRequest.getId())
                .observe(this, info -> {
                    if (info != null && info.getState() == WorkInfo.State.ENQUEUED) {
                        Log.d("B", "Building Alerts");
                        buildAlerts();
                    }
                });
    }
    public void buildAlerts(){
        Log.d("Building Alerts Finally", "Building Alerts Finally");
        final StringBuilder sb = new StringBuilder();

        Thread thread = new Thread() {
            public void run() {
                try {
                    URL url = new URL("https://lapi.transitchicago.com/api/1.0/alerts.aspx?outputType=JSON");
                    Scanner scan = new Scanner(url.openStream());
                    while (scan.hasNext()) sb.append(scan.nextLine());
                    scan.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    sb.append("");
                }
            }
        };

        thread.start();
        try{
            thread.join();
        } catch (InterruptedException e){
            e.printStackTrace();
        }
        mStationAlertsViewModel.putAlertsIntoDatabase(sb.toString());
    }

//    private void dialogPositiveButton(String title, String message){
//        AlertDialog.Builder alert = new AlertDialog.Builder(this);
//        alert.setTitle(title);
//        alert.setMessage(message);
//        alert.setPositiveButton("OK", null);
//        alert.show();
//    }

    public void toAddFavoriteActivity(View v){
        Intent intent = new Intent(MainActivity.this, AddFavoriteActivity.class);
        startActivity(intent);
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(MainActivity.this, AllLinesActivity.class);
        startActivity(intent);
        intent.putExtra("fromFavorites", false);
    }
//
////    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
////        savedInstanceState.putSerializable("allStations", allStations);
////        savedInstanceState.putSerializable("allAlerts", allAlerts);
////    }
//    }

}