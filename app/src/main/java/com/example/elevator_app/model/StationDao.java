package com.example.elevator_app.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
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

        @Query("UPDATE station_table SET isFavorite = 0, nickname = '' WHERE stationID = :id")
        void removeFavorite(String id);

        @Query("SELECT * FROM station_table")
        LiveData<List<Station>> getAllStations();

        @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1")
        LiveData<List<Station>> getAllAlertStations();

        @Query("SELECT * FROM station_table WHERE stationID = :stationID")
        Station getStation(String stationID);

        @Query("SELECT * FROM station_table WHERE isFavorite = 1")
        LiveData<List<Station>> getAllFavorites();

        @Query("SELECT COUNT(isFavorite) FROM station_table WHERE isFavorite = 1")
        int getFavoritesCount();

        @Query("SELECT name FROM station_table where stationID = :stationID")
        String getName(String stationID);

        @Query("SELECT headline FROM station_table where stationID = :stationID")
        String getHeadline(String stationID);

        @Query("SELECT shortDescription FROM station_table where stationID = :stationID")
        String getShortDescription(String stationID);

        @Query("SELECT beginDateTime FROM station_table where stationID = :stationID")
        String getBeginDateTime(String stationID);

        @Query("SELECT hasElevator FROM station_table WHERE stationID = :stationID")
        boolean getHasElevator(String stationID);

        @Query("SELECT hasElevatorAlert FROM station_table WHERE stationID = :stationID")
        boolean getHasElevatorAlert(String stationID);
}