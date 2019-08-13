package com.github.cta_elevator_alerts.utils;

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

/**
 * Pushes notification when station goes up or down.
 *
 * @author Southport Developers (Sam Siner & Tyler Arndt)
 */

public class NotificationPusher {

    public static void createAlertNotifications(Context context, ArrayList<String> pastAlerts, ArrayList<String> currAlerts){
        StationRepository repository = StationRepository.getInstance((Application)context.getApplicationContext());

        Log.d("NotificationPusher", "Past Alerts: " + pastAlerts.toString());
        Log.d("NotificationPusher", "Curr Alerts: " + currAlerts.toString());

        //Create notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channelOut = new NotificationChannel("out", "Elevators Out", NotificationManager.IMPORTANCE_HIGH);
            channelOut.enableVibration(true);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channelOut);

            //Create notification builder
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "out")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setAutoCancel(true);

            //Elevators newly working
            for (String s : pastAlerts){
                if (!currAlerts.contains(s)){
                    //Create notification tap action
                    Intent intent = new Intent(context, DisplayAlertActivity.class);
                    intent.putExtra("stationID", s);

                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
                    stackBuilder.addNextIntentWithParentStack(intent);
                    int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
                    PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(uniqueInt, PendingIntent.FLAG_UPDATE_CURRENT);

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

            NotificationChannel channelWork = new NotificationChannel("working", "Elevators Working", NotificationManager.IMPORTANCE_HIGH);
            channelOut.enableVibration(true);
            notificationManager.createNotificationChannel(channelWork);

            //Create notification builder
            NotificationCompat.Builder builder2 = new NotificationCompat.Builder(context, "working")
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setPriority(NotificationCompat.PRIORITY_MAX);

            //Elevators newly out
            for (String s2 : currAlerts){
                if (!pastAlerts.contains(s2)){
                    //Create notification tap action
                    Intent intent2 = new Intent(context, DisplayAlertActivity.class);
                    intent2.putExtra("stationID", s2);
                    TaskStackBuilder stackBuilder2 = TaskStackBuilder.create(context);
                    stackBuilder2.addNextIntentWithParentStack(intent2);

                    int uniqueInt2 = (int) (System.currentTimeMillis() & 0xfffffff);
                    PendingIntent resultPendingIntent2 = stackBuilder2.getPendingIntent(uniqueInt2, PendingIntent.FLAG_UPDATE_CURRENT);

                    builder2.setContentIntent(resultPendingIntent2)
                            .setPriority(NotificationCompat.PRIORITY_MAX)
                            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                            .setSmallIcon(R.drawable.elevate_logo_small)
                            .setColor(ContextCompat.getColor(context, R.color.colorAndroidRed))
                            .setContentTitle("Elevator is down!")
                            .setContentText("Elevator at " + repository.mGetStationName(s2) + " is down!");

                    notificationManager.notify(Integer.parseInt(s2), builder2.build());
                }
            }
        }
    }
}
