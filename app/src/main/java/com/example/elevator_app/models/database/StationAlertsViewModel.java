package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private StationRepository mRepository;
    private LiveData<List<Station>> mAllStations;
    private LiveData<List<Station>> mAllAlertStations;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = new StationRepository(application);
        mAllAlertStations = mRepository.mGetAllAlertStations();
        mAllStations = mRepository.mGetAllStations();
    }

    public LiveData<List<Station>> getmAllAlertStations() { return mAllAlertStations;}

    public LiveData<List<Station>> getmAllStations() { return mAllStations;}

    public void insert(Station station){ mRepository.insert(station);}
}
