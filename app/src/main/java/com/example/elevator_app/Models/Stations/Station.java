package com.example.elevator_app.Models.Stations;

import com.example.elevator_app.Models.Alerts.ElevatorAlert;

import java.io.Serializable;
import java.util.ArrayList;

public class Station implements Serializable {
    private final String name;
    private final boolean hasElevator;
    private final ArrayList<ElevatorAlert> alerts;
    private final boolean[] routes;

    public Station(String name, boolean hasElevator, boolean[] routes){
        this.name = name;
        this.hasElevator = hasElevator;
        this.routes = routes;
        alerts = new ArrayList<>();
    }

    public String getName(){ return name; }
    public boolean getElevator(){ return hasElevator; }
    public ArrayList<ElevatorAlert> getAlerts(){ return alerts; }
    public boolean[] getRoutes(){ return routes; }
    public void clearAlerts(){ alerts.clear(); }
    public void addAlert(ElevatorAlert e){ alerts.add(e); }
}
