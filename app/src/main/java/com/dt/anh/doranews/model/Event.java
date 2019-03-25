package com.dt.anh.doranews.model;

public class Event {
    private String mTitle;
    private String mImageUrl;
    private int mNumberNews;
    private String mTime;
    private String mCategory;

    public Event(String title, String imageUrl, int numberNews, String time, String category) {
        mTitle = title;
        mImageUrl = imageUrl;
        mNumberNews = numberNews;
        mTime = time;
        mCategory = category;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public int getNumberNews() {
        return mNumberNews;
    }

    public void setNumberNews(int numberNews) {
        mNumberNews = numberNews;
    }

    public String getTime() {
        return mTime;
    }

    public void setTime(String time) {
        mTime = time;
    }

    public String getCategory() {
        return mCategory;
    }

    public void setCategory(String category) {
        mCategory = category;
    }
}
