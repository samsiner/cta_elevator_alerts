package com.example.elevator_app.models.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Station.class}, version = 1)
public abstract class StationRoomDatabase extends RoomDatabase {
    public abstract StationDao stationDao();

    private static volatile StationRoomDatabase INSTANCE;

    static StationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StationRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StationRoomDatabase.class, "station_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}