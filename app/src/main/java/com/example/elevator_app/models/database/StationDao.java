package com.example.elevator_app.models.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
//TODO: Write migration class, change version numbers
@Dao
public interface StationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Station station);

        @Query("DELETE FROM station_table")
        void deleteAll();

        @Update
        void update(Station station);

        @Query("UPDATE station_table SET isFavorite = 1, nickname = :nickname WHERE stationID = :id")
        void addFavorite(String id, String nickname);

        @Query("SELECT * FROM station_table")
        LiveData<List<Station>> getAllStations();

        @Query("SELECT * FROM station_table WHERE hasElevatorAlert=1")
        LiveData<List<Station>> getAllAlertStation();

        @Query("SELECT * FROM station_table WHERE stationID = :stationID")
        Station getStation(String stationID);

        @Query("SELECT * FROM station_table WHERE isFavorite=1")
        LiveData<List<Station>> getAllFavorites();
}