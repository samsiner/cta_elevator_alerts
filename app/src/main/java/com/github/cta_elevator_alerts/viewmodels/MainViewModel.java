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

public class MainViewModel extends AndroidViewModel {

    private final StationRepository mRepository;

    public MainViewModel(Application application){
        super(application);
        mRepository = StationRepository.getInstance(application);
    }

    public void setConnectionStatus(boolean b){ mRepository.setConnectionStatus(b);}

    public LiveData<List<Station>> getStationAlerts() { return mRepository.mGetAllAlertStations();}
    public LiveData<List<Station>> getFavorites() { return mRepository.mGetAllFavorites(); }
    public LiveData<String> getUpdateAlertsTime(){ return mRepository.getUpdatedAlertsTime(); }
    public LiveData<Boolean> getConnectionStatus(){ return mRepository.getConnectionStatus(); }
    public boolean[] getAllRoutes(String stationID){ return mRepository.mGetAllRoutes(stationID);}
    public boolean getHasElevatorAlert(String stationID){ return mRepository.mGetHasElevatorAlert(stationID);}

    //TODO: Comment out
    public void removeAlertKing(){ mRepository.removeAlertKing();}
    public void addAlertHoward(){ mRepository.addAlertHoward(); }
    public List<String> mGetStationAlertIDs(){ return mRepository.mGetStationAlertIDs(); }
//    public void addFavoriteKing(){ mRepository.addFavorite("41140");}
//    public void addFavoriteKimball(){ mRepository.addFavorite("41290");}
}
