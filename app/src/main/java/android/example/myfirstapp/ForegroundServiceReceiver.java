package android.example.myfirstapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.content.ContextCompat;

import static androidx.core.content.ContextCompat.startForegroundService;

public class ForegroundServiceReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context.getApplicationContext(), MyForegroundService.class);
        ContextCompat.startForegroundService(context.getApplicationContext(), serviceIntent);
    }
}
