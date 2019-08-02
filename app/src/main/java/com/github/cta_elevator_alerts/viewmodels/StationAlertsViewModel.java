package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.ArrayList;
import java.util.List;

public class StationAlertsViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public List<Station> mGetStationAlertsNotLiveData(){ return mRepository.mGetStationAlertsNotLiveData(); }
    public String rebuildAlerts(){return mRepository.buildAlerts();}

    public int getNumAlerts(){ return mRepository.getAlertsCount(); }
    public ArrayList<Integer> getLineAlertsCount(){ return mRepository.getLineAlertsCount(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }
    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
    public String buildStations(){ return mRepository.buildStations();}
    public void removeAllAlerts(){ mRepository.removeAllAlerts();}
}
