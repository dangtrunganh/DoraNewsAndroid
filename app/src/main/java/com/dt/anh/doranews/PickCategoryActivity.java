package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dt.anh.doranews.adapter.recyclerview.CategoryAdapter;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.result.CategoryAPI;
import com.dt.anh.doranews.util.ConstParamStorageLocal;
import com.dt.anh.doranews.util.ConstRoot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PickCategoryActivity extends AppCompatActivity implements View.OnClickListener {
    private Button btnPick;
    private TextView txtPickDone;
    private RecyclerView mRecyclerPick;
    private CategoryAdapter mCategoryAdapter;
    //    private List<Category> mCategoryList;
    private CategoryAPI mCategoryAPI;
    private ArrayList<Category> mCategoryListTest; //All from api
    private ArrayList<Category> mCategoryListInLocal;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_category);

        initViews();
    }

    private void initViews() {
        btnPick = findViewById(R.id.btn_pick_done);
        txtPickDone = findViewById(R.id.text_pick_done);
        mRecyclerPick = findViewById(R.id.recycler_pick_category);
        btnPick.setOnClickListener(this);

        setUpAdapter();
    }

    private void setUpAdapter() {
        mCategoryListTest = new ArrayList<>();
        mCategoryAdapter = new CategoryAdapter(mCategoryListTest, this, txtPickDone, btnPick);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        mRecyclerPick.setLayoutManager(mLayoutManager);
        mRecyclerPick.setAdapter(mCategoryAdapter);
        mCategoryAdapter.notifyDataSetChanged();
        loadCategories();
    }

    @SuppressLint("SetTextI18n")
    private void loadCategories() {
        //Dựa vào getType() đưa ra load loại event tương ứng
        mCategoryListInLocal = new ArrayList<>();
        boolean x = loadCategoriesInLocal();

        //Đoạn này logic lằng nhằng?
//        if (x) {
        //Tức là nếu truyền vào true, thì thực hiện update property boolean
        ArrayList<Category> hihi = getListCategoryFromAPI(loadCategoriesInLocal());
//        }
    }

    private boolean loadCategoriesInLocal() {
        //Đọc từ pre ra
        mCategoryListInLocal = getListCategoriesChosen();
        if (mCategoryListInLocal == null) {
            mCategoryListInLocal = null;
            return false;
        }
        if (mCategoryListInLocal.size() == 0) {
            mCategoryListInLocal = null;
            return false;
        }
        return true;
    }

    private ArrayList<Category> getListCategoriesChosen() {
        ArrayList<Category> categories = new ArrayList<>();
        SharedPreferences pre = getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
        String json = pre.getString(ConstParamStorageLocal.KEY_PREF_LIST_CATEGORY, ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT);
        if (json.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT)) {
//            Toast.makeText(this, "Nothing in pref", Toast.LENGTH_SHORT).show();
            return categories;
        }
        Gson gson = new Gson();
        categories = gson.fromJson(json, new TypeToken<List<Category>>() {
        }.getType());
        Log.e("TAG-ListCategories", categories.toString());
        return categories;
    }

    private ArrayList<Category> getListCategoryFromAPI(final boolean flag) {
        //flag = true: Đã có trong local list các category đã chọn
        //flag = false: Không có trong local
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);
        Call<CategoryAPI> call = apiService.getResultCategory();
        call.enqueue(new Callback<CategoryAPI>() {
            @Override
            public void onResponse(Call<CategoryAPI> call, Response<CategoryAPI> response) {
                mCategoryAPI = response.body();
                if (mCategoryAPI == null) {
//                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    return;
                }
                mCategoryListTest = mCategoryAPI.getArrayList();
                Log.e("API===", mCategoryListTest.toString());
//                setUpAdapter(mCategoryListTest);
//                Log.e("size")#64DD17
                if (flag) {
                    //flag = true: Đã có trong local list các category đã chọn
                    Log.e("ListTest.size()", mCategoryListTest.size() + "");
                    Log.e("ListLocal.size()", mCategoryListTest.size() + "");
                    int k = 0;
                    for (Category category : mCategoryListTest) {
                        for (Category categoryLocal : mCategoryListInLocal) {
                            k++;
                            Log.e("k=", String.valueOf(k));
//                            Log.e("i = ", String.valueOf(i));
//                            Log.e("j = ", String.valueOf(j));
                            if (category.getId().equals(categoryLocal.getId())) {
                                category.setSelected(true);
                                break;
                            }
                        }
                    }
                    mCategoryAdapter.getListCategoryChosen().clear();
                    mCategoryAdapter.setListCategoryChosen(mCategoryListInLocal);
                    btnPick.setEnabled(true);
                    btnPick.setText("OK");
                    btnPick.setBackgroundColor(0xff64DD17);
                    txtPickDone.setText("(Đã chọn " + mCategoryListInLocal.size() + "/" + mCategoryListTest.size() + ")");
                } else {
                    txtPickDone.setText("(Đã chọn 0" + "/" + mCategoryListTest.size() + ")");
                }

                mCategoryAdapter.updateListCategories(mCategoryListTest);
//                Log.e("1111", "kkkk");
            }

            @Override
            public void onFailure(Call<CategoryAPI> call, Throwable t) {
//                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
            }
        });
        return mCategoryListTest;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(PickCategoryActivity.this, MainActivity.class));
        finish();
    }

    @SuppressLint("ApplySharedPref")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_pick_done:
                //1. Lấy ra list các category đã chọn từ adapter
                List<Category> categoriesChosen = mCategoryAdapter.getListCategoryChosen();
                //2. Lưu vào trong share preference
                if (categoriesChosen == null) {
//                    Toast.makeText(this, "categoriesChosen is null", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (categoriesChosen.size() == 0) {
//                    Toast.makeText(this, "categoriesChosen size is zero", Toast.LENGTH_SHORT).show();
                    return;
                }
                //===
                //tạo đối tượng getSharedPreferences
                SharedPreferences pre = getSharedPreferences(ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
                //tạo đối tượng Editor để lưu thay đổi
                SharedPreferences.Editor editor = pre.edit();
                editor.clear();

                //Chuyển list về dạng json để lưu xuống
                Gson gson = new Gson();
                String json = gson.toJson(categoriesChosen);
                editor.putString(ConstParamStorageLocal.KEY_PREF_LIST_CATEGORY, json);
                //chấp nhận lưu xuống file
                editor.commit();
                editor.apply();
                //3. Navigate tới màn hình MainActivity, trong MainActivity sẽ đọc trc tiên danh sách category này trong
                //share preference để load lên.
                startActivity(new Intent(PickCategoryActivity.this, MainActivity.class));
                finish();
                break;
            default:
                break;
        }
    }
}
