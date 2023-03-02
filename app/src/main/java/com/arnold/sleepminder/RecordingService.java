package com.arnold.sleepminder;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.arnold.sleepminder.activities.MainActivity;
import com.arnold.sleepminder.storage.FileHandler;

public class RecordingService extends Service {
    private final int ONGOING_NOTIFICATION_ID = 1;
    public static RecordingService instance;

    public RecordingService() {
        instance = this;
    }

    @Override
    public void onDestroy() {
        MyApplication.recorder.stop(MyApplication.context);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Show the tracking notification
        startForeground(ONGOING_NOTIFICATION_ID, getNotification());

        // Start tracking
        MyApplication.recorder.start(MyApplication.context, new FileHandler());

        return START_STICKY;
    }

    private Notification getNotification() {
        // Set up the notification
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "default")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("SleepMinder is active")
                        .setContentText("Sleep well :)");
        // Create an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, MainActivity.class);

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(MainActivity.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);

        builder.setProgress(0, 0, true);
        builder.setCategory(NotificationCompat.CATEGORY_SERVICE);

        builder.setOngoing(true);
        @SuppressLint("UnspecifiedImmutableFlag") PendingIntent stopTrackingIntent = PendingIntent.getService(
                this,
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.addAction(R.mipmap.ic_action_stop , "Stop tracking", stopTrackingIntent);

        return builder.build();
    }
}
