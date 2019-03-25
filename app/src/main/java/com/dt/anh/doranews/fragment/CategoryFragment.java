package com.dt.anh.doranews.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
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
import com.dt.anh.doranews.util.ConstRoot;

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
    private ProgressDialog dialog;
    private RecyclerView recyclerViewEvents;

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

    @Override
    protected void initializeComponents() {
        View view = getView();
        if (view == null) {
            return;
        }
        recyclerViewEvents = getView().findViewById(R.id.recycler_event);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL, false);
        linearLayoutManager.setAutoMeasureEnabled(true);
        recyclerViewEvents.setLayoutManager(linearLayoutManager);
        recyclerViewEvents.setNestedScrollingEnabled(false);
        recyclerViewEvents.setHasFixedSize(false);

        NestedScrollView nestedScrollView = getView().findViewById(R.id.nested_scroll_view);
        mEventAdapter = new EventAdapter(mDatumArrayList, getActivity(), recyclerViewEvents, nestedScrollView);
        recyclerViewEvents.setAdapter(mEventAdapter);
        loadEvent(1);


        Log.e("MaxNumber", maxNumber + "");
        mEventAdapter.setLoadMore(() -> {
//            if(mDatumArrayList.size() <= maxNumber) {
            mDatumArrayList.add(null);
            mEventAdapter.notifyItemInserted(mDatumArrayList.size() - 1);
            new Handler().postDelayed(() -> {
                mDatumArrayList.remove(mDatumArrayList.size() - 1);
                mEventAdapter.notifyItemRemoved(mDatumArrayList.size());

                int index = (int) (Math.ceil(mDatumArrayList.size() / 10.0)) + 1;
                if (index > 1) {
                    Log.e("====", "+=====+");
                    Log.e("size = ", mDatumArrayList.size() + "");
                    Log.e("Index = ", index + "");
                    loadEvent(index);
                }

            }, 2000); // Time to load
//            } else {
//                Log.e("xxx", "Load data completed!");
//            }

        });


        //==============
        RecyclerView recyclerViewNews = getView().findViewById(R.id.recycler_news_frg_event);
        recyclerViewNews.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManagerX = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerViewNews.setLayoutManager(linearLayoutManagerX);

        recyclerViewNews.setAdapter(mArticleAdapter);
        mArticleAdapter.notifyDataSetChanged();
        loadNews();

        TextView tvViewAll = getView().findViewById(R.id.tv_all_news_frg_event);
        tvViewAll.setOnClickListener(this);
    }

    private void loadNews() {
        String type = getType();
//        Log.e("TYPEEEE", type);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        //Khong hot
        //Mặc định load 1 trang đầu thôi, vì đây là vuốt ngang
        Call<ArticleResult> call = apiService.getResultArticle(type, "1");

        call.enqueue(new Callback<ArticleResult>() {
            @Override
            public void onResponse(Call<ArticleResult> call, Response<ArticleResult> response) {
                articleResult = response.body();
                if (articleResult == null) {
                    Toast.makeText(getContext(), "Failed to load data articles", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                    return;
                }
                mArticleArrayList = (ArrayList<Article>) articleResult.getArticles();
                Log.e("API article===", mArticleArrayList.toString());
//                setUpAdapter(mCategoryListTest);
                mArticleAdapter.updateListArticles(mArticleArrayList);
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<ArticleResult> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load data - onFailure articles", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });

    }

    private String getType() {
        if (getArguments() == null || getArguments().getString(PARAM_CATEGORY_SLUG) == null) {
            return EventType.SOCIAL;
        }
        String result = getArguments().getString(PARAM_CATEGORY_SLUG);
        Log.e("getTypeSlug category: ", result);
        return result;
    }

    private String getCategory() {
        if (getArguments() == null || getArguments().getString(PARAM_CATEGORY_NAME) == null) {
            return EventType.SOCIAL;
        }
        String result = getArguments().getString(PARAM_CATEGORY_NAME);
        Log.e("getName category: ", result);
        return result;
    }

    private boolean getIsHot() {
        //true neu la event hot, false neu khong la event hot
        if (getArguments() == null) {
            return false;
        }
        boolean result = getArguments().getBoolean(PARAM_HOT_EVENT);
        Log.e("getIsHot category: ", result + "");
        return result;
    }

    private void loadEvent(int numberPage) {
        //Dựa vào getType() đưa ra load loại event tương ứng
        String type = getType();
//        mEventAdapter.updateListEvents(EventsFake.getListEvent());
        //=====
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ConstRoot.URL_GET_ROOT)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ServerAPI apiService = retrofit.create(ServerAPI.class);

        if (!getIsHot()) {
            //Khong hot
            String page = String.valueOf(numberPage);
            Call<EventResult> call = apiService.getResultEvent(type, page);

            call.enqueue(new Callback<EventResult>() {
                @Override
                public void onResponse(Call<EventResult> call, Response<EventResult> response) {
                    eventResult = response.body();
                    if (eventResult == null) {
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    mDatumArrayList.addAll(eventResult.getData());
                    Log.e("Datum event not hot ==", mDatumArrayList.toString());
//                setUpAdapter(mCategoryListTest);
                    if (eventResult.getData().size() == 0) {
                        return;
                    }

                    if (eventResult.getData().size() < EventAdapter.VISIBLE_THRESHOLD) {
                        mEventAdapter.setFlagLoadContinue(true);
                    }
                    mEventAdapter.updateListEvents(eventResult.getData());
//                    recyclerViewEvents.addOnScrollListener();
//                    recyclerViewEvents.findViewHolderForAdapterPosition().itemView.
                }

                @Override
                public void onFailure(Call<EventResult> call, Throwable t) {
                    Toast.makeText(getContext(), "Failed to load data - onFailure", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            //hot
            String page = String.valueOf(numberPage);
            Call<EventResult> call = apiService.getHotEvent("true", page);

            call.enqueue(new Callback<EventResult>() {
                @Override
                public void onResponse(Call<EventResult> call, Response<EventResult> response) {
                    eventResult = response.body();
                    if (eventResult == null) {
                        Toast.makeText(getContext(), "Failed to load data", Toast.LENGTH_SHORT).show();
                        return;
                    }
//                    mDatumArrayList.addAll(eventResult.getData());
                    maxNumber = eventResult.getPaging().getTotal();
                    Log.e("API Datum event hot===", mDatumArrayList.toString());
//                setUpAdapter(mCategoryListTest);
                    if (eventResult.getData().size() == 0) {
                        return;
                    }
                    if (eventResult.getData().size() < 10) {
                        mEventAdapter.setFlagLoadContinue(true);
                    }
                    mEventAdapter.updateListEvents(eventResult.getData());
                }

                @Override
                public void onFailure(Call<EventResult> call, Throwable t) {
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
        dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Loading..");
        dialog.setCancelable(false);
        dialog.show();
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
