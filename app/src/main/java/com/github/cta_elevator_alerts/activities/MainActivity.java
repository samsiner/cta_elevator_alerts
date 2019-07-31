package com.github.cta_elevator_alerts.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.adapters.FavoritesAdapter;
import com.github.cta_elevator_alerts.adapters.SwipeController;
import com.github.cta_elevator_alerts.model.APIWorker;
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;
import com.github.cta_elevator_alerts.adapters.StationAlertsAdapter;
import com.github.cta_elevator_alerts.viewmodels.StationAlertsViewModel;
import com.github.cta_elevator_alerts.R;

import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Sam:
    //TODO: Network availability: https://developer.android.com/training/monitoring-device-state/connectivity-monitoring
    //TODO: More tests

    //Tyler:
    //TODO: Edit / Remove favorite functionality: https://www.learn2crack.com/2016/02/custom-swipe-recyclerview.html
    //TODO: Navigation - tabs? (FragmentPagerAdapter?)
    //TODO: Figure out alternative to Toolbar that can keep our minAPI lower than 21

    //To do before deployment:
    //TODO: Reduce app size: https://developer.android.com/studio/build/shrink-code
    //TODO: improve UI performance

    //Possible future features:
    //Firebase - cloud database

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;
    private NotificationCompat.Builder builder;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String updateAlertsTime;
    private TextView tv_alertsTime;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_alertsTime = findViewById(R.id.txt_update_alert_time);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        tv_alertsTime.setText(sharedPref.getString("Updated Time", "No time found"));

        buildNotification();

        //Create ViewModels for favorites and alerts
        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        mStationAlertsViewModel = ViewModelProviders.of(this).get(StationAlertsViewModel.class);
        tv_alertsTime.setText(updateAlertsTime);

        mSwipeRefreshLayout = findViewById(R.id.swipe_main_activity);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            if (isConnectedToInternet()) updateAlertsTime = mStationAlertsViewModel.rebuildAlerts();
            if (updateAlertsTime.equals("") || !isConnectedToInternet()) showNoInternetPositiveDialog();
            tv_alertsTime.setText(updateAlertsTime);
            mSwipeRefreshLayout.setRefreshing(false);
        });

        String buildStationResult = mStationAlertsViewModel.buildStations();
        if (buildStationResult.equals("Internet issue") || !isConnectedToInternet()){
            showNoInternetPositiveDialog();
            return;
        }

        //Create recyclerviews to display favorites and alerts
        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        final StationAlertsAdapter alertsAdapter = new StationAlertsAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        SwipeController swipeController = new SwipeController(favoritesAdapter);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(swipeController);
        itemTouchHelper.attachToRecyclerView(favoritesRecyclerView);

        mStationAlertsViewModel.getStationAlerts().observe(this, stations1 -> {
            alertsAdapter.notifyDataSetChanged();

//            //Display notification if elevator is newly out
//            Log.d("Newly Out", mStationAlertsViewModel.getStationElevatorsNewlyOut().toString());
//            Log.d("Newly Working", mStationAlertsViewModel.getStationElevatorsNewlyWorking().toString());
            if (mStationAlertsViewModel.getStationElevatorsNewlyOut() != null){
                for (String s : mStationAlertsViewModel.getStationElevatorsNewlyOut()) {
                    showNotification(Integer.parseInt(s), true);
                }
            }
            //Display notification if elevator is newly working
            if (mStationAlertsViewModel.getStationElevatorsNewlyWorking() != null) {

                for (String s : mStationAlertsViewModel.getStationElevatorsNewlyWorking()) {
                    showNotification(Integer.parseInt(s), false);
                }
            }

            //If no alerts
            TextView tv = findViewById(R.id.noStationAlerts);
            if (mStationAlertsViewModel.getNumAlerts() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });

        if (isConnectedToInternet()) updateAlertsTime = mStationAlertsViewModel.rebuildAlerts();
        if (updateAlertsTime.equals("") || !isConnectedToInternet()) showNoInternetPositiveDialog();

        mFavoritesViewModel.getFavorites().observe(this, stations -> {
            favoritesAdapter.notifyDataSetChanged();

            //If no favorites
            TextView tv = findViewById(R.id.noFavoritesAdded);
            if (mFavoritesViewModel.getNumFavorites() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });

        if (getIntent().getStringExtra("nickname") != null){
            String nickname = getIntent().getStringExtra("nickname");
            String stationID = getIntent().getStringExtra("stationID");
            Log.d("Adding favorite", nickname);
            mFavoritesViewModel.addFavorite(stationID, nickname);
        }

        //Build Alerts API work request
        PeriodicWorkRequest apiAlertsWorkRequest = new PeriodicWorkRequest.Builder(APIWorker.class, 15, TimeUnit.MINUTES)
                .setConstraints(new Constraints.Builder()
                        //.setRequiredNetworkType(NetworkType.CONNECTED)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqueAPIAlertsWork", ExistingPeriodicWorkPolicy.REPLACE, apiAlertsWorkRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(apiAlertsWorkRequest.getId())
                .observe(this, info -> {
                    if (info != null && info.getState() == WorkInfo.State.ENQUEUED) {
                        if (isConnectedToInternet()) updateAlertsTime = mStationAlertsViewModel.rebuildAlerts();
                        if (updateAlertsTime.equals("") || !isConnectedToInternet()) showNoInternetPositiveDialog();
                        tv_alertsTime.setText(updateAlertsTime);
                    }
                });
    }

    private void buildNotification(){
        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("CHANNEL_ID", "Channel", NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("This is a channel");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        //Create notification builder
        builder = new NotificationCompat.Builder(this,"CHANNEL_ID")
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(NotificationCompat.PRIORITY_MAX);
    }

    private boolean isConnectedToInternet(){
        ConnectivityManager cm = (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return (activeNetwork != null && activeNetwork.isConnectedOrConnecting());
    }

    private void showNoInternetPositiveDialog(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Not connected to internet");
        alert.setMessage("You are not connected to the internet. Please refresh and try again.");
        alert.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        alert.show();
    }

    private void showNotification(int id, boolean isNewlyOut){
        //Show notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        //Create notification tap action
        Intent intent = new Intent(this, DisplayAlertActivity.class);
        intent.putExtra("stationID", Integer.toString(id));
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);
        PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        if (isNewlyOut){
            builder.setSmallIcon(R.drawable.status_red)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Elevator is down!")
                   .setContentText("Elevator at " + mStationAlertsViewModel.getStationName(Integer.toString(id)) + " is down");
        } else {
            builder.setSmallIcon(R.drawable.status_green)
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Elevator is back up!")
                    .setContentText("Elevator at " + mStationAlertsViewModel.getStationName(Integer.toString(id)) + " is back up and running");
        }

        notificationManager.notify(id, builder.build());
    }

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

    public StationAlertsViewModel getStationAlertsViewModel(){ return mStationAlertsViewModel; }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Updated Time", updateAlertsTime);
        editor.apply();
    }
}