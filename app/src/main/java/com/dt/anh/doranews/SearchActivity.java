package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.Toast;

import com.dt.anh.doranews.adapter.recyclerview.NewsAllAdapter2;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.model.result.articleresult.ArticleResult;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstRoot;
import com.dt.anh.doranews.util.UtilTools;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity implements View.OnClickListener, SearchView.OnQueryTextListener {
    private SearchView searchView;
    private ImageView imageBack;
    private ArrayList<Article> mArticleArrayList;
    private ProgressDialog dialog;
    private RecyclerView mRecyclerViewArticles;
    private RecyclerView.LayoutManager layoutManager;
    private NewsAllAdapter2 mNewsAllAdapter2;
    private ArticleResult articleResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initViews();
    }

    private void initViews() {
        searchView = findViewById(R.id.sv_search_act);
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();

        imageBack = findViewById(R.id.img_back_search_act);
        imageBack.setOnClickListener(this);

        mRecyclerViewArticles = this.findViewById(R.id.rv_list_searched_articles);
//        lnlScheduling = (LinearLayout) view.findViewById(R.id.lnl_scheduling);
//        lnlScheduling.setOnClickListener(this);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
//        user = MainActivity.userMainAct;

        String category_slug = "phap-luat";
        String category = "Pháp luật";
        mArticleArrayList = new ArrayList<>();
        mNewsAllAdapter2 = new NewsAllAdapter2(mArticleArrayList, this, category, category_slug, mRecyclerViewArticles);
        mRecyclerViewArticles.setAdapter(mNewsAllAdapter2);
        layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerViewArticles.setLayoutManager(layoutManager);
        mNewsAllAdapter2.notifyDataSetChanged();


        searchView.setOnQueryTextListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_search_act:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onQueryTextSubmit(String newText) {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        if (!newText.equals("")) {
            search(newText, 1);
        }
        return false;
    }


    @Override
    public boolean onQueryTextChange(String newText) {
//        if (!newText.equals("")) {
//            search(newText, 1);
//        } else {
//            String category_slug = "phap-luat";
//            String category = "Pháp luật";
//            mArticleArrayList = new ArrayList<>();
//            mNewsAllAdapter2 = new NewsAllAdapter2(mArticleArrayList, this, category, category_slug, mRecyclerViewArticles);
//            mRecyclerViewArticles.setAdapter(mNewsAllAdapter2);
//            layoutManager
//                    = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            mRecyclerViewArticles.setLayoutManager(layoutManager);
//            mNewsAllAdapter2.notifyDataSetChanged();
//
//        }
        return false;
    }

    private void search(String keyWord, int numberPage) {
        String type = UtilTools.convertStringToSlug(keyWord); //slug
        if (type == null) {
            Toast.makeText(this, "Not a category", Toast.LENGTH_SHORT).show();
            return;
        }

        if (type.equals("")) {
            Toast.makeText(this, "Not a category", Toast.LENGTH_SHORT).show();
            return;
        }

        mNewsAllAdapter2.setSlugCategory(keyWord);
        mNewsAllAdapter2.setTitleCategory(keyWord);
        if (numberPage == 1) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();
        }
        mArticleArrayList = new ArrayList<>();
        mNewsAllAdapter2.resetListArticle();
        Log.e("TYPEEEE-slug", type);
        String page = String.valueOf(numberPage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        //Khong hot
        Call<ArticleResult> call = apiService.getResultArticle(type, page);

        call.enqueue(new Callback<ArticleResult>() {
            @Override
            public void onResponse(Call<ArticleResult> call, Response<ArticleResult> response) {
                articleResult = response.body();
                if (articleResult == null) {
                    Toast.makeText(getApplicationContext(), "Failed to load data articles", Toast.LENGTH_SHORT).show();
                    if (numberPage == 1) {
                        dialog.dismiss();
                    }
                    return;
                }
//                mArticlesArrayList = (ArrayList<Article>) articleResult.getArticles();
//                Log.e("API article===", mArticlesArrayList.toString());
////                setUpAdapter(mCategoryListTest);
//                mNewsAllAdapter2.updateListArticles(mArticlesArrayList);

                if (articleResult.getArticles().size() == 0) {
                    return;
                }

                if (articleResult.getArticles().size() < NewsAllAdapter2.VISIBLE_THRESHOLD) {
                    mNewsAllAdapter2.setFlagLoadContinue(true);
                }
                mNewsAllAdapter2.updateListArticles(articleResult.getArticles());
                if (numberPage == 1) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<ArticleResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure articles", Toast.LENGTH_SHORT).show();
                if (numberPage == 1) {
                    dialog.dismiss();
                }
            }
        });
    }
}
