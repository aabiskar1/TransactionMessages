package com.example.remindtopay;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class App extends Application {
    public static final String CHANNEL_1_ID = "exampleServiceChannel1";
    public static final String CHANNEL_2_ID = "exampleServiceChannel2";

    @Override
    public void onCreate() {
        super.onCreate();

        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name;
            NotificationChannel channel1 = new NotificationChannel(CHANNEL_1_ID,
                    "Example Service Channel1", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("This is notification channel 1");

            NotificationChannel channel2 = new NotificationChannel(CHANNEL_2_ID,
                    "Example Service Channel1", NotificationManager.IMPORTANCE_LOW);
            channel1.setDescription("This is notification channel 2");

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel1);
            manager.createNotificationChannel(channel2);

        }
    }

}
