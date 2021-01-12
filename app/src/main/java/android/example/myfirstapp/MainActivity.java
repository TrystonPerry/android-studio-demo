package android.example.myfirstapp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.WallpaperInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;

import java.net.URL;

import me.pushy.sdk.Pushy;
import me.pushy.sdk.util.exceptions.PushyException;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "android.example.myfirstapp.MESSAGE";

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!Pushy.isRegistered(this)) {
            new RegisterForPushNotificationsAsync(this).execute();
        }
        setContentView(R.layout.activity_main);

        // Create notification channel if on Android OREO or above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel foregroundServiceChannel = new NotificationChannel("FOREGROUND_SERVICE", "Foreground Service", NotificationManager.IMPORTANCE_DEFAULT);
            Uri sound = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" + getPackageName() + "/" + R.raw.alarm1);
            AudioAttributes attributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ALARM)
                    .build();
            NotificationChannel alarmsChannel = new NotificationChannel("ALARMS", "Alarms", NotificationManager.IMPORTANCE_HIGH);
            alarmsChannel.setSound(sound, attributes);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(foregroundServiceChannel);
            notificationManager.createNotificationChannel(alarmsChannel);
        }

        Button button = (Button) findViewById(R.id.button_service_toggle);
        button.setText("Start service");
//
//        FirebaseMessaging.getInstance().getToken()
//                .addOnCompleteListener(new OnCompleteListener<String>() {
//                    @Override
//                    public void onComplete(@NonNull Task<String> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("TEST", "Fetching FCM registration token failed", task.getException());
//                            return;
//                        }
//
//                        // Get new FCM registration token
//                        String token = task.getResult();
//
//                        // Log and toast
//                        Log.d("TEST", token);
//                        Toast.makeText(getApplicationContext(), token, Toast.LENGTH_SHORT).show();
//
////                        ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
////                        ClipData clip = ClipData.newPlainText("User Token", token);
////                        clipboard.setPrimaryClip(clip);
//                    }
//                });
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.editText);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    public void incrementCount(View view) {
        count++;
        EditText editText = (EditText) findViewById(R.id.counter);
        editText.setText(Integer.toString(count));
    }

    public void sendToast(View view) {
        Context context = getApplicationContext();
        Toast toast = Toast.makeText(context, "TOAST!!!", Toast.LENGTH_SHORT);
        toast.show();
    }

    public void toggleService(View view) {
        Intent serviceIntent = new Intent(this, MyForegroundService.class);
        ContextCompat.startForegroundService(this, serviceIntent);

        Button button = (Button) findViewById(R.id.button_service_toggle);
        button.setText("End service");
    }

    private class RegisterForPushNotificationsAsync extends AsyncTask<Void, Void, Object> {
        Activity mActivity;

        public RegisterForPushNotificationsAsync(Activity activity) {
            this.mActivity = activity;
        }

        protected Object doInBackground(Void... params) {
            try {
                // Register the device for notifications
                String deviceToken = Pushy.register(getApplicationContext());

                // Registration succeeded, log token to logcat
                Log.d("Pushy", "Pushy device token: " + deviceToken);

                // Send the token to your backend server via an HTTP GET request
//                new URL("https://{YOUR_API_HOSTNAME}/register/device?token=" + deviceToken).openConnection();

                // Provide token to onPostExecute()
                return deviceToken;
            }
            catch (Exception exc) {
                // Registration failed, provide exception to onPostExecute()
                return exc;
            }
        }

        @Override
        protected void onPostExecute(Object result) {
            String message;

            // Registration failed?
            if (result instanceof Exception) {
                // Log to console
                Log.e("Pushy", result.toString());

                // Display error in alert
                message = ((Exception) result).getMessage();
            }
            else {
                message = "Pushy device token: " + result.toString() + "\n\n(copy from logcat)";
            }

            // Registration succeeded, display an alert with the device token
            new android.app.AlertDialog.Builder(this.mActivity)
                    .setTitle("Pushy")
                    .setMessage(message)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();

            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("User Token", result.toString());
            clipboard.setPrimaryClip(clip);
        }
    }
}