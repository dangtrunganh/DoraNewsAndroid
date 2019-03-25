package com.dt.anh.doranews.adapter.fragmentpager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

public class DetailNewsAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> _fragments;

    public DetailNewsAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
        this._fragments = new ArrayList<>();
    }


    public void add(Fragment fragment) {
        this._fragments.add(fragment);
    }

    public void set_fragments(ArrayList<Fragment> _fragments) {
        this._fragments = _fragments;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return _fragments.size();
    }

    // Returns the fragment to display for that page
    @Override
    public Fragment getItem(int position) {

        Fragment fragment = this._fragments.get(position);


        return fragment;
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
}

