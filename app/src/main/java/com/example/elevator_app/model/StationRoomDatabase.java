package com.example.elevator_app.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Station.class}, version = 3)
public abstract class StationRoomDatabase extends RoomDatabase {
    public abstract StationDao stationDao();

    private static volatile StationRoomDatabase INSTANCE;

    static StationRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (StationRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            StationRoomDatabase.class, "station_database")
                            .addCallback(sStationRoomDatabaseCallback)
                            //TODO: Look into migration
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static StationRoomDatabase.Callback sStationRoomDatabaseCallback = new StationRoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);

            Thread thread = new Thread() {
                public void run() {
                    StationDao stationDao = INSTANCE.stationDao();
                    stationDao.deleteAll();

//                    //Add sample station with alert
//                    Station station = new Station("41140", "King Drive", true);
//                    station.setRoutes(false, false, false, true, false, false, false, false);
//                    station.addAlert("Elevator at King Drive Temporarily Out-of-Service", "The elevator at Central Park (Pink Line) is temporarily out-of-service.", "2019-06-07T19:05:00");
//                    stationDao.insert(station);
//
//                    //Add sample station without alert
//                    Station station2 = new Station("40340", "Berwyn", false);
//                    stationDao.insert(station2);


                }
            };
            thread.start();
        }
    };
}