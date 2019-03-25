
package com.dt.anh.doranews.model.result.eventresult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CategoryEvent {

    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("voice")
    @Expose
    private Object voice;

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getVoice() {
        return voice;
    }

    public void setVoice(Object voice) {
        this.voice = voice;
    }

}
