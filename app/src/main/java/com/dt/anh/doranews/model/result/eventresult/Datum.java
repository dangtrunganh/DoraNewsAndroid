
package com.dt.anh.doranews.model.result.eventresult;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Datum {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("num_articles")
    @Expose
    private Integer numArticles;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("time")
    @Expose
    private String time;
    @SerializedName("readableTime")
    @Expose
    private String readableTime;
    @SerializedName("category")
    @Expose
    private CategoryEvent category;

    @SerializedName("content")
    @Expose
    private String content;

    @SerializedName("long_event")
    @Expose
    private LongEvent longEvent;

    @SerializedName("medias")
    @Expose
    private List<Media> medias = null;

    @Override
    public String toString() {
        return "Datum{" +
                "id='" + id + '\'' +
                ", numArticles=" + numArticles +
                ", slug='" + slug + '\'' +
                ", title='" + title + '\'' +
                ", image='" + image + '\'' +
                ", time='" + time + '\'' +
                ", readableTime='" + readableTime + '\'' +
                ", category='" + category + '\'' +
                ", content='" + content + '\'' +
                ", longEvent'=" + longEvent + '\'' +
                ", medias='" + medias + '\'' +
                '}' + "\n";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getNumArticles() {
        return numArticles;
    }

    public void setNumArticles(Integer numArticles) {
        this.numArticles = numArticles;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getReadableTime() {
        return readableTime;
    }

    public void setReadableTime(String readableTime) {
        this.readableTime = readableTime;
    }

    public CategoryEvent getCategory() {
        return category;
    }

    public void setCategory(CategoryEvent category) {
        this.category = category;
    }

    public LongEvent getLongEvent() {
        return longEvent;
    }

    public void setLongEvent(LongEvent longEvent) {
        this.longEvent = longEvent;
    }

    public List<Media> getMedias() {
        return medias;
    }

    public void setMedias(List<Media> medias) {
        this.medias = medias;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
