package com.example.elevator_app;

import java.util.*;

public class ElevatorAlert{

    private String headline, shortDesc, fullDesc, beginDateTime, endDateTime;

    public ElevatorAlert(String headline, String shortDesc, String fullDesc, String beginDateTime, String endDateTime){
        this.headline = headline;
        this.shortDesc = shortDesc;
        this.fullDesc = fullDesc;
        this.beginDateTime = beginDateTime;
        this.endDateTime = endDateTime;
    }

    public String getHeadline(){ return headline; }
    public String getShortDesc(){ return shortDesc; }
    public String getFullDesc(){ return fullDesc; }
    public String getBeginDateTime(){ return beginDateTime; }
    public String getEndDateTime() { return endDateTime; }

}
