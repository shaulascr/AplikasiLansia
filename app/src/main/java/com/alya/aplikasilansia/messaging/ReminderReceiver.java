package com.alya.aplikasilansia.messaging;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.alya.aplikasilansia.MainActivity;
import com.alya.aplikasilansia.R;
public class ReminderReceiver extends BroadcastReceiver {
    public static final String ACTION_REMINDER = "com.alya.aplikasilansia.ACTION_REMINDER";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null && ACTION_REMINDER.equals(intent.getAction())) {
            String title = intent.getStringExtra("title");
            String description = intent.getStringExtra("desc");

            if (title != null && description != null) { // Check for nullability
                Intent notificationIntent = new Intent(context, MainActivity.class);
                notificationIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default")
                        .setSmallIcon(R.drawable.ic_notification)
                        .setContentTitle(title)
                        .setContentText(description)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(0, builder.build());
            } else {
                Log.w("ReminderReceiver", "Null title or description received");
            }
        } else {
            // Log or handle the case where the action is not as expected
            Log.w("ReminderReceiver", "Unexpected or null intent action: " + (intent != null ? intent.getAction() : "null"));
        }
    }
}

