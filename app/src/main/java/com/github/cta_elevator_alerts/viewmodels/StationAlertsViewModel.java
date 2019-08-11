package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.List;

/**
 * ViewModel between MainActivity and StationRepository
 * to display station alerts
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class StationAlertsViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public StationAlertsViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public void rebuildAlerts(){ mRepository.buildAlerts(); }
    public void buildStations(){ mRepository.buildStations();}

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public LiveData<String> getUpdateAlertsTime(){ return mRepository.getUpdatedAlertsTime(); }
    public LiveData<String> getNewlyOut(){ return mRepository.getNewlyOut(); }
    public LiveData<String> getNewlyWorking(){ return mRepository.getNewlyWorking(); }
    public LiveData<Boolean> getConnectionStatus(){ return mRepository.getConnectionStatus(); }

    //TEST
    public void removeAlertClark(){ mRepository.removeAlertClark();}

    public List<Station> mGetStationAlertsNotLiveData(){ return mRepository.mGetStationAlertsNotLiveData(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }

    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }

    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
}
