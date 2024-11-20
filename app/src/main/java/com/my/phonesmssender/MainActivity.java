package com.my.phonesmssender;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.FirebaseApp;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_SMS_PERMISSION = 1;

    private TextView tokenTextView;
    private Button copyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tokenTextView = findViewById(R.id.tokenTextView);
        copyButton = findViewById(R.id.copyButton);
        copyButton.setEnabled(false); // Disable the button until token is fetched

        requestSMSPermissionIfNeeded();

        FirebaseApp.initializeApp(this);

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Log.d("FCMToken", "FCM Token: " + token);
                tokenTextView.setText(token);
                copyButton.setEnabled(true);

                copyButton.setOnClickListener(v -> {
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("FCM Token", token);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(MainActivity.this, "Token copied to clipboard", Toast.LENGTH_SHORT).show();
                });
            } else {
                Log.d("FCMToken", "Fetching FCM token failed", task.getException());
                tokenTextView.setText("Failed to fetch FCM Token");
            }
        });
    }

    private void requestSMSPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, REQUEST_CODE_SMS_PERMISSION);
        } else {
            Toast.makeText(this, "SMS permission already granted", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "SMS Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "SMS Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
