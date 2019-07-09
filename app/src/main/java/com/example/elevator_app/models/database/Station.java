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
    public String stationID;
    public boolean hasElevator, hasElevatorAlert;
    public boolean red, blue, brown, green, orange, pink, purple, yellow;
    public String name, headline, shortDescription, beginDateTime;

    public Station(String stationID, String name, boolean hasElevator){
        this.stationID = stationID;
        this.name = name;
        this.hasElevator = hasElevator;
        hasElevatorAlert = false;
        headline = "";
        shortDescription = "";
        beginDateTime = "";
    }

    public void setRoutes(boolean red, boolean blue, boolean brown, boolean green,boolean orange, boolean pink, boolean purple, boolean yellow){
        this.red = red;
        this.blue = blue;
        this.brown = brown;
        this.green = green;
        this.orange = orange;
        this.pink = pink;
        this.purple = purple;
        this.yellow = yellow;
    }

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
