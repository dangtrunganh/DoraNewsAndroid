package com.dt.anh.doranews.model;

import com.google.gson.annotations.SerializedName;

public class Category {
    @SerializedName("id")
    private String id;
    @SerializedName("slug")
    private String slug;
    @SerializedName("name")
    private String nameCategory;
    @SerializedName("urlImage")
    private String urlImage;

    private boolean isSelected;

    public Category(String id, String slug, String nameCategory, String urlImage) {
        this.id = id;
        this.slug = slug;
        this.nameCategory = nameCategory;
        this.urlImage = urlImage;
        this.isSelected = false;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public String getUrlImage() {
        return urlImage;
    }

    public void setUrlImage(String urlImage) {
        this.urlImage = urlImage;
    }

    @Override
    public String toString() {
        return "id=" + id + ", slug=" + slug + ", nameCategory=" + nameCategory + ", urlImage=" + urlImage + ", isSelected=" + isSelected + "\n";
    }
}
