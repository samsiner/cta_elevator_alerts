package com.github.cta_elevator_alerts.workers;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
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

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("worker", "Worker", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            //Create notification builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(),"worker")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            //Create notification tap action
            Intent intent = new Intent(getApplicationContext(), DisplayAlertActivity.class);
            intent.putExtra("stationID", Integer.toString(41140));
            TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
            stackBuilder.addNextIntentWithParentStack(intent);
            PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            builder.setContentIntent(resultPendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setAutoCancel(true)
                    .setSmallIcon(R.drawable.elevate_logo_small)
                    .setColor(ContextCompat.getColor(getApplicationContext(), R.color.colorAndroidRed))
                    .setContentTitle("TEST!")
                    .setContentText("Click for King Drive Alert");

            notificationManager.notify(0, builder.build());
        }
        return Result.success();
    }
}
