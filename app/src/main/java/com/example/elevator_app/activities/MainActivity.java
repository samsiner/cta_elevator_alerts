package com.example.elevator_app.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.elevator_app.adapters.FavoritesAdapter;
import com.example.elevator_app.adapters.SwipeController;
import com.example.elevator_app.model.APIWorker;
import com.example.elevator_app.viewmodels.FavoritesViewModel;
import com.example.elevator_app.adapters.StationAlertsAdapter;
import com.example.elevator_app.viewmodels.StationAlertsViewModel;
import com.example.elevator_app.R;

import java.io.IOException;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //TODO: Proguard?
    //TODO: Instabug?
    //TODO: Refresh?
    //TODO: Make error catching more specific - or throw instead of catch?
    //TODO: Test for no network availability
    //TODO: Notifications?
    //TODO: Display last updated time for elevator alerts
    //TODO: right facing arrow next to each station on specificLine
    //TODO: TESTS
    //TODO: Edit favorite functionality
    //TODO: Show trash can when swiping to remove favorite
    //TODO: Keep nickname on add favorites
    //TODO: Not rebuild database every time you open

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        final StationAlertsAdapter alertsAdapter = new StationAlertsAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);

        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeController swipeController = new SwipeController(favoritesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(favoritesRecyclerView);

        //Get ViewModel
        //TODO: figure out why this is causing problems in StationAlertsAdapter
        mStationAlertsViewModel = ViewModelProviders.of(this).get(StationAlertsViewModel.class);
        mStationAlertsViewModel.getStationAlerts().observe(this, alertsAdapter::setStations);

        mFavoritesViewModel.getFavorites().observe(this, stations -> {
            LinearLayout rl = findViewById(R.id.LinearLayout);
            rl.removeView(findViewById(R.id.noFavoritesAdded));
            favoritesAdapter.setFavorites(stations);

            //If no favorites
            if (mFavoritesViewModel.getNumFavorites() < 1) {
                rl = findViewById(R.id.LinearLayout);
                TextView tv = new TextView(MainActivity.this);
                tv.setId(R.id.noFavoritesAdded);
                tv.setText("No favorites added!");
                tv.setTextSize(18);
                tv.setTextColor(MainActivity.this.getResources().getColor(R.color.colorWhite));
                tv.setHeight(100);
                tv.setWidth(100);
                tv.setGravity(Gravity.CENTER);
                tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
                RelativeLayout tv2 = findViewById(R.id.relative_layout_main);
                int index = rl.indexOfChild(tv2);
                rl.addView(tv, index + 1);
            }
        });

        if (getIntent().getStringExtra("Nickname") != null){
            String nickname = getIntent().getStringExtra("Nickname");
            String stationID = getIntent().getStringExtra("stationID");
            mFavoritesViewModel.addFavorite(stationID, nickname);
        }

        buildAlerts();

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
                        buildAlerts();
                    }
                });

        //If no alerts
        if (mStationAlertsViewModel.getNumAlerts() < 1) {
            LinearLayout rl = this.findViewById(R.id.LinearLayout);
            TextView tv = new TextView(this);
            tv.setText("No current elevator alerts!");
            tv.setTextSize(18);
            tv.setTextColor(this.getResources().getColor(R.color.colorWhite));
            tv.setHeight(100);
            tv.setWidth(100);
            tv.setGravity(Gravity.CENTER);
            tv.setTypeface(tv.getTypeface(), Typeface.ITALIC);
            TextView tv2 = this.findViewById(R.id.text_tempDown_header);
            int index = rl.indexOfChild(tv2);
            rl.addView(tv, index + 1);
        }
    }

    private void buildAlerts(){
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

    public FavoritesViewModel getFavoritesViewModel(){ return mFavoritesViewModel; }
//
////    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        super.onSaveInstanceState(savedInstanceState);
////        savedInstanceState.putSerializable("allStations", allStations);
////        savedInstanceState.putSerializable("allAlerts", allAlerts);
////    }
//    }
}