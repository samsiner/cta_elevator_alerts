package com.example.elevator_app;

import android.util.Log;

import com.example.elevator_app.Models.Alerts.AllAlerts;
import com.example.elevator_app.Models.Alerts.ElevatorAlert;
import com.example.elevator_app.Models.Lines.AllLines;
import com.example.elevator_app.Models.Stations.AllStations;
import com.example.elevator_app.Models.Stations.Station;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class UnitTest {

    @Test
    public void testElevatorAlertClass() {

        ElevatorAlert newAlert = new ElevatorAlert("headline",
                "short description", "full description",
                "begin date time");

        assertEquals(newAlert.getHeadline(), "headline");
        assertEquals(newAlert.getShortDesc(), "short description");
        assertEquals(newAlert.getFullDesc(), "full description");
        assertEquals(newAlert.getBeginDateTime(), "begin date time");
    }

    @Test
    public void testStationClass(){
        boolean[] arrl = new boolean[9];
        Arrays.fill(arrl, false);
        arrl[1] = true;

        Station newStation = new Station( "name", true, arrl);

        assertEquals(newStation.getName(), "name");
        assertTrue(newStation.getElevator());

        boolean[] arrl2 = new boolean[9];
        Arrays.fill(arrl2, false);
        arrl2[1] = true;
        assertArrayEquals(newStation.getRoutes(), arrl2);
        assertEquals(newStation.getAlerts(), new ArrayList<ElevatorAlert>());

        newStation.addAlert(new ElevatorAlert("Test Alert", "", "",
                ""));
        assertEquals(newStation.getAlerts().size(), 1);
        assertEquals(newStation.getAlerts().get(0).getHeadline(), "Test Alert");
        newStation.clearAlerts();
        assertEquals(newStation.getAlerts().size(), 0);
    }

    @Test
    public void testAllAlertsAllStationsClasses() {
        AllStations allStations = new AllStations();
        allStations.addStation("40780", "Central Park", true, new boolean[]{false, false, false, false, false, false, false, true, false});
        assertEquals(allStations.getAllStations().size(), 1);
        assertTrue(allStations.getAllStations().containsKey("40780"));
        assertEquals(allStations.getStation("40780").getName(), "Central Park");

        AllAlerts allAlerts = new AllAlerts(allStations.getAllStations());
        allAlerts.addAlert("40780", "Elevator at Central Park Temporarily Out-of-Service", "The elevator at Central Park (Pink Line) is temporarily out-of-service.", "{\"FullDescription\":{\"#cdata-section\":\"<p>The elevator at Central Park (Pink Line) is temporarily out-of-service.</p>\r\n\r\n<p>Our crews are working to assess the repair time for this elevator&mdash;this alert will be updated with additional information if/when we can estimate its return to service.</p>\r\n\r\n<p>" +
                        "Crews are working to return this elevator to service as quickly as possible. <a href=\"http://www.transitchicago.com/elevatorescalatorupgrades/\">Learn more about the work we do to maintain, repair and upgrade elevators.</a></p>\r\n\r\n<p>&nbsp;</p>\r\n\"}", "2019-06-07T19:05:00");
        assertEquals(allAlerts.getElevatorOutStationIDs().size(), 1);
        assertTrue(allAlerts.getElevatorOutStationIDs().contains("40780"));
        assertEquals(allStations.getStation("40780").getAlerts().size(),1);
        assertEquals(allStations.getStation("40780").getAlerts().get(0).getHeadline(), "Elevator at Central Park Temporarily Out-of-Service");
    }

    @Test
    public void testAllLinesClass(){
        AllLines allLines = new AllLines();
        assertArrayEquals(allLines.getLine("Yellow Line"), new String[]{"40140", "41680", "40900"});
    }
}