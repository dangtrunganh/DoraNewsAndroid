package com.dt.anh.doranews.model;

public class News {
    private String mTitleNews;
    private String mImageUrl;
    private String mContent;

    public News(String titleNews, String imageUrl, String content) {
        mTitleNews = titleNews;
        mImageUrl = imageUrl;
        mContent = content;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getTitleNews() {
        return mTitleNews;
    }

    public void setTitleNews(String titleNews) {
        mTitleNews = titleNews;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String content) {
        mContent = content;
    }
}
