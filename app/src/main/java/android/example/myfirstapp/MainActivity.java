package android.example.myfirstapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "android.example.myfirstapp.MESSAGE";

    private static int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}