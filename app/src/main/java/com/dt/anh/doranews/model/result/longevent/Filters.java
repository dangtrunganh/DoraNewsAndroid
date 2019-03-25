
package com.dt.anh.doranews.model.result.longevent;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Filters {

    @SerializedName("long_event")
    @Expose
    private String longEvent;

    public String getLongEvent() {
        return longEvent;
    }

    public void setLongEvent(String longEvent) {
        this.longEvent = longEvent;
    }

}
