package com.example.elevator_app.models.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Station station);

        @Query("DELETE FROM station_table")
        void deleteAll();

        @Update
        void update(Station station);

        @Query("SELECT * from station_table")
        LiveData<List<Station>> getAllStations();
}

