package com.dt.anh.doranews.util;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;

import com.dt.anh.doranews.model.ArticleVoice;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MediaManager implements MediaPlayer.OnCompletionListener {
    private static final int IDE = 1;
    private static final int PLAYING = 2;
    private static final int STOPPED = 3;
    private static final int PAUSE = 4;
    private int mState = IDE;
    private int mIndex = 0;
    private static final String TAG = MediaManager.class.getName();
    private Context mContext;
    private ArrayList<ArticleVoice> mArticleVoices;
    private MediaPlayer mPlayer;

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        next();
    }

    public MediaManager(Context mContext, ArrayList<Article> articles) {
        this.mContext = mContext;
        initData(articles);

        mPlayer = new MediaPlayer();
        mPlayer.setOnCompletionListener(this);
    }




    private void initData(ArrayList<Article> articles) {
        mArticleVoices = new ArrayList<>();
        for (Article article : articles) {
            ArticleVoice articleVoice = new ArticleVoice(article.getTitle(), article.getId());
            mArticleVoices.add(articleVoice);
        }
    }

    public boolean next() {
        mIndex = (mIndex + 1) % mArticleVoices.size();
        stop();
        return play();
    }

    public boolean back() {
        if (mIndex == 0) {
            mIndex = mArticleVoices.size();
        }
        mIndex--;

        stop();
        return play();
    }

    public boolean play() {
        try {
            if (mState == IDE || mState == STOPPED) {
                ArticleVoice articleVoice = mArticleVoices.get(mIndex);
//                mPlayer = MediaPlayer.create(mContext, Uri.parse(articleVoice.getUrl()));
                mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                mPlayer.setDataSource(articleVoice.getUrlVoice());
//                mPlayer.setDataSource(mContext, Uri.parse(articleVoice.getUrl()));

                mPlayer.prepare();
                mPlayer.start();
                mState = PLAYING;
                return true;
            } else if (mState == PLAYING) {
                pause();
                return false;
            } else if (mState == PAUSE) {
                mPlayer.start();
                mState = PLAYING;
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void pause() {
        mPlayer.pause();
        mState = PAUSE;
    }

    public boolean play(int position) {
        stop();
        mIndex = position;
        return play();
    }

    public void stop() {
        if (mState == PLAYING || mState == PAUSE) {
            mPlayer.stop();
            mPlayer.reset();
            mState = STOPPED;
        }
    }
}
