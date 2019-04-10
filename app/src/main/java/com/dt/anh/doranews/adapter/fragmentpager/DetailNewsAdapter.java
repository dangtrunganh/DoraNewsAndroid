package com.dt.anh.doranews.adapter.fragmentpager;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.ViewGroup;

import com.dt.anh.doranews.fragment.DetailNewsInVpFragment;
import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.service.ArticlePlayerService;
import com.google.gson.Gson;

import java.util.ArrayList;

public class DetailNewsAdapter extends FragmentStatePagerAdapter {
    //    private ArrayList<DetailNewsInVpFragment> _fragments;
    private ArrayList<Article> mArrayNews;
    private Gson gson;
//    private ArticlePlayerService.OnListenerActivity listenerActivity;

    public DetailNewsAdapter(FragmentManager fragmentManager, ArrayList<Article> mArrayNews/*, ArticlePlayerService.OnListenerActivity listenerActivity*/) {
        super(fragmentManager);
//        this._fragments = new ArrayList<>();
        this.mArrayNews = mArrayNews;
//        this.listenerActivity = listenerActivity;
        gson = new Gson();
    }


//    public void add(DetailNewsInVpFragment fragment) {
//        this._fragments.add(fragment);
//    }

//    public void set_fragments(ArrayList<DetailNewsInVpFragment> _fragments) {
//        this._fragments = _fragments;
//    }


    public void setmArrayNews(ArrayList<Article> mArrayNews) {
        this.mArrayNews = mArrayNews;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return mArrayNews.size();
    }

    // Returns the fragment to display for that page
    @Override
    public DetailNewsInVpFragment getItem(int position) {
//        DetailNewsInVpFragment fragment = this._fragments.get(position);
        Article news = mArrayNews.get(position);
        String jsonNews = gson.toJson(news);

        return DetailNewsInVpFragment.newInstance(jsonNews/*, mMediaManager*/, position/*, this.listenerActivity*/);
//            switch (position) {
//                case 0:
//                    return ListMusicFragment.newInstance();
//                case 1:
//                    return LyricMusicFragment.newInstance();
//                default:
//                    return null;
//            }
    }

    // Returns the page title for the top indicator
    @Override
    public CharSequence getPageTitle(int position) {
        return "Page " + position;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        super.destroyItem(container, position, object);
    }
}

