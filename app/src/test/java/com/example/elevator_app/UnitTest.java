package com.example.elevator_app;

import com.example.elevator_app.Models.Alerts.ElevatorAlert;
import com.example.elevator_app.Models.Stations.AllStations;
import com.example.elevator_app.Models.Stations.Station;

import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URL;
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
    public void testAllStationsClass(){
        AllStations allStations = new AllStations();
        allStations.buildStations("{\\”CTAAlerts\\”:{\\”TimeStamp\\”:\\”2019-07-05T14:40:01\\”,\\”ErrorCode\\”:\\”0\\”,\\”ErrorMessage\\”:null,\\”Alert\\”:[{\\”AlertId\\”:\\”59806\\”,\\”Headline\\”:\\”Elevator at Central Park Temporarily Out-of-Service\\”,\\”ShortDescription\\”:\\”The elevator at Central Park (Pink Line) is temporarily out-of-service.\\”,\\”FullDescription\\”:{\\”#cdata-section\\”:\\”<p>The elevator at Central Park (Pink Line) is temporarily out-of-service.</p>\\r\\n\\r\\n<p>Our crews are working to assess the repair time for this elevator&mdash;this alert will be updated with additional information if/when we can estimate its return to service.</p>\\r\\n\\r\\n<p>Crews are working to return this elevator to service as quickly as possible. <a href=\\\\”http://www.transitchicago.com/elevatorescalatorupgrades/\\\\”>Learn more about the work we do to maintain, repair and upgrade elevators.</a></p>\\r\\n\\r\\n<p>&nbsp;</p>\\r\\n\\”},\\”SeverityScore\\”:\\”5\\”,\\”SeverityColor\\”:\\”000000\\”,\\”SeverityCSS\\”:\\”special-note\\”,\\”Impact\\”:\\”Elevator Status\\”,\\”EventStart\\”:\\”2019-06-07T19:05:00\\”,\\”EventEnd\\”:null,\\”TBD\\”:\\”1\\”,\\”MajorAlert\\”:\\”0\\”,\\”AlertURL\\”:{\\”#cdata-section\\”:\\”http://www.transitchicago.com/travel_information/alert_detail.aspx?AlertId=59806\\”},\\”ImpactedService\\”:{\\”Service\\”:[{\\”ServiceType\\”:\\”T\\”,\\”ServiceTypeDescription\\”:\\”Train Station\\”,\\”ServiceName\\”:\\”Central Park\\”,\\”ServiceId\\”:\\”40780\\”,\\”ServiceBackColor\\”:\\”e27ea6\\”,\\”ServiceTextColor\\”:\\”ffffff\\”,\\”ServiceURL\\”:{\\”#cdata-section\\”:\\”http://www.transitchicago.com/travel_information/station.aspx?StopId=32\\”}},{\\”ServiceType\\”:\\”R\\”,\\”ServiceTypeDescription\\”:\\”Train Route\\”,\\”ServiceName\\”:\\”Pink Line\\”,\\”ServiceId\\”:\\”Pink\\”,\\”ServiceBackColor\\”:\\”e27ea6\\”,\\”ServiceTextColor\\”:\\”ffffff\\”,\\”ServiceURL\\”:{\\”#cdata-section\\”:\\”http://www.transitchicago.com/pinkline/\\”}}]},\\”ttim\\”:\\”0\\”,\\”GUID\\”:\\”f034c751-a2b1-414b-84d2-0c94d5f9f171\\”}]}}");
        assertEquals(allStations.getAllStations().size(), 1);
    }
}