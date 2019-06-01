package com.example.elevator_app;

import java.util.*;

public class ElevatorAlert{

    private String headline = "", shortDesc = "", fullDesc = "", station = "";
    private ArrayList<String> routes;

    public ElevatorAlert(){
        routes = new ArrayList<>();
    }

    public void setHeadline(String headline){ this.headline = headline;};
    public void setShortDesc(String shortDesc){ this.shortDesc = shortDesc;};
    public void setFullDesc(String fullDesc){ this.fullDesc = fullDesc;};
    public void setStation(String station){ this.station = station;};
    public void addRoutes(String routesList){ routes.add(routesList);}

    public String getHeadline(){ return headline; }
    public String getShortDesc(){ return shortDesc; }
    public String getFullDesc(){ return fullDesc; }
    public String getStation(){ return station; }
    public ArrayList<String> getRoutes(){ return routes; }
}
