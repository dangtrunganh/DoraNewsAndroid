
package com.dt.anh.doranews.model.result.longevent;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MainDetailLongEvent {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("filters")
    @Expose
    private Filters filters;
    @SerializedName("sort")
    @Expose
    private Sort sort;
    @SerializedName("data")
    @Expose
    private List<Datum> data = null;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public Filters getFilters() {
        return filters;
    }

    public void setFilters(Filters filters) {
        this.filters = filters;
    }

    public Sort getSort() {
        return sort;
    }

    public void setSort(Sort sort) {
        this.sort = sort;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

}
