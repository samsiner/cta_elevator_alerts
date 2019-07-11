package com.example.elevator_app.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elevator_app.adapters.FavoritesAdapter;
import com.example.elevator_app.viewmodels.FavoritesViewModel;
import com.example.elevator_app.adapters.StationAlertsAdapter;
import com.example.elevator_app.viewmodels.StationAlertsViewModel;
import com.example.elevator_app.R;
import com.example.elevator_app.model.Station;

import java.util.List;

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