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
    private final String shortDesc, beginDateTime, stationName;
    private final boolean hasElevator;

    public DisplayAlertViewModel(Application application, String stationID){
        super(application);
        StationRepository mRepository = StationRepository.getInstance(application);
        List<String> arrList = mRepository.getAlertDetails(stationID);

        stationName = arrList.get(0);
        shortDesc = arrList.get(1);
        beginDateTime = arrList.get(2);
        hasElevator = mRepository.mGetHasElevator(stationID);
    }

    public String getShortDesc(){ return shortDesc;}
    public String getBeginDateTime(){ return beginDateTime;}
    public String getStationName(){ return stationName; }
    public boolean getHasElevator(){ return hasElevator; }
}
