package com.dt.anh.doranews;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.dt.anh.doranews.model.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class EmptyActivity extends AppCompatActivity {
    public static final String TAG = EmptyActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        setUpData();
    }

    private void setUpData() {
        ArrayList<Category> categoryArrayList = getListCategoriesChosen();
        if (categoryArrayList == null) {
            Toast.makeText(this, "Nothing in pref", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        }
        else if (categoryArrayList.size() == 0) {
            Toast.makeText(this, "Nothing in pref", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(EmptyActivity.this, LoginActivity.class));
            finish();
        } else {
            startActivity(new Intent(EmptyActivity.this, MainActivity.class));
            finish();
        }
    }

    private ArrayList<Category> getListCategoriesChosen() {
        ArrayList<Category> categories = new ArrayList<>();
        SharedPreferences pre = getSharedPreferences
                (PickCategoryActivity.PREF_NAME, MODE_PRIVATE);
        String json = pre.getString("list_categories", "");
        if (json.equals("")) {
            Toast.makeText(this, "Nothing in pref", Toast.LENGTH_SHORT).show();
            return categories;
        }
        Gson gson = new Gson();
        categories = gson.fromJson(json, new TypeToken<List<Category>>(){}.getType());
        return categories;
    }
}
