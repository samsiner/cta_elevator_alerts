package com.example.elevator_app;

import java.util.ArrayList;
import java.util.HashMap;

public class AllAlerts {

    private CreateAlerts createAlerts;
    private ArrayList<String> elevatorOutStationIDs;

    public AllAlerts(HashMap<String, Station> a) {
        elevatorOutStationIDs = new ArrayList<>();
        createAlerts = new CreateAlerts(a);
        updateAlerts();
    }

    public void updateAlerts(){
        elevatorOutStationIDs = createAlerts.buildAlerts();
    }

    public ArrayList<String> getElevatorOutStationIDs(){
        return elevatorOutStationIDs;
    }
}
