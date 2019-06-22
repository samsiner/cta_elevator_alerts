package com.example.elevator_app;

import org.junit.Test;

import java.util.ArrayList;

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
        ArrayList<String> arrl = new ArrayList<>();
        arrl.add("str1");
        arrl.add("str2");

        Station newStation = new Station( "name", true, arrl);

        assertEquals(newStation.getName(), "name");
        assertEquals(newStation.getElevator(), true);
        ArrayList<String> arrl2 = new ArrayList<>();
        arrl2.add("str1");
        arrl2.add("str2");
        assertEquals(newStation.getRoutes(), arrl2);
        assertEquals(newStation.getAlerts(), new ArrayList<ElevatorAlert>());

        newStation.addAlert(new ElevatorAlert("", "", "",
                ""));
        assertEquals(newStation.getAlerts().size(), 1);

        newStation.clearAlerts();
        assertEquals(newStation.getAlerts().size(), 0);
    }

    /*@Test
    public void testMainActivity(){
        //test


    }*/

}