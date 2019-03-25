package com.dt.anh.doranews;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class TestAct extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
//            mediaPlayer = MediaPlayer.create(this, Uri.parse("http://dict.youdao.com/dictvoice?audio=good&type=1"));
//                mPlayer.setDataSource(mContext, Uri.parse(articleVoice.getUrl()));
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource("http://topica.ai:1234/news/event/voice/rdqIQl93D8");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
