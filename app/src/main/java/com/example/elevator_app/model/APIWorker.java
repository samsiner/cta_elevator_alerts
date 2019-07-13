package com.example.elevator_app.model;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

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
