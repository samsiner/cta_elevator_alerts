package com.github.cta_elevator_alerts.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.List;

/**
 * ViewModel between DisplayAlertActivity and StationRepository
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class DisplayAlertViewModel extends AndroidViewModel {
    private String shortDesc, stationName;
    private boolean hasElevator, hasAlert;
    private final Application application;

    public DisplayAlertViewModel(Application application){
        super(application);
        this.application = application;
    }

    public void setStationID(String stationID){
        StationRepository mRepository = StationRepository.getInstance(application);
        List<String> arrList = mRepository.getAlertDetails(stationID);

        stationName = arrList.get(0);
        shortDesc = arrList.get(1);
        hasElevator = mRepository.mGetHasElevator(stationID);
        hasAlert = mRepository.mGetHasElevatorAlert(stationID);
    }

    public String getShortDesc(){ return shortDesc;}
    public String getStationName(){ return stationName; }
    public boolean getHasElevator(){ return hasElevator; }
    public boolean getHasAlert(){ return hasAlert; }
}
