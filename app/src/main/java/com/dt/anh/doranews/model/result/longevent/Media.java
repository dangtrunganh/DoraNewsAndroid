
package com.dt.anh.doranews.model.result.longevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Media {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("voice")
    @Expose
    private String voice;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getVoice() {
        return voice;
    }

    public void setVoice(String voice) {
        this.voice = voice;
    }

}
