package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.result.userresult.UserResult;
import com.dt.anh.doranews.service.servicefirebase.FireBaseInstanceIDService;
import com.dt.anh.doranews.util.ConstParamStorageLocal;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.ConstRoot;
import com.dt.anh.doranews.util.UtilTools;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import android.provider.Settings.Secure;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class EmptyActivity extends AppCompatActivity {
    public static final String TAG = EmptyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);
        setUpData();
        setUpUserId();
    }

    private String getDeviceId() {
        @SuppressLint("HardwareIds") String android_id = Secure.getString(getContentResolver(),
                Secure.ANDROID_ID);
        Log.e("k-android_id", android_id);
        return android_id;
    }

    private String getTokenFirebase() {
        FireBaseInstanceIDService fireBaseInstanceIDService = new FireBaseInstanceIDService();
        String mToken = fireBaseInstanceIDService.getToken();
        Log.e("k-Token FireBase", mToken);
        return mToken;
    }

    private void sendInfoToServer() {
        String deviceId = getDeviceId();
        String tokenFb = getTokenFirebase();
        if (deviceId != null && tokenFb != null) {
            //Gửi thông tin lên server
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ConstRoot.URL_GET_ROOT_LOG_IN)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            ServerAPI apiService = retrofit.create(ServerAPI.class);
            RequestBody requestBody = new MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart("deviceid", deviceId)
                    .addFormDataPart("token", tokenFb)
                    .build();

            Call<UserResult> call = apiService.login(requestBody);
            call.enqueue(new Callback<UserResult>() {
                @Override
                public void onResponse(Call<UserResult> call, Response<UserResult> response) {
//                    ResponseBody responseBody = response.body();
                    UserResult userResult = response.body();
                    String uId = userResult.getUId();
                    if (uId == null) {
                        Log.e("su-login", "fail: uId null");
                    } else {
                        Log.e("su-login", "success: " + userResult.getUId());
                        //========
                        // store to local cache
                        UtilTools.storeUId(EmptyActivity.this, userResult.getUId());
                        // Sau đó thực hiện navigate sang màn hình MainActivity, nếu lần sau đã vào đc MainActivity rồi
                        // thì sure là đã login, đã lưu lại uId ở local
                        //========
                    }
                }

                @Override
                public void onFailure(Call<UserResult> call, Throwable t) {
                    Log.e("su-login", "fail: onFailure");
                    Toast.makeText(EmptyActivity.this, "Fail to login!", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void setUpUserId() {
        String uuId = UtilTools.getUId(EmptyActivity.this);
        String jsonCategory = UtilTools.getListCategoryInCache(EmptyActivity.this);
        if (uuId.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_KEY_UID_DEFAULT) &&
                jsonCategory.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT)) {
            //Tức là đang rỗng, chưa có uId trong SharedPreference
            sendInfoToServer();
        }
    }

    private void setUpData() {
        ArrayList<Category> categoryArrayList = getListCategoriesChosen();
        if (categoryArrayList == null) {
            //Nothing in pref tức là lần đầu mở app
//            sendInfoToServer();
            //======
            Toast.makeText(this, "Nothing in pref - null", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        } else if (categoryArrayList.size() == 0) {
            //Nothing in pref tức là lần đầu mở app
//            sendInfoToServer();
            //======
            Toast.makeText(this, "Nothing in pref - 0", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        } else {
            Intent intentResult = new Intent(EmptyActivity.this, MainActivity.class);
            //===getIntentExtras====
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String eventId = bundle.getString("event_id");
                String longEventId = bundle.getString("long_event_id");
                intentResult.putExtra(ConstParamTransfer.TRANSFER_EVENT_ID_FR_EMPTY_TO_MAIN, eventId);
                intentResult.putExtra(ConstParamTransfer.TRANSFER_LONG_EVENT_ID_FR_EMPTY_TO_MAIN, longEventId);
            }
            startActivity(intentResult);
            finish();
        }
    }

    private ArrayList<Category> getListCategoriesChosen() {
//        SharedPreferences pre = getSharedPreferences
//                (ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
//        String json = pre.getString(ConstParamStorageLocal.KEY_PREF_LIST_CATEGORY,
//                ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT);
        String json = UtilTools.getListCategoryInCache(EmptyActivity.this);
        if (json.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT)) {
            Toast.makeText(this, "Ep-Nothing in pref", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
        Gson gson = new Gson();
        return gson.fromJson(json, new TypeToken<List<Category>>() {
        }.getType());
    }
}
