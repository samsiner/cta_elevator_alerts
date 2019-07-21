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
    public void putAlertsIntoDatabase(String JSONString){
        mRepository.buildAlerts(JSONString);
    }

    public int getNumAlerts(){ return mRepository.getAlertsCount(); }
    public List<String> getStationElevatorsNewlyWorking(){ return mRepository.getFavoriteElevatorNewlyWorking(); }
    public List<String> getStationElevatorsNewlyOut(){ return mRepository.getFavoriteElevatorNewlyOut(); }
    public String getStationName(String stationID){ return mRepository.mGetStationName(stationID); }
    public void setClicked(){mRepository.setClickedTrue();}
}
