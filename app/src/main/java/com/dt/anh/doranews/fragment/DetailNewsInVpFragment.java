package com.dt.anh.doranews.fragment;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
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
import com.dt.anh.doranews.client.audio.MediaBrowserHelper;
import com.dt.anh.doranews.model.EventType;
import com.dt.anh.doranews.model.News;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.service.ArticlePlayerService;
import com.dt.anh.doranews.service.StateLevel;
import com.dt.anh.doranews.service.audio.MusicService;
import com.dt.anh.doranews.util.ConstParamTransfer;
import com.dt.anh.doranews.util.MediaManager;
import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

public class DetailNewsInVpFragment extends BaseFragment implements View.OnClickListener {
    public static final String PARAM_DETAIL_NEWS = "param_news";
    public static final String PARAM_URL_NEWS = "param_url";
    //    private static MediaManager mMediaManager;
    private static int mPosition;

    private int mProgress;

//    private ArticlePlayerService mPlayerService;
//    private ServiceConnection mConnection;
//    private boolean mIsConnect;

    private ImageView btnPlay;
    private MediaBrowserHelper mMediaBrowserHelper;

    private boolean mIsPlaying;

    private Article currentArticles;

//    private static ArticlePlayerService.OnListenerActivity mListenerActivity;

    public DetailNewsInVpFragment() {

    }

    public static DetailNewsInVpFragment newInstance(String newsJsonString, /*MediaManager mediaManager,*/ int position/*, ArticlePlayerService.OnListenerActivity listenerActivity*/) {
        DetailNewsInVpFragment detailNewsInVpFragment = new DetailNewsInVpFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_DETAIL_NEWS, newsJsonString);
        detailNewsInVpFragment.setArguments(args);
//        mMediaManager = mediaManager;
        mPosition = position;
//        mListenerActivity = listenerActivity;
        return detailNewsInVpFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeVariable();
//        boundService();
//        updateUI();
//        mMediaBrowserHelper = new MediaBrowserConnection(getContext());
//        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());
    }

    @Override
    public void onStart() {
        super.onStart();
//        mMediaBrowserHelper.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
//        mMediaBrowserHelper.onStop();
    }

    //    private void boundService() {
//        mConnection = new ServiceConnection() {
//            @Override
//            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
//                if (iBinder instanceof ArticlePlayerService.ArticleBinder) {
//                    mIsConnect = true;
//                    mPlayerService = ((ArticlePlayerService.ArticleBinder) iBinder).getService();
//                    mPlayerService.setListenerActivity(mListenerActivity);
//                    updateUI();
//                } else {
//                    mIsConnect = false;
//                }
//            }
//
//            @Override
//            public void onServiceDisconnected(ComponentName componentName) {
//                mIsConnect = false;
//            }
//        };
//
//        //getContext or getActivity??
//        Intent intent = new Intent(getContext(), ArticlePlayerService.class);
//        Objects.requireNonNull(getContext()).bindService(intent, mConnection, Service.BIND_AUTO_CREATE);
//    }

//    private void setViewForButton() {
//        switch (getLevelImagePlay()) {
//            case StateLevel.PAUSE:
//                btnPlay.setVisibility(View.GONE);
//                break;
//            case StateLevel.PLAY:
//                btnPlay.setVisibility(View.VISIBLE);
//                break;
//        }
//    }

//    private int getLevelImagePlay() {
//        if (mPlayerService == null) {
//            return StateLevel.PLAY;
//        }
//        if (mPlayerService.isOnlyPlaying()) {
//            return StateLevel.PAUSE;
//        }
//
//        return StateLevel.PLAY;
//    }


//    public void updateUI() {
//        if (!mIsConnect) return;
//        Objects.requireNonNull(getActivity()).runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                setViewForButton();
//            }
//        });
//    }

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

        //Bài báo hiện tại của Fragment này
        currentArticles = gson.fromJson(jsonStringNews, Article.class);
        if (currentArticles == null) {
            return;
        }

        Glide.with(view.getContext()).load(currentArticles.getImage()).
                apply(new RequestOptions().override(400, 0).
                        placeholder(R.drawable.image_default).error(R.drawable.image_default))
                .into(imageCoverNews);
        txtTitleNews.setText(currentArticles.getTitle());
        String summarization = "";
        for (int i = 0; i < currentArticles.getMedias().size(); i++) {
            if (currentArticles.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                summarization = currentArticles.getMedias().get(i).getBody().get(0).getContent();
                txtContentNews.setText(summarization);
                break;
            }
        }

        btnReadMore.setOnClickListener(view1 -> {
            //Xử lý sự kiện khi click vào button xem chi tiết bài báo, mở ra 1 activity riêng, tạm thời show
            //cmn web view ra :v
            Intent intent = new Intent(view1.getContext(), WebViewActivity.class);
//                Log.e("pIIIp-url", news.getUrl());
            intent.putExtra(PARAM_URL_NEWS, currentArticles.getUrl());
            startActivity(intent);
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
                if (mIsPlaying) {
//                    mMediaBrowserHelper.getTransportControls().pause();
                } else {
//                    mMediaBrowserHelper.getTransportControls().playFromMediaId(String.valueOf(currentArticles.getId()), new Bundle());
//                    mMediaBrowserHelper.getTransportControls().play();
                }
//                changeStateFromDetailMusic();
                break;
            default:
                break;
        }
    }

//    private void changeStateFromDetailMusic() {
//        if (!mIsConnect) {
//            return;
//        }
//        mPlayerService.playArticle();
//        if (mPlayerService.isOnlyPlaying()) {
//            btnPlay.setVisibility(View.GONE);
////            mImagePlay.setImageLevel(StateLevel.PAUSE);
//            return;
//        }
////        mImagePlay.setImageLevel(StateLevel.PLAY);
//        btnPlay.setVisibility(View.VISIBLE);
//    }

    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
//    private class MediaBrowserConnection extends MediaBrowserHelper {
//        private MediaBrowserConnection(Context context) {
//            super(context, MusicService.class);
//        }
//
//        @Override
//        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
////            mSeekBarAudio.setMediaController(mediaController);
//        }
//
//        @Override
//        protected void onChildrenLoaded(@NonNull String parentId,
//                                        @NonNull List<MediaBrowserCompat.MediaItem> children) {
//            super.onChildrenLoaded(parentId, children);
//
//            final MediaControllerCompat mediaController = getMediaController();
//
//            // Queue up all media items for this simple sample.
//            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
//                mediaController.addQueueItem(mediaItem.getDescription());
//            }
//
//            // Call prepare now so pressing play just works.
//            mediaController.getTransportControls().prepare();
//        }
//    }

    /**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     */
//    private class MediaBrowserListener extends MediaControllerCompat.Callback {
//        @Override
//        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
//            mIsPlaying = playbackState != null &&
//                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
//            btnPlay.setPressed(mIsPlaying);
//            //Ca ben duoi
//        }
//
//        @Override
//        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
//            if (mediaMetadata == null) {
//                return;
//            }
//
//            //Đoạn này cần gọi ở màn hình ngoài
////            mTitleTextView.setText(
////                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
////            mArtistTextView.setText(
////                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
////            mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
////                    MainActivity.this,
////                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));
//        }
//
//        @Override
//        public void onSessionDestroyed() {
//            super.onSessionDestroyed();
//        }
//
//        @Override
//        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
//            super.onQueueChanged(queue);
//        }
//    }
}
