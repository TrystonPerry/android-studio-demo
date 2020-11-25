package android.example.myfirstapp;

import me.pushy.sdk.Pushy;

import android.content.Intent;
import android.graphics.Color;
import android.content.Context;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.net.Uri;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.concurrent.atomic.AtomicInteger;

public class PushReceiver extends BroadcastReceiver {
    private NotificationID notificationID = new NotificationID();

    @Override
    public void onReceive(Context context, Intent intent) {
        String notificationTitle = "MyApp";
        String notificationText = "Test notification";

        // Attempt to extract the "message" property from the payload: {"message":"Hello World!"}
        if (intent.getStringExtra("message") != null) {
            notificationText = intent.getStringExtra("message");
        }

        // Prepare a notification with vibration, sound and lights
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "ALARMS")
                .setAutoCancel(true)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setLights(Color.RED, 1000, 1000)
                .setVibrate(new long[]{0, 400, 250, 400})
                .setContentIntent(PendingIntent.getActivity(context, 0, new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT));

        // Get an instance of the NotificationManager service
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);

        Log.d("TEST", "MESSAGE RECEIVED");

        // Build the notification and display it
        notificationManager.notify(notificationID.getID(), builder.build());
    }

    public class NotificationID {
        private final AtomicInteger c = new AtomicInteger(2);
        public int getID() {
            return c.incrementAndGet();
        }
    }
}