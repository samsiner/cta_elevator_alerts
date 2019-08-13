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

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.github.cta_elevator_alerts.R;
import com.github.cta_elevator_alerts.activities.DisplayAlertActivity;
import com.github.cta_elevator_alerts.model.StationRepository;

import java.util.ArrayList;

public class NotificationPusher {

    public static void createAlertNotifications(Context context, ArrayList<String> pastAlerts, ArrayList<String> currAlerts){
        StationRepository repository = StationRepository.getInstance((Application)context.getApplicationContext());

        Log.d("NotificationPusher", "Past Alerts: " + pastAlerts.toString());
        Log.d("NotificationPusher", "Curr Alerts: " + currAlerts.toString());

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("alerts", "New & Removed Alerts", NotificationManager.IMPORTANCE_HIGH);
            channel.enableVibration(true);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);

            //Create notification builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "worker")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            for (String s : pastAlerts){
                if (!currAlerts.contains(s)){
                    //Create notification tap action
                    Intent intent = new Intent(context, DisplayAlertActivity.class);
                    intent.putExtra("stationID", s);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(resultPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSmallIcon(R.drawable.elevate_logo_small)
                            .setColor(ContextCompat.getColor(context, R.color.colorAndroidGreen))
                            .setContentTitle("Elevator is working again!")
                            .setContentText("Elevator at " + repository.mGetStationName(s) + " is working again!");

                    notificationManager.notify(Integer.parseInt(s), builder.build());
                }
            }

            for (String s : currAlerts){
                if (!pastAlerts.contains(s)){
                    //Create notification tap action
                    Intent intent = new Intent(context, DisplayAlertActivity.class);
                    intent.putExtra("stationID", s);
                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(intent);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder.setContentIntent(resultPendingIntent)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSmallIcon(R.drawable.elevate_logo_small)
                            .setColor(ContextCompat.getColor(context, R.color.colorAndroidRed))
                            .setContentTitle("Elevator is down!")
                            .setContentText("Elevator at " + repository.mGetStationName(s) + " is down!");

                    notificationManager.notify(Integer.parseInt(s), builder.build());
                }
            }
        }
    }
}
