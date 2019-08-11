package com.github.cta_elevator_alerts.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

/**
 * Data Access Object for Room Database (station_table)
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

@Dao
public interface StationDao {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insert(Station station);

        @Query("UPDATE station_table SET isFavorite = 1, nickname = :nickname WHERE stationID = :id")
        void addFavorite(String id, String nickname);

        @Query("UPDATE station_table SET isFavorite = 0, nickname = '' WHERE stationID = :id")
        void removeFavorite(String id);

        @Query("UPDATE station_table SET name = :name WHERE stationID = :stationID")
        void updateName(String stationID, String name);

        @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1")
        LiveData<List<Station>> getAllAlertStations();

        @Query("SELECT * FROM station_table WHERE hasElevatorAlert = 1")
        List<Station> getAllAlertStationsNotLiveData();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1")
        List<String> getAllAlertStationIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND red = 1")
        List<String> getAllRedLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND blue = 1")
        List<String> getAllBlueLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND brown = 1")
        List<String> getAllBrownLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND green = 1")
        List<String> getAllGreenLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND orange = 1")
        List<String> getAllOrangeLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND pink = 1")
        List<String> getAllPinkLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND purple = 1")
        List<String> getAllPurpleLineAlertIDs();

        @Query("SELECT stationID FROM station_table WHERE hasElevatorAlert = 1 AND yellow = 1")
        List<String> getAllYellowLineAlertIDs();

        @Query("SELECT * FROM station_table WHERE stationID = :stationID")
        Station getStation(String stationID);

        @Query("SELECT * FROM station_table WHERE isFavorite = 1")
        LiveData<List<Station>> getAllFavorites();

        @Query("SELECT * FROM station_table WHERE isFavorite = 1")
        List<Station> getAllFavoritesNotLiveData();

        @Query("SELECT COUNT(isFavorite) FROM station_table WHERE isFavorite = 1")
        int getFavoritesCount();

        @Query("SELECT COUNT(*) FROM station_table")
        int getStationCount();

        @Query("SELECT name FROM station_table WHERE stationID = :stationID")
        String getName(String stationID);

        @Query("SELECT shortDescription FROM station_table WHERE stationID = :stationID")
        String getShortDescription(String stationID);

        @Query("SELECT beginDateTime FROM station_table WHERE stationID = :stationID")
        String getBeginDateTime(String stationID);

        @Query("SELECT hasElevator FROM station_table WHERE stationID = :stationID")
        boolean getHasElevator(String stationID);

        @Query("UPDATE station_table SET hasElevator = 1 WHERE stationID = :stationID")
        void setHasElevator(String stationID);

        @Query("UPDATE station_table SET hasElevatorAlert = 1, shortDescription = :shortDesc, beginDateTime = :begin WHERE stationID = :stationID")
        void setAlert(String stationID, String shortDesc, String begin);

        @Query("UPDATE station_table SET hasElevatorAlert = 0, shortDescription = '', beginDateTime = '' WHERE stationID = :stationID")
        void removeAlert(String stationID);

        @Query("SELECT hasElevatorAlert FROM station_table WHERE stationID = :stationID")
        boolean getHasElevatorAlert(String stationID);

        @Query("SELECT isFavorite FROM station_table WHERE stationID = :stationID")
        boolean isFavoriteStation(String stationID);

        @Query("SELECT red FROM station_table WHERE stationID = :stationID")
        boolean getRed(String stationID);

        @Query("SELECT blue FROM station_table WHERE stationID = :stationID")
        boolean getBlue(String stationID);

        @Query("SELECT brown FROM station_table WHERE stationID = :stationID")
        boolean getBrown(String stationID);

        @Query("SELECT green FROM station_table WHERE stationID = :stationID")
        boolean getGreen(String stationID);

        @Query("SELECT orange FROM station_table WHERE stationID = :stationID")
        boolean getOrange(String stationID);

        @Query("SELECT pink FROM station_table WHERE stationID = :stationID")
        boolean getPink(String stationID);

        @Query("SELECT purple FROM station_table WHERE stationID = :stationID")
        boolean getPurple(String stationID);

        @Query("SELECT yellow FROM station_table WHERE stationID = :stationID")
        boolean getYellow(String stationID);

        @Query("UPDATE station_table SET red = 1 WHERE stationID = :stationID")
        void setRedTrue(String stationID);

        @Query("UPDATE station_table SET blue = 1 WHERE stationID = :stationID")
        void setBlueTrue(String stationID);

        @Query("UPDATE station_table SET brown = 1 WHERE stationID = :stationID")
        void setBrownTrue(String stationID);

        @Query("UPDATE station_table SET green = 1 WHERE stationID = :stationID")
        void setGreenTrue(String stationID);

        @Query("UPDATE station_table SET orange = 1 WHERE stationID = :stationID")
        void setOrangeTrue(String stationID);

        @Query("UPDATE station_table SET pink = 1 WHERE stationID = :stationID")
        void setPinkTrue(String stationID);

        @Query("UPDATE station_table SET purple = 1 WHERE stationID = :stationID")
        void setPurpleTrue(String stationID);

        @Query("UPDATE station_table SET yellow = 1 WHERE stationID = :stationID")
        void setYellowTrue(String stationID);
}