package com.github.cta_elevator_alerts.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.work.Constraints;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.FavoritesAdapter;
import com.github.cta_elevator_alerts.adapters.StationAlertsAdapter;
import com.github.cta_elevator_alerts.workers.NetworkWorker;
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;
import com.github.cta_elevator_alerts.viewmodels.StationAlertsViewModel;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

/**
 * MainActivity displays favorite stations and their
 * elevator status, all current alerts,
 * the most updated date/time, and the privacy policy.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class MainActivity extends AppCompatActivity {

    //Sam:
    //TODO: Fix worker/notifications
    //TODO: Splash screen
    //TODO: Refactor with LiveData.postvalue

    //Tyler:
    //TODO: Navigation - tabs? (FragmentPagerAdapter?), back stack
    //TODO: Update app icon on google play console

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;
    private NotificationCompat.Builder builder;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter alertsAdapter, favoritesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        //Create ViewModels for favorites and alerts
        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        mStationAlertsViewModel = ViewModelProviders.of(this).get(StationAlertsViewModel.class);

        //Create adapter to display alerts
        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        alertsAdapter = new StationAlertsAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Create adapter to display favorites
        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        favoritesAdapter = new FavoritesAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        addNotificationChannel();
        addPrivacyPolicyLink();
        addSwipeRefresh();
        addAlertsObserver();

        buildStationsAndAlertsAsync();

        addFavoritesObserver();
        addLastUpdatedObserver();
        addConnectionStatusObserver();
        addNetworkWorker();
        addFavorite();
    }

    private void addNotificationChannel(){
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

    private void addPrivacyPolicyLink(){
        TextView t2 = findViewById(R.id.txt_privacy);
        t2.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addSwipeRefresh(){
        mSwipeRefreshLayout = findViewById(R.id.swipe_main_activity);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            buildStationsAndAlertsAsync();
            mSwipeRefreshLayout.setRefreshing(false);
        });
    }

    private void addAlertsObserver(){
        mStationAlertsViewModel.getStationAlerts().observe(this, stations1 -> {
            try{
                alertsAdapter.notifyDataSetChanged();
            } catch (IllegalStateException e){
                return;
            }

            //Display notification if elevator is newly out
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
            if (mStationAlertsViewModel.mGetStationAlertsNotLiveData().size() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });
    }

    public void buildStationsAndAlertsAsync(){
        Toast toast = Toast.makeText(this, "Updating Stations and Alerts", Toast.LENGTH_SHORT);
        toast.show();
        new BuildStationsAndAlerts(this).execute();
    }

    private void addFavoritesObserver(){
        mFavoritesViewModel.getFavorites().observe(this, stations -> {
            try{
                favoritesAdapter.notifyDataSetChanged();
            } catch (IllegalStateException e){
                return;
            }

            //If no favorites
            TextView tv = findViewById(R.id.noFavoritesAdded);
            if (mFavoritesViewModel.getNumFavorites() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });
    }

    private void addLastUpdatedObserver(){
        TextView tv_alertsTime = findViewById(R.id.txt_update_alert_time);
        mStationAlertsViewModel.getUpdateAlertsTime().observe(this, tv_alertsTime::setText);
    }

    private void addConnectionStatusObserver(){
        mStationAlertsViewModel.getConnectionStatus().observe(this, isConnected -> {
            if (!isConnected) {
                Toast toast = Toast.makeText(this, "Not connected - please refresh!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    private void addNetworkWorker(){
        //Build Alerts API work request
        PeriodicWorkRequest apiAlertsWorkRequest = new PeriodicWorkRequest.Builder(NetworkWorker.class, 15, TimeUnit.MINUTES)
                .addTag("UniqueAPIAlertsWork")
                .setConstraints(new Constraints.Builder()
//                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqueAPIAlertsWork", ExistingPeriodicWorkPolicy.REPLACE, apiAlertsWorkRequest);
    }

    private void addFavorite(){
        if (getIntent().getStringExtra("nickname") != null){
            String nickname = getIntent().getStringExtra("nickname");
            String stationID = getIntent().getStringExtra("stationID");
            mFavoritesViewModel.addFavorite(stationID, nickname);
        }
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
                    .setColor(getResources().getColor(R.color.colorAndroidRed))
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Elevator is down!")
                    .setContentText("Elevator at " + mStationAlertsViewModel.getStationName(Integer.toString(id)) + " is down");
        } else {
            builder.setSmallIcon(R.drawable.status_green)
                    .setColor(getResources().getColor(R.color.colorAndroidGreen))
                    .setContentIntent(resultPendingIntent)
                    .setContentTitle("Elevator is back up!")
                    .setContentText("Elevator at " + mStationAlertsViewModel.getStationName(Integer.toString(id)) + " is back up and running");
        }

        notificationManager.notify(id, builder.build());
    }

    private static class BuildStationsAndAlerts extends AsyncTask<Void, Void, Void> {

        private final WeakReference<MainActivity> mainActivity;

        BuildStationsAndAlerts(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.buildStations();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            MainActivity activity = mainActivity.get();
            new UpdateAlerts(activity).execute();
        }
    }

    private static class UpdateAlerts extends AsyncTask<Void, Void, Void> {

        private final WeakReference<MainActivity> mainActivity;

        UpdateAlerts(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.rebuildAlerts();
            return null;
        }
    }

    public void toAddFavoriteActivity(View v){
        Intent intent = new Intent(MainActivity.this, AddFavoriteActivity.class);
        intent.putExtra("fromEdit", false);
        startActivity(intent);
    }

    public void toAllLinesActivity(View v){
        Intent intent = new Intent(MainActivity.this, AllLinesActivity.class);
        startActivity(intent);
    }

    public FavoritesViewModel getFavoritesViewModel(){ return mFavoritesViewModel; }

    public StationAlertsViewModel getStationAlertsViewModel(){ return mStationAlertsViewModel; }
}