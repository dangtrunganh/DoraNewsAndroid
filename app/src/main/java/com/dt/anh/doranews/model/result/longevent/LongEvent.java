
package com.dt.anh.doranews.model.result.longevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LongEvent {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
