package com.dt.anh.doranews.model.result;

import com.dt.anh.doranews.model.Category;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class CategoryAPI implements Serializable {
    @SerializedName("data")
    private ArrayList<Category> mArrayList;

    public CategoryAPI(ArrayList<Category> arrayList) {
        mArrayList = arrayList;
    }

    public ArrayList<Category> getArrayList() {
        return mArrayList;
    }
}
