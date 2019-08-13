package com.github.cta_elevator_alerts.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
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
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.adapters.FavoritesAdapter;
import com.github.cta_elevator_alerts.adapters.StationAlertsAdapter;
import com.github.cta_elevator_alerts.viewmodels.FavoritesViewModel;
import com.github.cta_elevator_alerts.viewmodels.StationAlertsViewModel;
import com.github.cta_elevator_alerts.utils.NetworkWorker;
import com.github.cta_elevator_alerts.utils.NotificationPusher;

import java.lang.ref.WeakReference;
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

    //Sam:
    //TODO: Change notifications to favorites only

    //Tyler:
    //TODO: Bottom navigation
    //TODO: Launcher icon silhouette (see Inspect Code)

    private StationAlertsViewModel mStationAlertsViewModel;
    private FavoritesViewModel mFavoritesViewModel;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView.Adapter alertsAdapter, favoritesAdapter;
    private SharedPreferences sharedPreferences;
    private TextView tv_alertsTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme_NoActionBar);
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

        //Create SharedPreferences for last updated date/time
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        tv_alertsTime = findViewById(R.id.txt_update_alert_time);
        String time = sharedPreferences.getString("LastUpdatedTime", "");
        if (time != null && !time.equals("")) tv_alertsTime.setText(time);

        addPrivacyPolicyLink();
        addSwipeRefresh();
        addAlertsObserver();
        addFavoritesObserver();
        addLastUpdatedObserver();
        addConnectionStatusObserver();
        addNetworkWorker();

        if (getIntent().getStringExtra("nickname") != null) addFavorite();

        //TODO: Remove
        addTestButtons();
    }

    private void addTestButtons(){
        Button b = new Button(this);
        b.setText("Remove alert quincy");
        LinearLayout l = findViewById(R.id.LinearLayout);
        b.setOnClickListener(v -> {
            ArrayList<String> past = (ArrayList<String>) mStationAlertsViewModel.mGetStationAlertIDs();
            mStationAlertsViewModel.removeAlertQuincy();
            ArrayList<String> curr = (ArrayList<String>) mStationAlertsViewModel.mGetStationAlertIDs();
            NotificationPusher.createAlertNotifications(this, past, curr);
        });
        l.addView(b);

        Button b1 = new Button(this);
        b1.setText("Add alert Howard");
        b1.setOnClickListener(v -> {
            ArrayList<String> past = (ArrayList<String>) mStationAlertsViewModel.mGetStationAlertIDs();
            mStationAlertsViewModel.addAlertHoward();
            ArrayList<String> curr = (ArrayList<String>) mStationAlertsViewModel.mGetStationAlertIDs();
            NotificationPusher.createAlertNotifications(this, past, curr);
        });
        l.addView(b1);
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
        mStationAlertsViewModel.getStationAlerts().observe(this, alerts -> {
                alertsAdapter.notifyDataSetChanged();

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

    private void buildStationsAndAlertsAsync(){
        Toast toast = Toast.makeText(this, "Updating...", Toast.LENGTH_SHORT);
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
        mStationAlertsViewModel.getUpdateAlertsTime().observe(this, text -> {
            tv_alertsTime.setText(text);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("LastUpdatedTime", text);
            editor.apply();
        });
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
        PeriodicWorkRequest apiAlertsWorkRequest = new PeriodicWorkRequest.Builder(NetworkWorker.class, 15, TimeUnit.MINUTES)
                .addTag("UniqueAPIAlertsWork")
                .setConstraints(new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED)
                        .setRequiresBatteryNotLow(true)
                        .setRequiresStorageNotLow(true)
                        .build())
                .build();

        WorkManager.getInstance(this).enqueueUniquePeriodicWork("UniqueAPIAlertsWork", ExistingPeriodicWorkPolicy.REPLACE, apiAlertsWorkRequest);

        //If not connected, update connection status to "Not Connected" because worker won't run
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork == null || !activeNetwork.isConnected()) buildStationsAndAlertsAsync();
    }

    private void addFavorite(){
        String nickname = getIntent().getStringExtra("nickname");
        String stationID = getIntent().getStringExtra("stationID");
        mFavoritesViewModel.addFavorite(stationID, nickname);
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
        private ArrayList<String> pastAlerts;

        UpdateAlerts(MainActivity activity) {
            mainActivity = new WeakReference<>(activity);
        }

        @Override
        protected void onPreExecute(){
            MainActivity activity = mainActivity.get();
            pastAlerts = (ArrayList<String>)activity.mStationAlertsViewModel.mGetStationAlertIDs();
        }

        @Override
        protected Void doInBackground(Void... params) {
            MainActivity activity = mainActivity.get();
            activity.mStationAlertsViewModel.rebuildAlerts();
            return null;
        }

        @Override
        protected void onPostExecute(Void results){
            MainActivity activity = mainActivity.get();
            ArrayList<String> currentAlerts = (ArrayList<String>)activity.mStationAlertsViewModel.mGetStationAlertIDs();
            NotificationPusher.createAlertNotifications(activity, pastAlerts, currentAlerts);
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