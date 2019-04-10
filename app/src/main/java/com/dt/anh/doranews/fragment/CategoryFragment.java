package com.dt.anh.doranews.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.anh.doranews.AllNewsActivity;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.adapter.recyclerview.EventAdapter;
import com.dt.anh.doranews.adapter.recyclerview.ILoadMore;
import com.dt.anh.doranews.adapter.recyclerview.NewsInCategoryFrgAdapter;
import com.dt.anh.doranews.api.ServerAPI;
import com.dt.anh.doranews.fakedata.EventsFake;
import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.EventType;
import com.dt.anh.doranews.model.result.CategoryAPI;
import com.dt.anh.doranews.model.result.articleresult.ArticleResult;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.model.result.eventresult.Datum;
import com.dt.anh.doranews.model.result.eventresult.EventResult;
import com.dt.anh.doranews.util.ConstParamAPI;
import com.dt.anh.doranews.util.ConstRoot;
import com.dt.anh.doranews.util.UtilTools;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CategoryFragment extends BaseFragment implements View.OnClickListener {
    public static final String PARAM_CATEGORY_SLUG = "param_category_slug";
    public static final String PARAM_CATEGORY_NAME = "param_category_name";
    public static final String PARAM_HOT_EVENT = "param_hot";
    public static final String PARAM_CATEGORY_NAME_ALL_NEWS = "param_category_name_all_news";
    public static final String PARAM_CATEGORY_SLUG_ALL_NEWS = "param_slug_all_news";
    private EventAdapter mEventAdapter;
    private NewsInCategoryFrgAdapter mArticleAdapter;
    private EventResult eventResult;
    private ArticleResult articleResult;
    private ArrayList<Datum> mDatumArrayList = new ArrayList<>();
    private ArrayList<Article> mArticleArrayList = new ArrayList<>();
    int maxNumber = 0;
    //    1private ProgressDialog dialog;
    private RecyclerView recyclerViewEvents;
    private RecyclerView recyclerViewNews;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvViewAll;

    private TextView tvArticleTitle;
    private TextView tvEventTitle;
    private TextView tvTextNoNetwork;

    private ShimmerFrameLayout mShimmerViewContainer;

    //    private long oldTime = System.currentTimeMillis();
    private long oldTime = -1;

    public CategoryFragment() {
    }

    public static CategoryFragment newInstance(String categoryJsonString, String categoryName, boolean isHot) {
        CategoryFragment genreFragment = new CategoryFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_CATEGORY_SLUG, categoryJsonString);
        args.putString(PARAM_CATEGORY_NAME, categoryName);
        args.putBoolean(PARAM_HOT_EVENT, isHot);
        genreFragment.setArguments(args);
        return genreFragment;
        //Tham số là 1 list các category??
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVariable();
    }

    private void initializeVariable() {
        mDatumArrayList = new ArrayList<>();
        mArticleArrayList = new ArrayList<>();
        mArticleAdapter = new NewsInCategoryFrgAdapter(new ArrayList<>(), getActivity(), getType());
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getContext().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    protected void initializeComponents() {
        View view = getView();
        if (view == null) {
            return;
        }
        swipeContainer = view.findViewById(R.id.swipeContainer_frg_event);
        // Configure the refreshing colors
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        recyclerViewEvents = view.findViewById(R.id.recycler_event);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewEvents.setLayoutManager(linearLayoutManager);
        ViewCompat.setNestedScrollingEnabled(recyclerViewEvents, false);

        recyclerViewEvents.setNestedScrollingEnabled(false);
        recyclerViewEvents.setHasFixedSize(false);
//        recyclerViewEvents.= true;

        NestedScrollView nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        mEventAdapter = new EventAdapter(mDatumArrayList, getActivity(), recyclerViewEvents, nestedScrollView);
        recyclerViewEvents.setAdapter(mEventAdapter);

        //==============
        recyclerViewNews = view.findViewById(R.id.recycler_news_frg_event);
        recyclerViewNews.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManagerX);

        recyclerViewNews.setAdapter(mArticleAdapter);
        mArticleAdapter.notifyDataSetChanged();
        tvViewAll = view.findViewById(R.id.tv_all_news_frg_event);
        tvArticleTitle = view.findViewById(R.id.text_articles_title);
        tvEventTitle = view.findViewById(R.id.text_event_title);
        tvTextNoNetwork = view.findViewById(R.id.text_no_network);


        //==skeleton view===
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);

        loadDataMain();

        // Lookup the swipe container view
        // Setup refresh listener which triggers new data loading
        // Your code to refresh the list here.
        // Make sure you call swipeContainer.setRefreshing(false)
        // once the network request has completed successfully.
        swipeContainer.setOnRefreshListener(this::loadDataMain);
    }

    private void refreshData() {
        //11. Có thể không cần cho visible
//        mShimmerViewContainer.setVisibility(View.VISIBLE);
        mShimmerViewContainer.startShimmerAnimation();
        loadDataMain();
    }

    private void loadDataMain() {
        //load event
        if (isNetworkAvailable()) {
            // do network operation here
            //Co mang thi moi co loadmore
            tvArticleTitle.setVisibility(View.VISIBLE);
            tvEventTitle.setVisibility(View.VISIBLE);
            recyclerViewNews.setVisibility(View.VISIBLE);
            recyclerViewEvents.setVisibility(View.VISIBLE);
            tvTextNoNetwork.setVisibility(View.GONE);

            //display nút xem tất cả đi
            tvViewAll.setVisibility(View.VISIBLE);
            //===
            loadEvent(ConstParamAPI.START_PAGE); //1
            mEventAdapter.setLoadMore(() -> {
                if (mEventAdapter.isFlagLoadContinue()) {
                    return;
                }
                mDatumArrayList.add(null);
                mEventAdapter.notifyItemInserted(mDatumArrayList.size() - 1);
                new Handler().postDelayed(() -> {
                    mDatumArrayList.remove(mDatumArrayList.size() - 1);
                    mEventAdapter.notifyItemRemoved(mDatumArrayList.size());

                    double thresHold = ConstParamAPI.EVENT_THRESHOLD * 1.0;
                    int index = (int) (Math.ceil(mDatumArrayList.size() / thresHold)) + 1;
                    if (index > 1 && (!mEventAdapter.isFlagLoadContinue())) {
                        loadEvent(index);
                    }
                }, 2000); // Time to load
            });

            //load article
            loadNews();

            //Có mạng thì mới kích xem tất cả được
            tvViewAll.setOnClickListener(this);
        } else if (getIsHot()) {
            //Không có mạng thì ko có load more
            //Không có mạng với tabHost
            tvTextNoNetwork.setVisibility(View.GONE);
            tvViewAll.setVisibility(View.GONE);
            Toast.makeText(getContext(), "No Available Network. Offline mode is on!", Toast.LENGTH_LONG).show();
//            return;
            //Load local event
            ArrayList<Datum> arrayListEvent = UtilTools.getListEvent(Objects.requireNonNull(getContext()));
            if (arrayListEvent.size() != 0) {
                loadLocalEventToOfflineMode(arrayListEvent);
            }

            //Load local articles
            ArrayList<Article> articles = UtilTools.getListArticle(getContext());
            if (articles.size() != 0) {
                loadLocalArticleToOfflineMode(articles);
            }
            swipeContainer.setRefreshing(false);
            //1dialog.dismiss();
            //==skeleton-view===
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            //=====
        } else {
            //Không có mạng với các tab khác
            //Tạm thời disable hết các list + text view đi
            tvArticleTitle.setVisibility(View.GONE);
            tvEventTitle.setVisibility(View.GONE);
            recyclerViewNews.setVisibility(View.GONE);
            recyclerViewEvents.setVisibility(View.GONE);
            tvTextNoNetwork.setVisibility(View.VISIBLE);

            //display nút xem tất cả đi
            tvViewAll.setVisibility(View.GONE);
            swipeContainer.setRefreshing(false);
            //1dialog.dismiss();
            //==skeleton-view===
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            //=====
        }
    }

    private void loadLocalEventToOfflineMode(ArrayList<Datum> arrayList) {
        mEventAdapter.updateListEvents(arrayList);
    }

    private void loadLocalArticleToOfflineMode(ArrayList<Article> articles) {
        mArticleAdapter.updateListArticles(articles);
    }

    private String getType() {
        if (getArguments() == null || getArguments().getString(PARAM_CATEGORY_SLUG) == null) {
            return EventType.SOCIAL;
        }
        //        Log.e("getTypeSlug category: ", result);
        return getArguments().getString(PARAM_CATEGORY_SLUG);
    }

    private String getCategory() {
        if (getArguments() == null || getArguments().getString(PARAM_CATEGORY_NAME) == null) {
            return EventType.SOCIAL;
        }
        //        Log.e("getName category: ", result);
        return getArguments().getString(PARAM_CATEGORY_NAME);
    }

    private boolean getIsHot() {
        //true neu la event hot, false neu khong la event hot
        if (getArguments() == null) {
            return false;
        }
        return getArguments().getBoolean(PARAM_HOT_EVENT);
    }

    @Override
    public void onResume() {
        super.onResume();
        mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }

    private void loadNews() {
        String type = getType();
        Log.e("tt-type", type);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        //Khong hot thì type là ""
        //Mặc định load 1 trang đầu thôi, vì đây là vuốt ngang
//        String initPage = String.valueOf(ConstParamAPI.START_PAGE);
        Call<ArticleResult> call = apiService.getResultArticle(type,
                String.valueOf(ConstParamAPI.START_PAGE),
                String.valueOf(ConstParamAPI.ARTICLE_THRESHOLD_CATEGORY_FRG));

        call.enqueue(new Callback<ArticleResult>() {
            @Override
            public void onResponse(@NonNull Call<ArticleResult> call, @NonNull Response<ArticleResult> response) {
                articleResult = response.body();
                if (articleResult == null) {
                    Toast.makeText(getContext(), "Failed to load data articles", Toast.LENGTH_SHORT).show();

                    //==skeleton-view===
                    mShimmerViewContainer.stopShimmerAnimation();
                    mShimmerViewContainer.setVisibility(View.GONE);
                    //=====
//                    dialog.dismiss();
                    swipeContainer.setRefreshing(false);
                    return;
                }
                mArticleArrayList = (ArrayList<Article>) articleResult.getArticles();
                mArticleAdapter.updateListArticles(mArticleArrayList);

                if (getType().equals("")) {
                    //Lưu xuống cache
                    UtilTools.storeArticle(Objects.requireNonNull(getContext()), mArticleArrayList);
//                    Toast.makeText(getContext(), "Stored data article!", Toast.LENGTH_SHORT).show();
                }

//                dialog.dismiss();
                //==skeleton-view===
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                //=====
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ArticleResult> call, @NonNull Throwable t) {
//                1. dialog.dismiss();
                //==skeleton-view===
                mShimmerViewContainer.stopShimmerAnimation();
                mShimmerViewContainer.setVisibility(View.GONE);
                //=====
                swipeContainer.setRefreshing(false);
            }
        });

    }

    private void loadEvent(int numberPage) {
        //Dựa vào getType() đưa ra load loại event tương ứng
        String type = getType();
        //=====
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        if (!getIsHot()) {
            //Khong hot
            String page = String.valueOf(numberPage);
            Call<EventResult> call = apiService.getResultEvent(type, page, String.valueOf(ConstParamAPI.EVENT_THRESHOLD));

            call.enqueue(new Callback<EventResult>() {
                @Override
                public void onResponse(@NonNull Call<EventResult> call, @NonNull Response<EventResult> response) {
                    eventResult = response.body();
                    if (eventResult == null) {
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (eventResult.getData().size() == 0) {
                        return;
                    }

                    if (eventResult.getData().size() < ConstParamAPI.EVENT_THRESHOLD) {
                        mEventAdapter.setFlagLoadContinue(true);
                    }
                    mEventAdapter.updateListEvents(eventResult.getData());
                }

                @Override
                public void onFailure(@NonNull Call<EventResult> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //hot
            //Chỉ lưu hot xuống cache
            String page = String.valueOf(numberPage);
            Call<EventResult> call = apiService.getHotEvent("true", page, String.valueOf(ConstParamAPI.EVENT_THRESHOLD));

            call.enqueue(new Callback<EventResult>() {
                @Override
                public void onResponse(@NonNull Call<EventResult> call, @NonNull Response<EventResult> response) {
                    eventResult = response.body();
                    if (eventResult == null) {
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    maxNumber = eventResult.getPaging().getTotal();
                    if (eventResult.getData().size() == 0) {
                        return;
                    }
                    if (eventResult.getData().size() < ConstParamAPI.EVENT_THRESHOLD) {
                        mEventAdapter.setFlagLoadContinue(true);
                    }

                    //Thỏa mãn, lưu vào cache, với page == 1 thì lưu
                    if (numberPage == 1) {
                        UtilTools.storeEvent(Objects.requireNonNull(getContext()), eventResult.getData());
//                        Toast.makeText(getContext(), "Stored data event!", Toast.LENGTH_SHORT).show();
                    }
                    //===
                    mEventAdapter.updateListEvents(eventResult.getData());
                }

                @Override
                public void onFailure(@NonNull Call<EventResult> call, @NonNull Throwable t) {
                    Toast.makeText(getContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_event;
    }

    @Override
    protected void initProgressbar() {
//        1dialog = new ProgressDialog(getActivity());
//        1dialog.setMessage("Loading..");
//        1dialog.setCancelable(false);
//        1dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_all_news_frg_event:
                Intent intent = new Intent(getActivity(), AllNewsActivity.class);
                intent.putExtra(PARAM_CATEGORY_NAME_ALL_NEWS, getCategory());
                intent.putExtra(PARAM_CATEGORY_SLUG_ALL_NEWS, getType());
                Objects.requireNonNull(getActivity()).startActivity(intent);
                break;
            default:
                break;
        }
    }
}
