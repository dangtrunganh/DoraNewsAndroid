package com.dt.anh.doranews;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.NestedScrollView;
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
import com.dt.anh.doranews.adapter.recyclerview.LongEventAdapter;
import com.dt.anh.doranews.adapter.recyclerview.NewsAdapter;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.model.result.eventdetailresult.EventDetailMainResult;
import com.dt.anh.doranews.model.result.longevent.MainDetailLongEvent;
import com.dt.anh.doranews.model.result.longevent.Datum;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.ConstRoot;
import com.dt.anh.doranews.util.UtilTools;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailEventActivity extends AppCompatActivity implements View.OnClickListener {
    private Toolbar mToolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private String mIdEvent;
    private NewsAdapter mNewsAdapter;

    private LongEventAdapter mLongEventAdapter;
    private ImageView mImageViewCover;
    private TextView mTextTitleEvent, mTextNameCategory, mTextNumberNews, mTextLoadMoreNews;
    private EventDetailMainResult eventMainResult;
    private MainDetailLongEvent mainDetailLongEvent;
    private ProgressDialog dialog;

    private ArrayList<Datum> mLongEvents = new ArrayList<>();
    private ArrayList<Article> mArticles = new ArrayList<>();

    private String mIdLongEvent;
    private RecyclerView recyclerViewLongEvent;

    private String uId;
    private TextView tvTitleLongEvent;

    private final String NUMBER_LONG_EVENT_PER_PAGE_STRING = String.valueOf(ConstParamAPI.NUMBER_LONG_EVENT_PER_PAGE);
//    private final int START_PAGE = 1;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_event);

        //Lấy uID từ local ra
        uId = UtilTools.getUId(DetailEventActivity.this);

        if (!getDataFromIntent()) {
            return;
        }

        initViews();
        loadListArticles();
        loadListLongEvent(mIdLongEvent, ConstParamAPI.START_PAGE);
        setUpLoadMore();
    }

    private void setUpLoadMore() {
        if (mLongEventAdapter.isFlagFinishLoadData()) {
            return;
        }
        //Sự kiện loadMore()
        mLongEventAdapter.setLoadMore(() -> {
            mLongEventAdapter.addItemLoadMore();
            new Handler().postDelayed(() -> {
                mLongEventAdapter.removeItemLoadMore();
                double thresHold = ConstParamAPI.NUMBER_LONG_EVENT_PER_PAGE * 1.0;
                int index = (int) (Math.ceil(mLongEvents.size() / thresHold)) + 1;
                if (index > 1 && (!mLongEventAdapter.isFlagFinishLoadData())) {
                    loadListLongEvent(mIdLongEvent, index);
                }
            }, 2000); // Time to load
        });
    }

    private boolean getDataFromIntent() {
        mIdEvent = getIdEvent();
        if (mIdEvent == null) {
            Toast.makeText(this, "Error: It dose not have mID in intent!", Toast.LENGTH_SHORT).show();
            return false;
        }

        mIdLongEvent = getIdLongEvent();
        if (mIdLongEvent == null) {
            Toast.makeText(this, "Error: It dose not have mIdLongEvent in intent!", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private String getIdEvent() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra(ConstParamTransfer.PARAM_ID_EVENT);
        } else {
            return null;
        }
    }

    private String getIdLongEvent() {
        Intent intent = getIntent();
        if (intent != null) {
            return intent.getStringExtra(ConstParamTransfer.PARAM_ID_LONG_EVENT);
        } else {
            return null;
        }
    }

    public void loadListArticles() {
        mLongEvents = new ArrayList<>();
        mArticles = new ArrayList<>();

        //=================
        //load event
        dialog.show();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ServerAPI apiService = retrofit.create(ServerAPI.class);

        Call<EventDetailMainResult> call = apiService.getResultEventDetail(mIdEvent, ConstParamTransfer.ARTICLE);

        call.enqueue(new Callback<EventDetailMainResult>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NonNull Call<EventDetailMainResult> call, @NonNull Response<EventDetailMainResult> response) {
                eventMainResult = response.body();
                if (eventMainResult == null) {
                    Toast.makeText(getApplicationContext(), "Error: Call API successfully, but data is null!", Toast.LENGTH_SHORT).show();
                    return;
                }
                Glide.with(getApplicationContext()).load(eventMainResult.getImage())
                        .apply(new RequestOptions().override(400, 0).
                                placeholder(R.drawable.image_default).error(R.drawable.image_default))
                        .into(mImageViewCover);
                mTextTitleEvent.setText(eventMainResult.getTitle());
                mTextNameCategory.setText(eventMainResult.getCategory().getName());
                mTextNumberNews.setText(eventMainResult.getNumArticles() + " bài báo / " + eventMainResult.getReadableTime());

                //Load into ListArticles
                mArticles = (ArrayList<Article>) eventMainResult.getArticles();
                mNewsAdapter.updateListEvents(mArticles);
                mNewsAdapter.setTitleEvent(eventMainResult.getTitle());
            }

            @Override
            public void onFailure(Call<EventDetailMainResult> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void initViews() {
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        if (getIdEvent() == null) {
            return;
        }

        //=================
        //All simple init view
        mImageViewCover = findViewById(R.id.image_cover_detail_event);
        mTextTitleEvent = findViewById(R.id.text_title_detail_event);
        mTextNameCategory = findViewById(R.id.text_category_detail_event);
        mTextNumberNews = findViewById(R.id.text_number_news_detail_event);
        mTextLoadMoreNews = findViewById(R.id.text_load_more_news);
        tvTitleLongEvent = findViewById(R.id.text_long_event_title);
        mTextLoadMoreNews.setOnClickListener(this);

        mToolbar = findViewById(R.id.toolbar_detail_event);
        setSupportActionBar(mToolbar);
        try {
            Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        collapsingToolbarLayout = findViewById(R.id.collab_toolbar_layout_detail_event);
        collapsingToolbarLayout.setTitleEnabled(false);
        this.setTitle("");
        collapsing();

        //=================
        //RecyclerView - List of events
        RecyclerView recyclerViewNews = findViewById(R.id.recycler_news_detail_event);
        mNewsAdapter = new NewsAdapter(mArticles, this, mTextLoadMoreNews);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManager);
        recyclerViewNews.setNestedScrollingEnabled(false);
        recyclerViewNews.setAdapter(mNewsAdapter);
        mNewsAdapter.notifyDataSetChanged();


        //=================
        //RecyclerView - List of long events
        recyclerViewLongEvent = findViewById(R.id.recycler_long_events);
        LinearLayoutManager linearLayoutManagerLongEvent = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        recyclerViewLongEvent.setLayoutManager(linearLayoutManagerLongEvent);
        recyclerViewLongEvent.setNestedScrollingEnabled(false);
        NestedScrollView nestedScrollViewLongEvent = findViewById(R.id.nested_scroll_view_detail_event);

        mLongEventAdapter = new LongEventAdapter(mLongEvents, this, mIdEvent, mIdLongEvent,
                recyclerViewLongEvent, nestedScrollViewLongEvent);
        mLongEventAdapter.notifyDataSetChanged();

        recyclerViewLongEvent.setAdapter(mLongEventAdapter);
    }

    private void collapsing() {
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        mToolbar.setNavigationOnClickListener(v -> finish());
    }

    private void loadListLongEvent(String idLongEvent, int numberPage) {
        //Dựa vào idLongEvent, load ra list event thuộc long event
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);
        String page = String.valueOf(numberPage);
        Call<MainDetailLongEvent> call = apiService.getResultLongEvent(idLongEvent, NUMBER_LONG_EVENT_PER_PAGE_STRING, page);

        call.enqueue(new Callback<MainDetailLongEvent>() {
            @Override
            public void onResponse(@NonNull Call<MainDetailLongEvent> call, @NonNull Response<MainDetailLongEvent> response) {
                mainDetailLongEvent = response.body();
                if (mainDetailLongEvent == null) {
                    Toast.makeText(getApplicationContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                if (mainDetailLongEvent.getData().size() == 0) {
                    dialog.dismiss();
                    return;
                }

                if (mainDetailLongEvent.getData().size() < ConstParamAPI.NUMBER_LONG_EVENT_PER_PAGE) {
                    //Khi data nhận về nhỏ có size nhỏ hơn Threshold thì chuyển flag = true, tức là sẽ không load tiếp nữa
                    //Nếu bằng thì tức là load đúng size buffer = 3, tức là vẫn còn mà
                    mLongEventAdapter.setFlagFinishLoadData(true);
                }
                //Kiểm tra xem list hiện tại có chứa ko, có rồi thì reject mnl
                boolean flag = true;

                for (Datum datum : mLongEvents) {
                    for (Datum datumCmp : mainDetailLongEvent.getData()) {
                        if (datum.getId().equals(datumCmp.getId())) {
                            flag = false;
                            break;
                        }
                    }
                    if (!flag) {
                        break;
                    }
                }
                if (!flag) {
                    return;
                }

//                if (mainDetailLongEvent.getData().size() == 1) {
//                    tvTitleLongEvent.setVisibility(View.GONE);
//                    recyclerViewLongEvent.setVisibility(View.GONE);
//                    dialog.dismiss();
//                    return;
//                }
                mLongEvents.addAll(mainDetailLongEvent.getData());
                mLongEventAdapter.updateListLongEvents(mainDetailLongEvent.getData());
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<MainDetailLongEvent> call, @NonNull Throwable t) {
                Toast.makeText(getApplicationContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
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
}
