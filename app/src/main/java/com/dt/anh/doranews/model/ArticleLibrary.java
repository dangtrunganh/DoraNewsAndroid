package com.dt.anh.doranews.model;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;

import com.dt.anh.doranews.BuildConfig;
import com.dt.anh.doranews.R;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;

public class ArticleLibrary {
    private static final TreeMap<String, MediaMetadataCompat> music = new TreeMap<>();
    private static final HashMap<String, String> albumRes = new HashMap<>(); //link image
    private static final HashMap<String, String> musicFileName = new HashMap<>(); //link voice
    public static ArrayList<Article> currentArticles = new ArrayList<>();

//    private

//    static {
//        createMediaMetadataCompat(
//                "Jazz_In_Paris",
//                "Jazz in Paris",
//                "Media Right Productions",
//                "Jazz & Blues",
//                "Jazz",
//                103,
//                TimeUnit.SECONDS,
//                "jazz_in_paris.mp3",
//                R.drawable.album_jazz_blues,
//                "album_jazz_blues");
//        createMediaMetadataCompat(
//                "The_Coldest_Shoulder",
//                "The Coldest Shoulder",
//                "The 126ers",
//                "Youtube Audio Library Rock 2",
//                "Rock",
//                160,
//                TimeUnit.SECONDS,
//                "the_coldest_shoulder.mp3",
//                R.drawable.album_youtube_audio_library_rock_2,
//                "album_youtube_audio_library_rock_2");
//    }

    public static String getRoot() {
        return "root";
    }

    private static String getAlbumArtUri(String albumArtResName) {
        return ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                BuildConfig.APPLICATION_ID + "/drawable/" + albumArtResName;
    }

    public static String getMusicFilename(String mediaId) {
        return musicFileName.containsKey(mediaId) ? musicFileName.get(mediaId) : null;
    }

    public static String getAlbumRes(String mediaId) {
        //Null thì trả về default
        return albumRes.containsKey(mediaId) ? albumRes.get(mediaId) : "https://upload.wikimedia.org/wikipedia/commons/b/b0/Apple_Headquarters_in_Cupertino.jpg";
    }

    public static Bitmap getAlbumBitmap(Context context, String mediaId) {
//        try {
//            return Picasso.get().load(ArticleLibrary.getAlbumRes(mediaId)).get();
//        } catch (IOException e) {
//            e.printStackTrace();
        return BitmapFactory.decodeResource(context.getResources(), R.drawable.image_default);
//        }
    }

    public static String getAlbumUrlImage(String mediaId) {
        return ArticleLibrary.getAlbumRes(mediaId);
    }

    public static List<MediaBrowserCompat.MediaItem> getMediaItems() {
        createMediaMetadataCompat(); //Tạo giá trị cho list play
        List<MediaBrowserCompat.MediaItem> result = new ArrayList<>();
        for (MediaMetadataCompat metadata : music.values()) {
            result.add(
                    new MediaBrowserCompat.MediaItem(
                            metadata.getDescription(), MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                    )
            );
        }
        return result;
    }

    public static MediaMetadataCompat getMetadata(Context context, String mediaId) {
        MediaMetadataCompat metadataWithoutBitmap = music.get(mediaId);
        Bitmap albumArt = getAlbumBitmap(context, mediaId);

        // Since MediaMetadataCompat is immutable, we need to create a copy to set the album art.
        // We don't set it initially on all items so that they don't take unnecessary memory.
        MediaMetadataCompat.Builder builder = new MediaMetadataCompat.Builder();
        for (String key :
                new String[]{
                        MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                        MediaMetadataCompat.METADATA_KEY_ALBUM,
                        MediaMetadataCompat.METADATA_KEY_ARTIST,
                        MediaMetadataCompat.METADATA_KEY_GENRE,
                        MediaMetadataCompat.METADATA_KEY_TITLE
                }) {
            builder.putString(key, metadataWithoutBitmap.getString(key));
        }
        builder.putLong(
                MediaMetadataCompat.METADATA_KEY_DURATION,
                metadataWithoutBitmap.getLong(MediaMetadataCompat.METADATA_KEY_DURATION));
        builder.putBitmap(MediaMetadataCompat.METADATA_KEY_ALBUM_ART, albumArt);
        return builder.build();
    }

    private static void createMediaMetadataCompat() {
        for (Article article : currentArticles) {
//            int mediaId = article.getId();
//            String title = article.getTitle();
//            String urlImage = article.getImage();
            ArticleVoice articleVoice = ArticleVoice.convertFromArticleToArticleVoice(article);
            String mediaId = String.valueOf(article.getId());
            music.put(mediaId,
                    new MediaMetadataCompat.Builder()
                            .putString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID, mediaId)
                            .putString(MediaMetadataCompat.METADATA_KEY_TITLE, articleVoice.getTitle())
                            .build());
            albumRes.put(mediaId, articleVoice.getUrlImage()); //link image
            musicFileName.put(mediaId, articleVoice.getUrlVoice()); //link voice
        }
    }
}
