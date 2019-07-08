package com.example.elevator_app.models.alerts;

import java.io.Serializable;

public class ElevatorAlert implements Serializable {

    private final String headline, shortDesc, beginDateTime;

    public ElevatorAlert(String headline, String shortDesc, String beginDateTime){
        this.headline = headline;
        this.shortDesc = shortDesc;
        this.beginDateTime = beginDateTime;
    }

    public String getHeadline(){ return headline; }
    public String getShortDesc(){ return shortDesc; }
    public String getBeginDateTime(){ return beginDateTime; }
}
