package com.example.elevator_app.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "station_table")
public class Station {

    @PrimaryKey
    @NonNull
    public final String stationID;
    public boolean hasElevator, hasElevatorAlert, isFavorite;
    public boolean red, blue, brown, green, orange, pink, purple, yellow;
    public String name, shortDescription, beginDateTime, nickname;

    public Station(@NonNull String stationID){
        this.stationID = stationID;
    }
}
