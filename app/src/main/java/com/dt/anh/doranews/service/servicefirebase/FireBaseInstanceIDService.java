package com.dt.anh.doranews.service.servicefirebase;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FireBaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG = "FireBaseInstanceIDService";

    @SuppressLint("LongLogTag")
    @Override
    public void onTokenRefresh() {
        getToken();
//        Log.d(TAG, "Refreshed token: " + getToken());
    }

    public String getToken() {
        return FirebaseInstanceId.getInstance().getToken();   // Đăng ký token cho app từ Firebase
    }
}
