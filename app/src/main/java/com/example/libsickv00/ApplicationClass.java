package com.example.libsickv00;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class ApplicationClass extends Application {

    public static final String CHANNEL_ID = "channel1";
    final static String PLAY = "play";
    final static String NEXT = "next";
    final static String  PREVIOUS = "previous";
    final static String EXIT = "exit";


    public ApplicationClass(){

    }

    @Override
    public void onCreate() {
        super.onCreate();

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Now Playing Song", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Important channel for showing Notification");

            // Notification MANAGER
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            nm.createNotificationChannel(notificationChannel);
        }

    }
}
