package com.example.elevator_app.models.database;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class StationRepository {
    private StationDao mStationDao;
    private LiveData<List<Station>> mAllStations;

    StationRepository(Application application) {
        StationRoomDatabase db = StationRoomDatabase.getDatabase(application);
        mStationDao = db.stationDao();
        mAllStations = mStationDao.getAllStations();
    }

    LiveData<List<Station>> getAllStationAlerts() {
        return mAllStations;
    }

    public void insert (Station station) {
        Thread thread = new Thread() {
            public void run() {
                mStationDao.insert(station);
            }
        };
    }


}
