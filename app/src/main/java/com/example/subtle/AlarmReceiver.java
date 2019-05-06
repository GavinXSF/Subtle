package com.example.subtle;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {

        Toast.makeText(context, "I'm running", Toast.LENGTH_SHORT).show();

    }

}
