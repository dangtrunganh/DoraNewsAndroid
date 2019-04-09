package com.dt.anh.doranews.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.dt.anh.doranews.EmptyActivity;
import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.model.result.eventresult.Datum;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class UtilTools {
    public static String convertStringToSlug(String category) {
        category = category.toLowerCase();
        switch (category) {
            case "thế giới":
                return "the-gioi";
            case "pháp luật":
                return "phap-luat";
            case "kinh tế":
                return "kinh-te";
            case "xe cộ":
                return "xe-co";
            case "giải trí":
                return "giai-tri";
            case "xã hội":
                return "xa-hoi";
            case "thể thao":
                return "the-thao";
            case "khoa học":
                return "khoa-hoc";
            case "văn hóa":
                return "van-hoa";
            case "biển đông":
                return "bien-dong";
            case "công nghệ":
                return "cong-nghe";
            case "nhà đất":
                return "nha-dat";
            case "đời sống":
                return "doi-song";
            default:
                return "";
        }
    }

    public static String getListCategoryInCache(Context mContext) {
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
        return pre.getString(ConstParamStorageLocal.KEY_PREF_LIST_CATEGORY,
                ConstParamStorageLocal.DEFAULT_VALUE_PREF_LIST_CATEGORY_DEFAULT);
    }


    //Bất cứ khi nào cần uID, chỉ cần vào trong cache là lấy được luôn, gọi hàm này là xong
    //Chắc chỉ cần sử dụng nó trong Detail Event để follow 1 Long Event

    /**
     * Hàm này thực hiện lấy UId từ SharedPreference ra
     *
     * @param mContext Ngữ cảnh hiện tại
     * @return UId tương ứng get được, chú ý có thể "" - xâu rỗng (default)
     */
    public static String getUId(Context mContext) {
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_UID, MODE_PRIVATE);
        return pre.getString(ConstParamStorageLocal.KEY_PREF_UID,
                ConstParamStorageLocal.DEFAULT_VALUE_PREF_KEY_UID_DEFAULT);
    }

    /**
     * Hàm này thực hiện lưu UId xuống SharedPreference
     *
     * @param mContext Ngữ cảnh hiện tại
     * @param uId      tương ứng là uId cần lưu xuống local
     */
    public static void storeUId(Context mContext, String uId) {
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_UID, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.apply();

        //===Storage-UId====
        editor.putString(ConstParamStorageLocal.KEY_PREF_UID, uId);
        //Lưu xuống file
        editor.apply();

    }

    public static void clearCacheCategory(Context mContext) {
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_LIST_CATEGORY, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.apply();
    }

    public static void clearCacheUId(Context mContext) {
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_UID, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.apply();
    }

    //===General====
    public static void clearCacheGeneral(Context mContext, String fileName) {
        SharedPreferences pre = mContext.getSharedPreferences
                (fileName, MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        editor.clear();
        editor.apply();
    }


    /*cache=================================cache*/
    //=====Cache Event & Articles in Hot event ======
    //1 ===========STORE==============
    //Mỗi lần store xuống, là xóa cmn hết cái cũ đi :v
    public static void storeEvent(Context mContext, List<Datum> arrayListEvent) {
        //Bước 1: Chuyển list sang json String
        Gson gson = new Gson();
        String json = gson.toJson(arrayListEvent);

        //Bước 2: Lưu vào trong share preference
        SharedPreferences pre = mContext.getSharedPreferences(
                ConstParamStorageLocal.FILE_NAME_PREF_CACHE_HOT,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();

        //Xóa list cũ đi rồi mới add vào
        editor.remove(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_EVENT);
        editor.apply();

        editor.putString(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_EVENT, json);
        //chấp nhận lưu xuống file
        editor.commit();
        editor.apply();
    }

    public static void storeArticle(Context mContext, ArrayList<Article> arrayListEvent) {
        //Bước 1: Chuyển list sang json String
        Gson gson = new Gson();
        String json = gson.toJson(arrayListEvent);

        //Bước 2: Lưu vào trong share preference
        SharedPreferences pre = mContext.getSharedPreferences(
                ConstParamStorageLocal.FILE_NAME_PREF_CACHE_HOT,
                MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();

        //Xóa list cũ đi rồi mới add vào
        editor.remove(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_ARTICLE);
        editor.apply();

        editor.putString(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_ARTICLE, json);
        //chấp nhận lưu xuống file
        editor.commit();
        editor.apply();
    }


    //2 ===========GET==============
    public static ArrayList<Datum> getListEvent(Context mContext) {
        //Ko có gì thì trả về list size = 0, có thì trả về list đó
        //1. get jsonString listEvent
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_CACHE_HOT, MODE_PRIVATE);
        String jsonEvent = pre.getString(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_EVENT,
                ConstParamStorageLocal.DEFAULT_VALUE_PREF_CACHE_HOT_EVENT);
        if (jsonEvent.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_CACHE_HOT_EVENT)) {
            Toast.makeText(mContext, "LE-Nothing in pref-event-cache", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        } else {
            Gson gson = new Gson();
            ArrayList<Datum> arrayList = gson.fromJson(jsonEvent, new TypeToken<List<Datum>>() {
            }.getType());
            return arrayList;
        }
    }

    public static ArrayList<Article> getListArticle(Context mContext) {
        //Ko có gì thì trả về list size = 0, có thì trả về list đó
        //1. get jsonString listArticle
        SharedPreferences pre = mContext.getSharedPreferences
                (ConstParamStorageLocal.FILE_NAME_PREF_CACHE_HOT, MODE_PRIVATE);

        String jsonArticle = pre.getString(ConstParamStorageLocal.KEY_PREF_CACHE_HOT_ARTICLE,
                ConstParamStorageLocal.DEFAULT_VALUE_PREF_CACHE_HOT_ARTICLE);

        if (jsonArticle.equals(ConstParamStorageLocal.DEFAULT_VALUE_PREF_CACHE_HOT_ARTICLE)) {
            Toast.makeText(mContext, "LA-Nothing in pref-article-cache", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        } else {
            Gson gson = new Gson();
            ArrayList<Article> arrayList = gson.fromJson(jsonArticle, new TypeToken<List<Article>>() {
            }.getType());
            return arrayList;
        }
    }
    /*cache=================================cache*/





    //=============Convert==============
    //===Convert jsonString to List Category======

}
