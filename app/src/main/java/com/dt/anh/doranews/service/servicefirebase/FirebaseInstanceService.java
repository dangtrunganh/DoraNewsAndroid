package com.dt.anh.doranews.service.servicefirebase;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.dt.anh.doranews.DetailEventActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.google.firebase.messaging.RemoteMessage;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Map;
import java.util.Random;

public class FirebaseInstanceService extends FireBaseMessageService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
//        if (remoteMessage.getData().isEmpty()) {
//            Map<String, String> x = remoteMessage.getData();
//            Log.d("X1X-empty", x.toString());
//            showNotification(Objects.requireNonNull(remoteMessage.getNotification()).getTitle(),
//                    remoteMessage.getNotification().getBody());
//        } else {
//            Map<String, String> x = remoteMessage.getData();
//            Toast.makeText(this, "" + remoteMessage.getData().toString(), Toast.LENGTH_SHORT).show();
        Log.d("X1X-not-empty", remoteMessage.getData().toString());
        showNotification(remoteMessage.getData(), remoteMessage.getNotification());
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intent);
//        }

    }

    //Khi app đang chạy
    @SuppressLint("ShowToast")
    private void showNotification(Map<String, String> data, RemoteMessage.Notification dataNoticeTitleBody) {
        //Khi app đang chạy

        //======INTENT========
        //Bật app lên, pending intent
        Log.d("X1X", data.toString());
        String id = data.get("event_id");
        String idLongEvent = data.get("long_event_id");
        String urlImage = data.get("url_image");
        if (id == null || idLongEvent == null) {
            return;
        }
        if (id.equals("") || idLongEvent.equals("")) {
            return;

        }
        // Bật act đó lên
        // Create an Intent for the activity you want to start
        Intent intent = new Intent(this, DetailEventActivity.class);
        intent.putExtra(ConstParamTransfer.PARAM_ID_EVENT, id);
        intent.putExtra(ConstParamTransfer.PARAM_ID_LONG_EVENT, idLongEvent);

        // Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(intent);

        // Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        //======INTENT========


        //======TITLE_AND_CONTENT_NOTICE======
//        String title = data.get("title");
//        String body = data.get("body");
        String title = dataNoticeTitleBody.getTitle();
        String body = dataNoticeTitleBody.getBody();
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
                .setSmallIcon(R.drawable.icon_app_dora)
                .setContentTitle(title)
                .setContentText(body)
                .setContentInfo("Info")
                .setContentIntent(resultPendingIntent)
                /*.setSound(Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE
                        + "://" + this.getPackageName() + "/raw/audionotice"))*/;
        if (urlImage != null) {
            if (!urlImage.equals("")) {
                try {
                    notificationBuilder.setLargeIcon(Picasso.get().load(urlImage).get());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        String notice = "Title: " + title + ", body: " + body;
        Log.e("FB_FB", notice);
        assert notificationManager != null;
        Notification notification = notificationBuilder.build();
//        notification.sound = Uri.parse("android.resource://"
//                + this.getPackageName() + "/" + R.raw.audionotice);
        notificationManager.notify(new Random().nextInt(), notification);
    }

//    @SuppressLint("ShowToast")
//    private void showNotification(String title, String body) {
//        //Khi app đã đóng
//        NotificationManager notificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        String NOTIFICATION_CHANNEL_ID = "com.dt.anh.doranews.test";
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel notificationChannel = new NotificationChannel(
//                    NOTIFICATION_CHANNEL_ID, "Notification",
//                    NotificationManager.IMPORTANCE_DEFAULT
//            );
//            notificationChannel.setDescription("VTA Channel");
//            notificationChannel.enableLights(true);
//            notificationChannel.setLightColor(Color.BLUE);
//            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
//            notificationChannel.enableLights(true);
//            assert notificationManager != null;
//            notificationManager.createNotificationChannel(notificationChannel);
//        }
//
//        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,
//                NOTIFICATION_CHANNEL_ID);
//        notificationBuilder.setAutoCancel(true)
//                .setDefaults(Notification.DEFAULT_ALL)
//                .setWhen(System.currentTimeMillis())
//                .setSmallIcon(R.drawable.ic_notification)
//                .setContentTitle(title)
//                .setContentText(body)
//                .setContentInfo("Info");
//
//        String notice = "Title-2: " + title + ", body: " + body;
//        Log.e("FB_FB-2", notice);
//        assert notificationManager != null;
//        notificationManager.notify(new Random().nextInt(), notificationBuilder.build());
//    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d("TOKEN_FIREBASE", s);
    }
}
