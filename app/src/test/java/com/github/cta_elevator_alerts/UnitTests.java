package com.github.cta_elevator_alerts;

import android.content.Context;

import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;

import com.github.cta_elevator_alerts.model.Station;
import com.github.cta_elevator_alerts.model.StationDao;
import com.github.cta_elevator_alerts.model.StationRoomDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for temp.
 */

@RunWith(JUnit4.class)
public class UnitTests {

        @Test
        public void testStationClass() {
            Station station = new Station("40900");
            assertEquals(station.name, "");
            assertEquals(station.shortDescription, "");
            assertEquals(station.beginDateTime, "");
            assertEquals(station.nickname, "");
            assertFalse(station.hasElevator);
            assertFalse(station.hasElevatorAlert);
            assertFalse(station.isFavorite);

            station.hasElevator = true;
            station.name = "Howard";
            station.hasElevatorAlert = true;
            station.shortDescription = "short description";
            station.beginDateTime = "begin date time";

            assertEquals(station.name, "Howard");
            assertEquals(station.shortDescription, "short description");
            assertEquals(station.beginDateTime, "begin date time");
            assertEquals(station.nickname, "");
            assertTrue(station.hasElevator);
            assertTrue(station.hasElevatorAlert);
            assertFalse(station.isFavorite);
        }
}