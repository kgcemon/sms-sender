package com.my.phonesmssender;

import android.telephony.SmsManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        String action = remoteMessage.getData().get("action");
        String number = remoteMessage.getData().get("number");
        String body = remoteMessage.getData().get("body");

        if ("send_sms_now".equals(action) && number != null && body != null) {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(number, null, body, null, null);
            Log.d("MyFirebaseMsgService", "SMS sent to: " + number);
        }
    }

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("firebaseToken", token);
    }
}
