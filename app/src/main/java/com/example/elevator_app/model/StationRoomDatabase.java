package com.example.elevator_app.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

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
                            .addCallback(sStationRoomDatabaseCallback)
                            //TODO: Look into migration
                            .fallbackToDestructiveMigration()
                            .addCallback(sStationRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

//    private static StationRoomDatabase.Callback sStationRoomDatabaseCallback = new StationRoomDatabase.Callback(){
//
//        @Override
//        public void onCreate (@NonNull SupportSQLiteDatabase db){
//            super.onCreate(db);
//            Thread thread = new Thread() {
//                public void run() {
//                    StationDao stationDao = INSTANCE.stationDao();
//                    stationDao.deleteAll();
//                }
//            };
//            thread.start();
//        }
//    };

    private static StationRoomDatabase.Callback sStationRoomDatabaseCallback = new StationRoomDatabase.Callback(){

        @Override
        public void onOpen (@NonNull SupportSQLiteDatabase db){
            super.onOpen(db);
            Thread thread = new Thread() {
                public void run() {
                    StationDao stationDao = INSTANCE.stationDao();
                    stationDao.deleteAll();
                }
            };
            thread.start();
        }
    };
}