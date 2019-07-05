package com.example.elevator_app.Models.Alerts;

import java.io.Serializable;

public class ElevatorAlert implements Serializable {

    private final String headline, shortDesc, fullDesc, beginDateTime;

    public ElevatorAlert(String headline, String shortDesc, String fullDesc, String beginDateTime){
        this.headline = headline;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.beginDateTime = beginDateTime;
    }

    public String getHeadline(){ return headline; }
    public String getShortDesc(){ return shortDesc; }
    public String getFullDesc(){ return fullDesc; }
    public String getBeginDateTime(){ return beginDateTime; }
}
