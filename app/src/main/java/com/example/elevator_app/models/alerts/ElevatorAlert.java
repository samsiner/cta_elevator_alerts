package com.example.elevator_app.models.alerts;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "elevator_alert_table")
public class ElevatorAlert {

    @PrimaryKey
    @ColumnInfo(name = "headline")
    private String stationID;



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
