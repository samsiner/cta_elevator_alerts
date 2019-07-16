package com.example.elevator_app.viewmodels;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;

import com.example.elevator_app.model.StationRepository;

import java.util.List;

public class DisplayAlertViewModel extends AndroidViewModel {
    private String headline, shortDesc, beginDateTime, stationName;
    private boolean hasElevator;

    public DisplayAlertViewModel(Application application, String stationID){
        super(application);
        StationRepository mRepository = StationRepository.getInstance(application);
        List<String> arrList = mRepository.getAlertDetails(stationID);

        stationName = arrList.get(0);
        headline = arrList.get(1);
        shortDesc = arrList.get(2);
        beginDateTime = arrList.get(3);
        hasElevator = mRepository.mGetHasElevator(stationID);
    }

    public String getHeadline(){ return headline;}
    public String getShortDesc(){ return shortDesc;}
    public String getBeginDateTime(){ return beginDateTime;}
    public String getStationName(){ return stationName; }
    public boolean getHasElevator(){ return hasElevator; }

}
