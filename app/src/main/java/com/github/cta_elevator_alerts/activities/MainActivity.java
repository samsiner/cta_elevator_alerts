package com.github.cta_elevator_alerts.activities;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
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
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.FavoritesAdapter;
import com.github.cta_elevator_alerts.adapters.StationAlertsAdapter;
import com.github.cta_elevator_alerts.model.APIWorker;
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;
import com.github.cta_elevator_alerts.viewmodels.StationAlertsViewModel;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    //Sam:
    //TODO: More tests
    //TODO: Fix worker
    //TODO: improve UI performance - esp startup time

    //Tyler:
    //TODO: Navigation - tabs? (FragmentPagerAdapter?), back stack
    //TODO: Place chicago train image into correct folders
    //TODO: Figure out alternative to Toolbar that can keep our minAPI lower than 21 (try ActionBar)
    //TODO: Remove unused resources (see Analyze -> Inspect Code -> Android -> Lint -> Performance)

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;
    private NotificationCompat.Builder builder;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private TextView tv_alertsTime;
    private SharedPreferences sharedPref;
    private int stationCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_alertsTime = findViewById(R.id.txt_update_alert_time);
        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        tv_alertsTime.setText(sharedPref.getString("Updated Time", ""));

        buildNotification();

        TextView t2 = findViewById(R.id.txt_privacy);
        t2.setMovementMethod(LinkMovementMethod.getInstance());

        //Create ViewModels for favorites and alerts
        mFavoritesViewModel = ViewModelProviders.of(this).get(FavoritesViewModel.class);
        mStationAlertsViewModel = ViewModelProviders.of(this).get(StationAlertsViewModel.class);

        mSwipeRefreshLayout = findViewById(R.id.swipe_main_activity);
        mSwipeRefreshLayout.setOnRefreshListener(() -> {
            new BuildStationsAndAlerts(this).execute();
            mSwipeRefreshLayout.setRefreshing(false);
        });

        //Create recyclerviews to display favorites and alerts
        RecyclerView alertsRecyclerView = findViewById(R.id.recycler_station_alerts);
        final StationAlertsAdapter alertsAdapter = new StationAlertsAdapter(this);
        alertsRecyclerView.setAdapter(alertsAdapter);
        alertsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        RecyclerView favoritesRecyclerView = findViewById(R.id.recycler_favorite_stations);
        final FavoritesAdapter favoritesAdapter = new FavoritesAdapter(this);
        favoritesRecyclerView.setAdapter(favoritesAdapter);
        favoritesRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mStationAlertsViewModel.getStationAlerts().observe(this, stations1 -> {
//            alertsAdapter.notifyDataSetChanged();

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
            if (mStationAlertsViewModel.getNumAlerts() < 1) {
                tv.setVisibility(View.VISIBLE);
            } else {
                tv.setVisibility(View.GONE);
            }
        });

        new BuildStationsAndAlerts(this).execute();

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

        mStationAlertsViewModel.getUpdateAlertsTime().observe(this, time -> tv_alertsTime.setText(time));

        if (getIntent().getStringExtra("nickname") != null){
            String nickname = getIntent().getStringExtra("nickname");
            String stationID = getIntent().getStringExtra("stationID");
            mFavoritesViewModel.addFavorite(stationID, nickname);
        }

        mStationAlertsViewModel.getConnectionStatus().observe(this, isConnected -> {
            if (!isConnected) {
                Toast toast = Toast.makeText(this, "Not connected - please refresh!", Toast.LENGTH_SHORT);
                toast.show();
            }
        });

        mStationAlertsViewModel.getStationCount().observe(this, stationCount -> this.stationCount = stationCount);

        if (getIntent().getStringExtra("nickname") != null){
            String nickname = getIntent().getStringExtra("nickname");
            String stationID = getIntent().getStringExtra("stationID");
            mFavoritesViewModel.addFavorite(stationID, nickname);
        }

        //Build Alerts API work request
        PeriodicWorkRequest apiAlertsWorkRequest = new PeriodicWorkRequest.Builder(APIWorker.class, 15, TimeUnit.MINUTES)
                .addTag("UniqueAPIAlertsWork")
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqueAPIAlertsWork", ExistingPeriodicWorkPolicy.REPLACE, apiAlertsWorkRequest);

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(apiAlertsWorkRequest.getId())
                .observe(this, info -> {
                    if (info != null && info.getState() == WorkInfo.State.ENQUEUED) {
                        new BuildStationsAndAlerts(this).execute();
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

    private static class BuildStationsAndAlerts extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> mainActivity;

        BuildStationsAndAlerts(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute(){
            MainActivity activity = mainActivity.get();
            Toast toast = Toast.makeText(activity, "Updating Stations and Alerts", Toast.LENGTH_SHORT);
            toast.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity activity = mainActivity.get();
            if (activity.stationCount <= 0) activity.mStationAlertsViewModel.buildStations();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.updateStationCount();
            activity.mStationAlertsViewModel.updateConnectionStatus();
            if (activity.stationCount > 0) new UpdateAlerts(activity).execute();
        }
    }

    private static class UpdateAlerts extends AsyncTask<Void, Void, Void> {

        private WeakReference<MainActivity> mainActivity;

        UpdateAlerts(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.rebuildAlerts();
            return null;
        }

        @Override
        protected void onPostExecute(Void results) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.updateUpdatedAlertsTime();
            activity.mStationAlertsViewModel.updateConnectionStatus();
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