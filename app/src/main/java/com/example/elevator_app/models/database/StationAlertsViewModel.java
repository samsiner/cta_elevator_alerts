package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private LiveData<List<Station>> mAllAlertStations;
    private LiveData<List<Station>> mAllFavorites;
    private MediatorLiveData<List<Station>> liveDataMerger = new MediatorLiveData<>();

    public StationAlertsViewModel(Application application){
        super(application);
        StationRepository mRepository = new StationRepository(application);
        mAllAlertStations = mRepository.mGetAllAlertStations();
        mAllFavorites = mRepository.mGetAllFavorites();
        liveDataMerger.addSource(mAllAlertStations, value -> liveDataMerger.setValue(value));
        liveDataMerger.addSource(mAllFavorites, value -> liveDataMerger.setValue(value));
    }

    public LiveData<List<Station>> getFavoritesAndAlerts() { return liveDataMerger;}
    public LiveData<List<Station>> getAlerts() { return mAllAlertStations;}
    public LiveData<List<Station>> getFavorites() { return mAllFavorites;}


}
