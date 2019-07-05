package com.example.elevator_app.Models.Alerts;

import com.example.elevator_app.HttpsRequest.CreateAlerts;
import com.example.elevator_app.Models.Stations.Station;

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
