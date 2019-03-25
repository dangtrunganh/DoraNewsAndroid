package com.dt.anh.doranews.fakedata;

import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.Event;

import java.util.ArrayList;

public class CategoriesFake {
    private static ArrayList<Category> listCategory;

    public static ArrayList<Category> getListCategory() {
        if (listCategory == null) {
            listCategory = new ArrayList<>();

            //==================
            Category category = new Category("1", "the-thao", "Thể thao",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
            category = new Category("1", "xa-hoi", "Xã hội",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
            category = new Category("1", "van-hoa", "Văn hóa",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
            category = new Category("1", "phap-luat", "Pháp luật",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
            category = new Category("1", "ca-nhac", "Ca nhạc",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
            category = new Category("1", "doi-song", "Đời sống",
                    "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
            listCategory.add(category);
        }
        return listCategory;
    }

}
