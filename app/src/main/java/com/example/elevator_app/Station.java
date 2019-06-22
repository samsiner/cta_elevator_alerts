package com.example.elevator_app;

import java.io.Serializable;
import java.util.ArrayList;

public class Station implements Serializable {
    private String name;
    private boolean hasElevator;
    private ArrayList<ElevatorAlert> alerts;
    private boolean[] routes;

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
