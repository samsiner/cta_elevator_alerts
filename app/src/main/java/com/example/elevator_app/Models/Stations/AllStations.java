//TODO: Add JavaDoc comments to all classes

package com.example.elevator_app.Models.Stations;

import com.example.elevator_app.HttpsRequest.CreateStations;

import java.io.Serializable;
import java.util.HashMap;

public class AllStations implements Serializable {

    private CreateStations createStations;
    private HashMap<String, Station> allStations;

    public AllStations(){
        allStations = null;
        createStations = new CreateStations();
        allStations = createStations.buildStations();
    }

    public Station getStation(String s){
        try{
            return allStations.get(s);
        } catch (NullPointerException e){
            e.printStackTrace();
        }
        return null;
    }

    public HashMap<String, Station> getAllStations(){
        return allStations;
    }
}
