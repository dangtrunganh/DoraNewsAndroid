package com.dt.anh.doranews.api;

import com.dt.anh.doranews.model.Category;
import com.dt.anh.doranews.model.result.CategoryAPI;
import com.dt.anh.doranews.model.result.articleresult.ArticleResult;
import com.dt.anh.doranews.model.result.eventdetailresult.EventDetailMainResult;
import com.dt.anh.doranews.model.result.eventresult.EventResult;
import com.dt.anh.doranews.model.result.longevent.MainDetailLongEvent;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ServerAPI {
    @GET("categories")
    Call<CategoryAPI> getResultCategory();

    @GET("events")
    Call<EventResult> getResultEvent(@Query("category") String category, @Query("page") String page);

    @GET("events")
    Call<EventResult> getHotEvent(@Query("hot") String isHot, @Query("page") String page);

    @GET("events/{id}")
    Call<EventDetailMainResult> getResultEventDetail(
            @Path("id") String id,
            @Query("includes") String article);

    @GET("events")
    Call<MainDetailLongEvent> getResultLongEvent(@Query("long_event") String idLongEvent,
                                                 @Query("per_page") String perPage,
                                                 @Query("page") String page);

    @GET("article/voice/{id}")
    Call<EventResult> getArticleVoice(@Path("id") String id);

    @GET("articles")
    Call<ArticleResult> getResultArticle(@Query("category") String category,
                                         @Query("page") String page);

    @Multipart
    @POST("login")
    Call<String> login(@Part("uid") RequestBody uID,
                       @Part("username") RequestBody userName,
                       @Part("email") RequestBody email,
                       @Part("token") RequestBody token);
}
