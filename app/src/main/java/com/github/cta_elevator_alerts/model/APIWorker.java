package com.github.cta_elevator_alerts.model;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

/**
 * Worker to update data every 15 minutes.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 *
 */

public class APIWorker extends Worker {

    public APIWorker(@NonNull Context context, @NonNull WorkerParameters params){
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork(){
        return Result.success();
    }
}
