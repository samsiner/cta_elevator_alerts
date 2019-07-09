package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private StationRepository mRepository;
    private LiveData<List<Station>> mAllStationAlerts;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = new StationRepository(application);
        mAllStationAlerts = mRepository.getAllStationAlerts();
    }

    public LiveData<List<Station>> getmAllStationAlerts() { return mAllStationAlerts;}

    public void insert(Station station){ mRepository.insert(station);}
}
