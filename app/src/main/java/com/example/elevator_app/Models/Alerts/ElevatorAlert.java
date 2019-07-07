package com.example.elevator_app.Models.Alerts;

import java.io.Serializable;

public class ElevatorAlert implements Serializable {

    private final String headline, shortDesc, beginDateTime;

    public ElevatorAlert(String headline, String shortDesc, String beginDateTime){
        this.headline = headline;
        this.shortDesc = shortDesc;
        this.beginDateTime = beginDateTime;
    }

    public String getHeadline(){ return headline; }
    public String getFullDesc(){ return shortDesc; }
    public String getBeginDateTime(){ return beginDateTime; }
}
