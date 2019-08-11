package com.github.cta_elevator_alerts.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * Room Database entity (Station)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

@Entity(tableName = "station_table")
public class Station {

    @PrimaryKey
    @NonNull
    public final String stationID;
    public boolean hasElevator, hasElevatorAlert, isFavorite;
    public boolean red, blue, brown, green, orange, pink, purple, yellow;
    public String name, shortDescription, nickname;

    public Station(@NonNull String stationID){
        this.stationID = stationID;
    }
}
