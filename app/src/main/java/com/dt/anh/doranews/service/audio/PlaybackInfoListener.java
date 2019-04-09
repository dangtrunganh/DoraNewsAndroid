package com.dt.anh.doranews.service.audio;

import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;

import com.dt.anh.doranews.service.players.MediaPlayerAdapter;

/**
 * Listener to provide state updates from {@link MediaPlayerAdapter} (the media player)
 * to {@link MusicService} (the service that holds our {@link MediaSessionCompat}.
 */
public abstract class PlaybackInfoListener {

    public abstract void onPlaybackStateChange(PlaybackStateCompat state);

    public void onPlaybackCompleted() {
    }
}