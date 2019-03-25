package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dt.anh.doranews.adapter.recyclerview.EventAdapter;
import com.dt.anh.doranews.adapter.recyclerview.LongEventAdapter;
import com.dt.anh.doranews.adapter.recyclerview.NewsAdapter;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.fakedata.EventsFake;
import com.dt.anh.doranews.fakedata.LongEventFake;
import com.dt.anh.doranews.fakedata.NewsFake;
import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.LongEvent;
import com.dt.anh.doranews.model.News;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.model.result.eventdetailresult.EventDetailMainResult;
import com.dt.anh.doranews.model.result.eventresult.Datum;
import com.dt.anh.doranews.model.result.eventresult.EventResult;
import com.dt.anh.doranews.model.result.longevent.MainDetailLongEvent;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.ConstRoot;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String mId;
    private NewsAdapter mNewsAdapter;

    private LongEventAdapter mLongEventAdapter;
    private ImageView mImageViewCover;
    private TextView mTextTitleEvent, mTextNameCategory, mTextNumberNews, mTextLoadMoreNews;
    private EventDetailMainResult eventMainResult;
    private MainDetailLongEvent mainDetailLongEvent;
    private ProgressDialog dialog;

    private ArrayList<com.dt.anh.doranews.model.result.eventdetailresult.LongEvent> mLongEvents;
    private ArrayList<Article> mArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        initViews();
    }

    public void getDataTransfer() {
        mLongEvents = new ArrayList<>();
        mArticles = new ArrayList<>();
        Intent intent = getIntent();
        if (intent != null) {
            mId = intent.getStringExtra(ConstParamTransfer.PARAM_ID_EVENT);
        }



//        mEventAdapter.updateListEvents(EventsFake.getListEvent());
        //=====
        //load event
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);
        Log.e("mID=", mId);
        Call<EventDetailMainResult> call = apiService.getResultEventDetail(mId, ConstParamTransfer.ARTICLE);
//        Log.e("pppp=====", call.);

        call.enqueue(new Callback<EventDetailMainResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<EventDetailMainResult> call, Response<EventDetailMainResult> response) {
                eventMainResult = response.body();
                if (eventMainResult == null) {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(getApplicationContext()).load(eventMainResult.getImage())
                        .apply(new RequestOptions().override(400, 0).
                                placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background))
                        .into(mImageViewCover);
                mTextTitleEvent.setText(eventMainResult.getTitle());
                mTextNameCategory.setText(eventMainResult.getCategory().getName());
                mTextNumberNews.setText(eventMainResult.getNumArticles() + " bài báo / " + eventMainResult.getReadableTime());

                //Load Long Events
                //Dựa vào id
                String idLongEvent = eventMainResult.getLongEvent().getId();
                loadLongEvent(idLongEvent);


                //Load News
                mArticles = (ArrayList<Article>) eventMainResult.getArticles();
                mNewsAdapter.updateListEvents(mArticles);
                mNewsAdapter.setTitleEvent(eventMainResult.getTitle());


                //load LongEvents
//                String idLongEvent = eventMainResult.getLongEvent().getId();
            }

            @Override
            public void onFailure(Call<EventDetailMainResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                Log.e("FAIL", t.getMessage());
                Log.e("Fail2", t.toString());
                Log.e("Fail3", "=====");
//                t.printStackTrace();
            }
        });

    }

    private void initViews() {
        //====
        mImageViewCover = findViewById(R.id.image_cover_detail_event);
        mTextTitleEvent = findViewById(R.id.text_title_detail_event);
        mTextNameCategory = findViewById(R.id.text_category_detail_event);
        mTextNumberNews = findViewById(R.id.text_number_news_detail_event);
        mTextLoadMoreNews = findViewById(R.id.text_load_more_news);
        mTextLoadMoreNews.setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar_detail_event);
        collapsingToolbarLayout = findViewById(R.id.collab_toolbar_layout_detail_event);

        setSupportActionBar(mToolbar);
        try {
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        collapsingToolbarLayout.setTitleEnabled(false);
        this.setTitle("");

        //=================
        //recyclerview
        mNewsAdapter = new NewsAdapter(new ArrayList<Article>(), this, mTextLoadMoreNews);
        RecyclerView recyclerViewNews = findViewById(R.id.recycler_news_detail_event);
        recyclerViewNews.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManager);
        recyclerViewNews.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();

        //=====


        RecyclerView recyclerViewLongEvent = findViewById(R.id.recycler_long_events);
        recyclerViewLongEvent.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerLongEvent = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewLongEvent.setLayoutManager(linearLayoutManagerLongEvent);
        mLongEventAdapter = new LongEventAdapter(new ArrayList<com.dt.anh.doranews.model.result.longevent.Datum>(), this, mId);
        recyclerViewLongEvent.setAdapter(mLongEventAdapter);
        mLongEventAdapter.notifyDataSetChanged();

        //=====


        getDataTransfer();
//        loadNews();
//        loadLongEvent();
    }


    private void loadLongEvent(String idLongEvent) {
        //Dựa vào idLongEvent, load ra list event thuộc long event
        mLongEventAdapter.setIdCurrentEvent(mId);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);
        Call<MainDetailLongEvent> call = apiService.getResultLongEvent(idLongEvent);

        call.enqueue(new Callback<MainDetailLongEvent>() {
            @Override
            public void onResponse(Call<MainDetailLongEvent> call, Response<MainDetailLongEvent> response) {
                mainDetailLongEvent = response.body();
                if (mainDetailLongEvent == null) {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
//                mDatumArrayList = (ArrayList<Datum>) eventResult.getData();
//                Log.e("API Datum event===", mDatumArrayList.toString());
//                setUpAdapter(mCategoryListTest);
                mLongEventAdapter.updateListLongEvents(mainDetailLongEvent.getData());
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<MainDetailLongEvent> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        //====


//        mLongEventAdapter.updateListLongEvents(LongEventFake.getListLongEvent());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.text_load_more_news:
                mNewsAdapter.loadMoreNews();
                break;
            default:
                break;
        }
    }

//    private void loadNews() {
//        mNewsAdapter.updateListEvents(NewsFake.getListNews());
//    }
}
