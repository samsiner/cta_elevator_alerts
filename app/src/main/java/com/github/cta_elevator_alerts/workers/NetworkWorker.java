package com.github.cta_elevator_alerts.workers;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.github.cta_elevator_alerts.model.StationRepository;

/**
 * Worker to update data every 15 minutes.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class NetworkWorker extends Worker {

    private final StationRepository repository;

    public NetworkWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);
        repository = StationRepository.getInstance((Application)getApplicationContext());
    }

    @Override
    public @NonNull Result doWork(){
        try{
            Log.d("NetworkWorker", "Building Stations and Alerts");
            repository.buildStations();
            repository.buildAlerts();
        } catch (Exception e){
            Log.d("NetworkWorker", "Failed to build");
            e.printStackTrace();
            return Result.failure();
        }
        Log.d("NetworkWorker", "Succeeded in build");
        return Result.success();
    }
}
