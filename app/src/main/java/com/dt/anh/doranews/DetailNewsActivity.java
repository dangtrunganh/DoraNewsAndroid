package com.dt.anh.doranews;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.dt.anh.doranews.adapter.fragmentpager.DetailNewsAdapter;
import com.dt.anh.doranews.adapter.recyclerview.NewsInCategoryFrgAdapter;
import com.dt.anh.doranews.fakedata.NewsFake;
import com.dt.anh.doranews.fragment.DetailNewsInVpFragment;
import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.News;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.notification.NotificationAction;
import com.dt.anh.doranews.service.ArticlePlayerService;
import com.dt.anh.doranews.service.StateLevel;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.MediaManager;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

public class DetailNewsActivity extends AppCompatActivity implements ArticlePlayerService.OnListenerActivity, View.OnClickListener {
    private android.support.v4.view.ViewPager vpgNews;
    private DetailNewsAdapter mDetailNewsAdapter;
    private CircleIndicator indicator;

    private ArrayList<Article> mArrayNews;
    private MediaManager mMediaManager;
    int position = 0;
//    private ProgressDialog dialog;

    private ArticlePlayerService mPlayerService;
    private ServiceConnection mConnection;
    private boolean mIsConnect;
    private LinearLayout mSmallControlView;
    private ImageView mImagePlaySmall;
    private ImageView mImagePlayPrevious;
    private ImageView mImagePlayNext;
    private TextView mTextTitleArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_detail_news);

        initViews();
        boundService();
        getIntentFromNotification();
//        updateUI();
    }

    public void getIntentFromNotification() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        if (intent.getAction() == null) {
            return;
        }
        if (intent.getAction().equals(NotificationAction.MAIN_ACTION)) {
            showDetailArticle();
            setVisibleController(true);
        }
    }

    public void setVisibleController(boolean isVisible) {
        if (isVisible) {
            mSmallControlView.setVisibility(View.VISIBLE);
            return;
        }
        mSmallControlView.setVisibility(View.GONE);
    }

    private void showDetailArticle() {
        Intent intent = new Intent(this, DetailNewsActivity.class);
        startActivity(intent);
    }


    private void updateUI() {
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mImagePlaySmall.setImageLevel(getLevelImagePlay());
                mTextTitleArticle
                        .setText(getCurrentArticle().getTitle());


//                mTextTitle.setText(getCurrentSong().getTitle());
//                mTextArtist.setText(getCurrentSong().getUsername());
//                loadSongAvatar(mImageSong, getCurrentSong());
//                setViewForButton();
            }
        });
    }

    public Article getCurrentArticle() {
        return mPlayerService.getCurrentArticle();
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

    private void boundService() {
        mConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                if (iBinder instanceof ArticlePlayerService.ArticleBinder) {
                    mIsConnect = true;
                    mPlayerService = ((ArticlePlayerService.ArticleBinder) iBinder).getService();
                    mPlayerService.setArticlesList(mArrayNews);
                    if (mPlayerService == null) {
                        Log.e("mPlayerService", "NULL");
                    }
                    mPlayerService.setListenerActivity(DetailNewsActivity.this);
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

        Intent intent = new Intent(this, ArticlePlayerService.class);
        bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
    }

    private void initViews() {
        //getData
//        dialog = new ProgressDialog(this);
//        dialog.setMessage("Loading..");
//        dialog.setCancelable(false);
//        dialog.show();


        Intent intent = getIntent();
        String json = "";
        String titleEvent = "";
        if (intent != null) {
            json = intent.getStringExtra(ConstParamTransfer.DETAIL_NEWS);
            titleEvent = intent.getStringExtra(ConstParamTransfer.TITLE_EVENT);
            position = intent.getIntExtra(NewsInCategoryFrgAdapter.POSITION_CLICK, 0);
        } else {
            Toast.makeText(this, "Error, nothing in intent", Toast.LENGTH_SHORT).show();
            return;
        }
        Gson gson = new Gson();
        mArrayNews = gson.fromJson(json, new TypeToken<List<Article>>() {
        }.getType());
        Log.e("popo", mArrayNews.toString());



        mMediaManager = new MediaManager(this, mArrayNews);
        //===
//        mArrayNews = NewsFake.getListNews();
        vpgNews = findViewById(R.id.vpg_news_detail_news);
        indicator = findViewById(R.id.indicator_detail_news);
        mDetailNewsAdapter = new DetailNewsAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<>();
        for (int i = 0; i < mArrayNews.size(); i++) {
            Article news = mArrayNews.get(i);
            String jsonNews = gson.toJson(news);
            Fragment fragment = DetailNewsInVpFragment.newInstance(jsonNews, mMediaManager, i, DetailNewsActivity.this);
            fragments.add(fragment);
        }
        mDetailNewsAdapter.set_fragments(fragments);


        vpgNews.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mDetailNewsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        vpgNews.setAdapter(mDetailNewsAdapter);
        vpgNews.setOffscreenPageLimit(mArrayNews.size());
        indicator.setViewPager(vpgNews);

        Toolbar toolbar = findViewById(R.id.toolbar_detail_news);
        setSupportActionBar(toolbar);
        try {
//            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (!titleEvent.equals("")) {
            toolbar.setTitle(titleEvent);

        } else {
            toolbar.setTitle("Title hot event");
        }

        //Lấy tiêu đề từ ngoài truyền vào

        vpgNews.setCurrentItem(position);
//        dialog.dismiss();

        mSmallControlView = findViewById(R.id.include_control_news_act_detail_news);
        mImagePlaySmall = findViewById(R.id.image_play_control_music);
        mImagePlaySmall.setOnClickListener(this);
        mTextTitleArticle = findViewById(R.id.text_title_control_music);
        mImagePlayPrevious = findViewById(R.id.image_previous_control_music);
        mImagePlayPrevious.setOnClickListener(this);
        mImagePlayNext = findViewById(R.id.image_next_control_music);
        mImagePlayNext.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case android.R.id.home:
//                Toast.makeText(this, "Surprise :v", Toast.LENGTH_SHORT).show();
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void updateArticle(Article article) {

    }

    @Override
    protected void onDestroy() {
        unbindService(mConnection);
        mIsConnect = false;
        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.image_play_control_music:
                changeStateFromDetailMusic();
                break;
            case R.id.image_previous_control_music:
                previousFromDetailMusic();
                break;
            case R.id.image_next_control_music:
                nextFromDetailMusic();
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
            mImagePlaySmall.setImageLevel(StateLevel.PAUSE);
            return;
        }
        mImagePlaySmall.setImageLevel(StateLevel.PLAY);
    }

    private void previousFromDetailMusic() {
        if (!mIsConnect) {
            return;
        }
        mPlayerService.previousArticle();
        mImagePlaySmall.setImageLevel(StateLevel.PAUSE);
    }

    private void nextFromDetailMusic() {
        if (!mIsConnect) {
            return;
        }
        mPlayerService.nextArticle();
        mImagePlaySmall.setImageLevel(StateLevel.PAUSE);
    }
}

