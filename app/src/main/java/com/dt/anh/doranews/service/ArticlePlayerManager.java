package com.dt.anh.doranews.service;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;
import android.widget.Toast;

import com.dt.anh.doranews.model.ArticleVoice;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.StringUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ArticlePlayerManager implements MediaPlayer.OnPreparedListener, MediaPlayer.OnCompletionListener {
    //    private int mRepeatType = RepeatType.REPEAT_OFF;
//    private boolean mIsShuffle;
    private OnListenerService mListenerService;
    private MediaPlayer mPlayer;
    private List<Article> mCurrentArticles;
    private List<ArticleVoice> mCurrentArticleVoices;
    private int mIndexArticleCurrent;
    private int mState = MediaPlayerState.IDLE;

    public ArticlePlayerManager() {
        mPlayer = new MediaPlayer();
    }

    public void setCurrentArticles(List<Article> currentArticles) {
        if (currentArticles == null) {
            return;
        }
        mCurrentArticles = currentArticles;
        initData(mCurrentArticles);
    }


    private void initData(List<Article> articles) {
        mCurrentArticleVoices = new ArrayList<>();
        for (Article article : articles) {
            ArticleVoice articleVoice = new ArticleVoice(article.getTitle(), article.getId());
            boolean flag = true;
            for (int i = 0; i < article.getMedias().size(); i++) {
                if (article.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                    String summarization = article.getMedias().get(i).getBody().get(0).getContent();
                    articleVoice.setSummary(summarization);
                    flag = false;
                    break;
                }
            }
            if (flag) {
                String summarization = "Bài báo này không có tóm tắt";
                articleVoice.setSummary(summarization);
            }
            mCurrentArticleVoices.add(articleVoice);
        }
    }


    public void setListenerService(OnListenerService listenerService) {
        mListenerService = listenerService;
    }

    //Dang trang thai playing
    public boolean isOnlyPlaying() {
        return mState == MediaPlayerState.PLAYING;
    }

    public boolean isPlaying() {
        return mState == MediaPlayerState.PLAYING || mState == MediaPlayerState.PAUSED;
    }

    public void setIndexSongCurrent(int indexArticleCurrent) {
        mIndexArticleCurrent = indexArticleCurrent;
    }

    public Article getArticleCurrent() {
        return mCurrentArticles.get(mIndexArticleCurrent);
    }

    public void playArticle() {
        if (mCurrentArticleVoices.size() == 0) {
            Log.e("ERROR", "mCurrentArticleVoices.size() = 0");
            return;
        }
        if (mState == MediaPlayerState.IDLE || mState == MediaPlayerState.STOPPED) {
            ArticleVoice articleVoice = mCurrentArticleVoices.get(mIndexArticleCurrent);
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            try {
                mPlayer.setDataSource(articleVoice.getUrl());
            } catch (IOException e) {
                nextArticle();
            }
            //Mỗi khi chạy bài mới sẽ set thông tin mới
            if (mListenerService != null) {
                mListenerService.updateArticle(getArticleCurrent());
            }
            //chay seekbar
            mPlayer.setOnPreparedListener(this);
            mPlayer.setOnCompletionListener(this);
            mPlayer.prepareAsync();
            mState = MediaPlayerState.PLAYING;
            return;
        }
        //dang o playing thi se chuyen sang paused
        if (mState == MediaPlayerState.PLAYING) {
            mPlayer.pause();
            mState = MediaPlayerState.PAUSED;
            return;
        }

        mPlayer.start();
        mState = MediaPlayerState.PLAYING;
    }


    public void stopArticle() {
        if (mState == MediaPlayerState.PLAYING || mState == MediaPlayerState.PAUSED) {
            mPlayer.stop();
            mPlayer.reset();
            mState = MediaPlayerState.STOPPED;
        }
    }

    public void nextArticle() {
        if (mIndexArticleCurrent == mCurrentArticles.size()) {
            return;
        }
        mIndexArticleCurrent++;
//        if (mIsShuffle) {
//            mIndexSongCurrent = new Random().nextInt(mCurrentSongs.size());
//        } else {
//            mIndexSongCurrent++;
//        }
        stopArticle();
        playArticle();
    }

    public void previousArticle() {
        if (mIndexArticleCurrent == 0) {
            return;
        }
        mIndexArticleCurrent--;
        stopArticle();
        playArticle();
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        repeatOff();
    }

    private void repeatOff() {
        if (mIndexArticleCurrent < mCurrentArticles.size() - 1) {
            mIndexArticleCurrent++;
            stopArticle();
            playArticle();
        }
    }

    public int getCurrentTime() {
        return mPlayer.getCurrentPosition();
    }

//    public String getTextTimeSeekBar() {
//        return StringUtil.getTextTimeSeekBar(getCurrentTime(),
//                getArticleCurrent().getDuration());
//    }

    public void playArticle(int position) {
        mIndexArticleCurrent = position;
        stopArticle();
        playArticle();
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {
        mPlayer.start();
    }

    //Dung de ra lenh cho MainActivity cap nhat SongUI
    public interface OnListenerService {
        void updateArticle(Article article);
    }
}
