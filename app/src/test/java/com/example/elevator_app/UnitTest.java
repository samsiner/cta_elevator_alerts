package com.example.elevator_app;

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
        assertEquals(newStation.getElevator(), true);

        boolean[] arrl2 = new boolean[9];
        Arrays.fill(arrl2, false);
        arrl2[1] = true;
        assertArrayEquals(newStation.getRoutes(), arrl2);
        assertEquals(newStation.getAlerts(), new ArrayList<ElevatorAlert>());

        newStation.addAlert(new ElevatorAlert("", "", "",
                ""));
        assertEquals(newStation.getAlerts().size(), 1);

        newStation.clearAlerts();
        assertEquals(newStation.getAlerts().size(), 0);
    }

    @Test
    public void testMainActivity(){
        MainActivity main = new MainActivity();



    }

}