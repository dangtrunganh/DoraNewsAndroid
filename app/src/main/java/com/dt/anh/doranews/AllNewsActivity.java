package com.dt.anh.doranews;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import com.dt.anh.doranews.adapter.recyclerview.NewsAllAdapter2;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.fragment.CategoryFragment;
import com.dt.anh.doranews.model.result.articleresult.ArticleResult;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstRoot;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class AllNewsActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private NewsAllAdapter2 mNewsAllAdapter2;
    //    private ArrayList<Article> mArticles;
    private String slug = "";
    private String category = "";
    private ArticleResult articleResult;
    private ProgressDialog dialog;
    private boolean flagDataTransfer;
    private ArrayList<Article> mArticlesArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_news);
        flagDataTransfer = getDataTransfer();

        initViews();
    }

    private void initViews() {
        RecyclerView recyclerViewNews = findViewById(R.id.recycler_news_all);
//        recyclerViewNews.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManager);

        //category và slug luôn không bị null
        mNewsAllAdapter2 = new NewsAllAdapter2(mArticlesArrayList, this, category, slug, recyclerViewNews);
        recyclerViewNews.setAdapter(mNewsAllAdapter2);
        mNewsAllAdapter2.notifyDataSetChanged();

        mToolbar = findViewById(R.id.toolbar_detail_all_news);
        setSupportActionBar(mToolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (category.equals("")) {
            mToolbar.setTitle("Tin mới");
        } else {
            mToolbar.setTitle(category);
        }

        if (flagDataTransfer) {
            loadNews(ConstParamAPI.START_PAGE);
            mNewsAllAdapter2.setLoadMore(() -> {
                mArticlesArrayList.add(null);
                mNewsAllAdapter2.notifyItemInserted(mArticlesArrayList.size() - 1);
                new Handler().postDelayed(() -> {
                    mArticlesArrayList.remove(mArticlesArrayList.size() - 1);
                    mNewsAllAdapter2.notifyItemRemoved(mArticlesArrayList.size());
                    double thresHold = ConstParamAPI.ARTICLE_THRESHOLD_ALL_NEWS_ACT * 1.0;
                    int index = (int) (Math.ceil(mArticlesArrayList.size() / thresHold)) + 1;
//                    Log.e("MMM", String.valueOf(index));
                    if (index > 1) {
                        loadNews(index);
                    }
                }, 3000); // Time to load

            });
        }

    }

    public boolean getDataTransfer() {
        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra(CategoryFragment.PARAM_CATEGORY_NAME_ALL_NEWS);
            slug = intent.getStringExtra(CategoryFragment.PARAM_CATEGORY_SLUG_ALL_NEWS);
            return true;
        }
        //Mặc định, chống lỗi
        category = "";
        slug = "";
        return false;
    }

    private void loadNews(int numberPage) {
        if (numberPage == 1) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("Loading..");
            dialog.setCancelable(false);
            dialog.show();
        }

        String type = slug;
//        Log.e("TYPEEEE-slug", type);
        String page = String.valueOf(numberPage);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        //Khong hot
        Call<ArticleResult> call = apiService.getResultArticle(type, page, String.valueOf(ConstParamAPI.ARTICLE_THRESHOLD_ALL_NEWS_ACT));

        call.enqueue(new Callback<ArticleResult>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResult> call, @NonNull Response<ArticleResult> response) {
                articleResult = response.body();
                if (articleResult == null) {
                    Toast.makeText(getApplicationContext(), "Failed to load data articles", Toast.LENGTH_SHORT).show();
                    if (numberPage == 1) {
                        dialog.dismiss();
                    }
                    return;
                }

                if (articleResult.getArticles().size() == 0) {
                    return;
                }

                if (articleResult.getArticles().size() < ConstParamAPI.ARTICLE_THRESHOLD_ALL_NEWS_ACT) {
                    mNewsAllAdapter2.setFlagLoadContinue(true);
                }
                mNewsAllAdapter2.updateListArticles(articleResult.getArticles());
                if (numberPage == 1) {
                    dialog.dismiss();
                }
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResult> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure articles", Toast.LENGTH_SHORT).show();
                if (numberPage == 1) {
                    dialog.dismiss();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
