
package com.dt.anh.doranews.model.result.articleresult;

import java.util.List;

import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ArticleResult {

    @SerializedName("paging")
    @Expose
    private Paging paging;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("articles")
    @Expose
    private List<Article> articles = null;

    public Paging getPaging() {
        return paging;
    }

    public void setPaging(Paging paging) {
        this.paging = paging;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public String getCategory() {
        return category;
    }
}
