package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public void rebuildAlerts(){ mRepository.buildAlerts(); }
    public void buildStations(){ mRepository.buildStations();}

    public void updateStationCount(){ mRepository.updateStationCount(); }
    public void updateConnectionStatus(){ mRepository.updateConnectionStatus();}
    public void updateUpdatedAlertsTime(){ mRepository.updateUpdatedAlertsTime();}

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public List<Station> mGetStationAlertsNotLiveData(){ return mRepository.mGetStationAlertsNotLiveData(); }
    public LiveData<Integer> getStationCount(){ return mRepository.getStationCount(); }
    public int getNumAlerts(){ return mRepository.getAlertsCount(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }
    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
    public LiveData<String> getUpdateAlertsTime(){ return mRepository.getUpdatedAlertsTime(); }
    public LiveData<Boolean> getConnectionStatus(){ return mRepository.getConnectionStatus(); }
}
