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

    public void setConnectionStatus(boolean b){ mRepository.setConnectionStatus(b);}

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public LiveData<String> getUpdateAlertsTime(){ return mRepository.getUpdatedAlertsTime(); }
    public LiveData<Boolean> getConnectionStatus(){ return mRepository.getConnectionStatus(); }

    //TODO: Remove next two methods
    public void removeAlertQuincy(){ mRepository.removeAlertQuincy();}
    public void addAlertHoward(){ mRepository.addAlertHoward(); }

    public List<String> mGetStationAlertIDs(){ return mRepository.mGetStationAlertIDs(); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
}
