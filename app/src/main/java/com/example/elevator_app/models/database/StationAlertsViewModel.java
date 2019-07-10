package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private LiveData<List<Station>> mAllAlertStations;

    public StationAlertsViewModel(Application application){
        super(application);
        StationRepository mRepository = StationRepository.getInstance(application);
        mAllAlertStations = mRepository.mGetAllAlertStations();
    }

    public LiveData<List<Station>> getStationAlerts() { return mAllAlertStations;}
}
