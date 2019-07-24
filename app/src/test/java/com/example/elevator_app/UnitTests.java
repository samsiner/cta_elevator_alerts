package com.example.elevator_app;

import androidx.room.Room;
import android.content.Context;

import com.example.elevator_app.model.Station;
import com.example.elevator_app.model.StationDao;
import com.example.elevator_app.model.StationRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.List;

/**
 * Unit tests for temp.
 */

@RunWith(JUnit4.class)
public class UnitTests {
        private StationDao stationDao;
        private StationRoomDatabase db;

        @Before
        public void createDb() {
////            Context context = ApplicationProvider.getApplicationContext();
//            db = Room.inMemoryDatabaseBuilder(context, StationRoomDatabase.class).build();
//            stationDao = db.getDao();
        }

        @After
        public void closeDb() throws IOException {
//            db.close();
        }

        @Test
        public void checkStation() throws Exception {
            Station station = new Station("40900");
            assertEquals(station.name, "");
            assertEquals(station.shortDescription, "");
            assertEquals(station.beginDateTime, "");
            assertEquals(station.nickname, "");
            assertFalse(station.hasElevator);
            assertFalse(station.hasElevatorAlert);
            assertFalse(station.isFavorite);

            station.hasElevator = true;
            station.setName("Howard");
            station.addAlert("short description", "begin date time");

            assertEquals(station.name, "Howard");
            assertEquals(station.shortDescription, "short description");
            assertEquals(station.beginDateTime, "begin date time");
            assertEquals(station.nickname, "");
            assertTrue(station.hasElevator);
            assertTrue(station.hasElevatorAlert);
            assertFalse(station.isFavorite);

//            stationDao.insert(station);
//            stationDao.addFavorite("40900", "Home");
//
//            assertEquals(stationDao.getFavoritesCount(), 1);
//            Station station2 = stationDao.getStation("40900");
//            assertEquals(station2.name, "Howard");
//            assertEquals(station2.shortDescription, "short description");
//            assertEquals(station2.beginDateTime, "begin date time");
//            assertEquals(station2.nickname, "Home");
//            assertTrue(station2.hasElevator);
//            assertTrue(station2.hasElevatorAlert);
//            assertTrue(station2.isFavorite);
        }
}