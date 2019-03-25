package com.dt.anh.doranews.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.WebViewActivity;
import com.dt.anh.doranews.model.EventType;
import com.dt.anh.doranews.model.News;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.service.ArticlePlayerService;
import com.dt.anh.doranews.service.StateLevel;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.MediaManager;
import com.google.gson.Gson;

import java.util.Objects;

public class DetailNewsInVpFragment extends BaseFragment implements View.OnClickListener {
    public static final String PARAM_DETAIL_NEWS = "param_news";
    public static final String PARAM_URL_NEWS = "param_url";
    //    private static MediaManager mMediaManager;
    private static int mPosition;

    private int mProgress;

    private ArticlePlayerService mPlayerService;
    private ServiceConnection mConnection;
    private boolean mIsConnect;

    private ImageView btnPlay;


    private static ArticlePlayerService.OnListenerActivity mListenerActivity;

    public DetailNewsInVpFragment() {

    }

    public static DetailNewsInVpFragment newInstance(String newsJsonString, MediaManager mediaManager, int position, ArticlePlayerService.OnListenerActivity listenerActivity) {
        DetailNewsInVpFragment detailNewsInVpFragment = new DetailNewsInVpFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_DETAIL_NEWS, newsJsonString);
        detailNewsInVpFragment.setArguments(args);
//        mMediaManager = mediaManager;
        mPosition = position;
        mListenerActivity = listenerActivity;
        return detailNewsInVpFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVariable();
        boundService();
        updateUI();
    }

    private void boundService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof ArticlePlayerService.ArticleBinder) {
                    mIsConnect = true;
                    mPlayerService = ((ArticlePlayerService.ArticleBinder) iBinder).getService();
                    mPlayerService.setListenerActivity(mListenerActivity);
                    updateUI();
                } else {
                    mIsConnect = false;
                }
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                mIsConnect = false;
            }
        };

        //getContext or getActivity??
        Intent intent = new Intent(getContext(), ArticlePlayerService.class);
        Objects.requireNonNull(getContext()).bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    private void setViewForButton() {
        switch (getLevelImagePlay()) {
            case StateLevel.PAUSE:
                btnPlay.setVisibility(View.GONE);
                break;
            case StateLevel.PLAY:
                btnPlay.setVisibility(View.VISIBLE);
                break;
        }
    }

    private int getLevelImagePlay() {
        if (mPlayerService == null) {
            return StateLevel.PLAY;
        }
        if (mPlayerService.isOnlyPlaying()) {
            return StateLevel.PAUSE;
        }

        return StateLevel.PLAY;
    }


    public void updateUI() {
        if (!mIsConnect) return;
        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setViewForButton();
            }
        });
    }

    private void initializeVariable() {

    }

    @Override
    protected void initializeComponents() {
        View view = getView();
        if (view == null) {
            return;
        }
        ImageView imageCoverNews = view.findViewById(R.id.image_cover_fr_detail_news);
        TextView txtTitleNews = view.findViewById(R.id.text_fr_title_detail_news);
        TextView txtContentNews = view.findViewById(R.id.text_content_fr_detail_news);


        //====

        Typeface custom_font = Typeface.createFromAsset(Objects.requireNonNull(getActivity()).getAssets(), "fonts/calibril.ttf");

        txtContentNews.setTypeface(custom_font);
        //====

        Button btnReadMore = view.findViewById(R.id.btn_read_more_fr_detail_news);


        //load view

        if (getArguments() == null || getArguments().getString(PARAM_DETAIL_NEWS) == null) {
            Log.e(this.getClass().getName(), "NULL CMNR");
            return;
        }

        String jsonStringNews = getArguments().getString(PARAM_DETAIL_NEWS);

        Gson gson = new Gson();
        final Article news = gson.fromJson(jsonStringNews, Article.class);
        if (news == null) {
            return;
        }

        Glide.with(view.getContext()).load(news.getImage()).
                apply(new RequestOptions().override(400, 0).
                        placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_background))
                .into(imageCoverNews);
        txtTitleNews.setText(news.getTitle());
        String summarization = "";
        for (int i = 0; i < news.getMedias().size(); i++) {
            if (news.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                summarization = news.getMedias().get(i).getBody().get(0).getContent();
                txtContentNews.setText(summarization);
                break;
            }
        }
        btnReadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Xử lý sự kiện khi click vào button xem chi tiết bài báo, mở ra 1 activity riêng, tạm thời show
                //cmn web view ra :v
                Intent intent = new Intent(view.getContext(), WebViewActivity.class);
//                Log.e("pIIIp-url", news.getUrl());
                intent.putExtra(PARAM_URL_NEWS, news.getUrl());
                startActivity(intent);
            }
        });
        btnPlay = view.findViewById(R.id.image_play_fr_detail_news);
        btnPlay.setOnClickListener(this);
    }

    private void loadNews() {

    }

    private String getType() {
        if (getArguments() == null || getArguments().getString(PARAM_DETAIL_NEWS) == null) {
            return EventType.SOCIAL;
        }
        return getArguments().getString(PARAM_DETAIL_NEWS);
    }

    @Override
    protected int getFragmentLayout() {
        return R.layout.fragment_in_vp_detail_news;
    }

    @Override
    protected void initProgressbar() {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play_fr_detail_news:
                changeStateFromDetailMusic();
//                btnPlay.setVisibility(View.GONE);
//                mMediaManager.play(mPosition);
                break;
            default:
                break;
        }
    }

    private void changeStateFromDetailMusic() {
        if (!mIsConnect) {
            return;
        }
        mPlayerService.playArticle();
        if (mPlayerService.isOnlyPlaying()) {
            btnPlay.setVisibility(View.GONE);
//            mImagePlay.setImageLevel(StateLevel.PAUSE);
            return;
        }
//        mImagePlay.setImageLevel(StateLevel.PLAY);
        btnPlay.setVisibility(View.VISIBLE);
    }
}
