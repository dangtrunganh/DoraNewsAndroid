package com.dt.anh.doranews.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.StringUtil;

import java.util.List;

public class ArticlePlayerService extends Service implements ArticlePlayerManager.OnListenerService{
    private static final String ACTION_CHANGE_MEDIA_STATE = "action_change_media_state";
    private static final String ACTION_NEXT = "action_next";
    private static final String ACTION_PREVIOUS = "action_previous";
//    private int mRepeatType;
    private OnListenerActivity mListenerActivity;
    private ArticlePlayerManager mArticlePlayerManager;
    private final IBinder mIBinder = new ArticleBinder();

    public void setListenerActivity(OnListenerActivity listenerActivity) {
        mListenerActivity = listenerActivity;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mArticlePlayerManager = new ArticlePlayerManager();
        mArticlePlayerManager.setListenerService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handleIntent(intent);
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        handleIntent(intent);
        return mIBinder;
    }

    @Override
    public void updateArticle(Article article) {
        if (mListenerActivity == null) {
            return;
        }
        mListenerActivity.updateArticle(article);
    }

    public class ArticleBinder extends Binder {
        public ArticlePlayerService getService() {
            return ArticlePlayerService.this;
        }
    }

    private void handleIntent(Intent intent) {
        String action = intent != null ? intent.getAction() : null;
        if (action == null) {
            return;
        }
        switch (action) {
            case ACTION_CHANGE_MEDIA_STATE:
                playArticle();
                break;
            case ACTION_NEXT:
                nextArticle();
                break;
            case ACTION_PREVIOUS:
                previousArticle();
                break;
            default:
                break;
        }
    }

    public void setIndexArticleCurrent(int position) {
        mArticlePlayerManager.setIndexSongCurrent(position);
    }

    public void setArticlesList(List<Article> articles) {
        mArticlePlayerManager.setCurrentArticles(articles);
    }



    public void playArticle() {
        mArticlePlayerManager.playArticle();
    }

    public void playArticle(int position) {
        if (mArticlePlayerManager == null) {
            return;
        }
        mArticlePlayerManager.playArticle(position);
    }

    public void nextArticle() {
        mArticlePlayerManager.nextArticle();
    }

    public void previousArticle() {
        mArticlePlayerManager.previousArticle();
    }

    public Article getCurrentArticle() {
        return mArticlePlayerManager.getArticleCurrent();
    }

//    public String getTextTimeSeekBar() {
//        return StringUtil.getTextTimeSeekBar(getCurrentTime(),
//                mSongPlayerManager.getSongCurrent().getDuration());
//    }

//    public String getTextExistTime() {
//        return StringUtil.parseMilliSecondsToTimer(mSongPlayerManager.getSongCurrent()
//                .getDuration() - getCurrentTime());
//    }

    public int getCurrentTime() {
        return mArticlePlayerManager.getCurrentTime();
    }

    // Music dang trang thai playing hoac paused
    public boolean isPlaying() {
        return mArticlePlayerManager.isPlaying();
    }

    //Music dang playing ma khong paused
    public boolean isOnlyPlaying() {
        return mArticlePlayerManager.isOnlyPlaying();
    }

//    public void seek(int progress) {
//        mArticlePlayerManager.seek(progress);
//    }

    //Dung de ra lenh cho MainActivity cap nhat SongUI
    public interface OnListenerActivity {
        /**
         * Hàm này để khi
         */
        void updateArticle(Article article);
    }
}
