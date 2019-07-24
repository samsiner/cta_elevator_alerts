package com.example.elevator_app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.elevator_app.model.Station;
import com.example.elevator_app.model.StationRepository;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public List<Station> mGetStationAlertsNotLiveData(){ return mRepository.mGetStationAlertsNotLiveData(); }
    public void rebuildAlerts(){mRepository.buildAlerts();}

    public int getNumAlerts(){ return mRepository.getAlertsCount(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }
    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
    public void addFavorite(String stationID, String nickname){ mRepository.addFavorite(stationID, nickname);}
}
