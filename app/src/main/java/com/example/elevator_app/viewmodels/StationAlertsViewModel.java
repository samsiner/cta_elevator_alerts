package com.example.elevator_app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.elevator_app.model.Station;
import com.example.elevator_app.model.StationRepository;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private LiveData<List<Station>> mAllAlertStations;
    private Station mStation;
    StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
        mAllAlertStations = mRepository.mGetAllAlertStations();
    }

    public LiveData<List<Station>> getStationAlerts() { return mAllAlertStations;}
    public Station getStation(){ return mStation; }
    public void putAlertsIntoDatabase(String JSONString){
        mRepository.buildAlerts(JSONString);
    }



}
