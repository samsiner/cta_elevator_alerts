package com.example.elevator_app.models.database;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "station_table")
public class Station {

    @PrimaryKey
    @NonNull
    public String stationID;
    public boolean hasElevator;
    public String[] routes;
    public boolean hasElevatorAlert;
    public String headline, shortDescription, beginDateTime;
}
