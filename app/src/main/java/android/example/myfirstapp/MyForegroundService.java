package android.example.myfirstapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class MyForegroundService extends Service {
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 1, notificationIntent, 0);

        // Listen to pushy service
        Pushy.listen(this);

        // Create notification for foreground service
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = new NotificationCompat.Builder(this, "FOREGROUND_SERVICE")
                    .setContentTitle("My Background Service")
                    .setContentText("This is a background service")
                    .setSmallIcon(R.drawable.ic_android)
                    .build();

            startForeground(1, notification);
        }

        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void destroyService() {
        stopForeground(true);
        stopSelf();
    }
}
