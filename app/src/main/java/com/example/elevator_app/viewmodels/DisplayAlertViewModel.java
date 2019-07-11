package com.example.elevator_app.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;

import com.example.elevator_app.model.StationRepository;

import java.util.List;

public class DisplayAlertViewModel extends AndroidViewModel {
    private String headline, shortDesc, beginDateTime, stationName;

    public DisplayAlertViewModel(Application application, String stationID){
        super(application);
        StationRepository mRepository = StationRepository.getInstance(application);
        List<String> arrList = mRepository.getAlertDetails(stationID);
        stationName = arrList.get(0);
        headline = arrList.get(1);
        shortDesc = arrList.get(2);
        beginDateTime = arrList.get(3);
    }

    public String getHeadline(){ return headline;}
    public String getShortDesc(){ return shortDesc;}
    public String getBeginDateTime(){ return beginDateTime;}
    public String getStationName(){ return stationName; }
}
