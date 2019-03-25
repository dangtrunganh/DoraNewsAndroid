package com.dt.anh.doranews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btn = findViewById(R.id.btn_login_log_in);
        btn.setOnClickListener(view -> {
            startActivity(new Intent(LoginActivity.this, PickCategoryActivity.class));
//                LoginActivity.this.finish();
        });
    }
}
