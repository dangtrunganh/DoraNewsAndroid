package com.dt.anh.doranews.service.servicefirebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class FirebaseInstanceService extends FireBaseMessageService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if (remoteMessage.getData().isEmpty()) {
            Map<String, String> x = remoteMessage.getData();
            showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),
                    remoteMessage.getNotification().getBody());
        } else {
            Map<String, String> x = remoteMessage.getData();
            Log.d("X1X", x.toString());
            String id = x.get("event_id");
            if (id == null) {
                Log.d("X1X", "error null!");
                return;
            }
            if (id.equals("")) {
                Log.d("X1X", "error empty!");
                return;

            }
            showNotification(remoteMessage.getData());

            //===
            // Bật act đó lên
            Intent intent = new Intent(this, DetailEventActivity.class);
            intent.putExtra(ConstParamTransfer.PARAM_ID_EVENT, id);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }

    }

    @SuppressLint("ShowToast")
    private void showNotification(Map<String, String> data) {
        //Khi app đang chạy
        String title = data.get("title");
        String body = data.get("body");

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.dt.anh.doranews.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("VTA Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");
        String notice = "Title: " + title + ", body: " + body;
        Log.e("FB_FB", notice);
        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    @SuppressLint("ShowToast")
    private void showNotification(String title, String body) {
        //Khi app đã đóng
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.dt.anh.doranews.test";

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID, "Notification",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationChannel.setDescription("VTA Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableLights(true);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
                NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info");

        String notice = "Title-2: " + title + ", body: " + body;
        Log.e("FB_FB-2", notice);
        assert notificationManager != null;
        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKEN_FIREBASE", s);
    }
}
