package com.example.elevator_app.models.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity(tableName = "station_table")
public class Station {

    @PrimaryKey
    @NonNull
    private String stationID;
    private boolean hasElevator, hasElevatorAlert;
    private boolean[] routes;
    private String name, headline, shortDescription, beginDateTime;

    public Station(String stationID, String name, boolean hasElevator, boolean[] routes){
        this.stationID = stationID;
        this.name = name;
        this.hasElevator = hasElevator;
        this.routes = routes;
        hasElevatorAlert = false;
        headline = "";
        shortDescription = "";
        beginDateTime = "";
    }

    public String getStationID(){ return stationID; }
    public String getName(){ return name; }
    public String getHeadline(){ return headline; }
    public String getShortDescription(){ return shortDescription; }
    public String getBeginDateTime(){ return beginDateTime; }
    public boolean isHasElevator(){ return hasElevator; }
    public boolean isHasElevatorAlert(){ return hasElevatorAlert; }
    public boolean[] getRoutes(){ return routes; }

    public void addAlert(String headline, String shortDescription, String beginDateTime){
        this.headline = headline;
        this.shortDescription = shortDescription;
        this.beginDateTime = convertDateTime(beginDateTime);
        hasElevatorAlert = true;
    }

    public void removeAlert(){
        this.headline = "";
        this.shortDescription = "";
        this.beginDateTime = "";
        hasElevatorAlert = false;
    }

    private String convertDateTime(String s){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy'-'MM'-'dd'T'HH:mm:ss", Locale.US);
        try {
            Date originalDate = dateFormat.parse(s);
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("MMMM' 'dd', 'yyyy' at 'h:mm a", Locale.US);
            return dateFormat2.format(originalDate);
        } catch (ParseException e) {
            return s;
        }
    }
}
