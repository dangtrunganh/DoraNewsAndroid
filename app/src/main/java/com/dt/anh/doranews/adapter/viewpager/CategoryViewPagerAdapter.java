package com.dt.anh.doranews.adapter.viewpager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.dt.anh.doranews.model.Category;

import java.util.ArrayList;

public class CategoryViewPagerAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> _fragments;
    private ArrayList<Category> _categories;

    public CategoryViewPagerAdapter(FragmentManager fm) {
        super(fm);
        this._fragments = new ArrayList<>();
        this._categories = new ArrayList<>();
    }

    public void set_fragments(ArrayList<Fragment> _fragments) {
        this._fragments = _fragments;
    }

    public void set_categories(ArrayList<Category> _categories) {
        this._categories = _categories;
    }

    @Override
    public Fragment getItem(int position) {
        return this._fragments.get(position);
//        switch (position) {
//            case TabPosition.TAB_SPORT:
//                return CategoryFragment.newInstance(EventType.SPORT);
//            case TabPosition.TAB_CULTURE:
//                return CategoryFragment.newInstance(EventType.CULTURE);
//            case TabPosition.TAB_LAW:
//                return CategoryFragment.newInstance(EventType.LAW);
//            case TabPosition.TAB_FOOT_BALL:
//                return CategoryFragment.newInstance(EventType.FOOT_BALL);
//            case TabPosition.TAB_SOCIAL:
//                return CategoryFragment.newInstance(EventType.SOCIAL);
//            case TabPosition.TAB_SHOW_BIZ:
//                return CategoryFragment.newInstance(EventType.SHOW_BIZ);
//            default:
//                return CategoryFragment.newInstance(EventType.SPORT);
//        }
    }

    @Override
    public int getCount() {
        return _fragments.size();
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        switch (position) {
//            case TabPosition.TAB_SPORT:
//                return EventType.SPORT;
//            case TabPosition.TAB_CULTURE:
//                return EventType.CULTURE;
//            case TabPosition.TAB_LAW:
//                return EventType.LAW;
//            case TabPosition.TAB_FOOT_BALL:
//                return EventType.FOOT_BALL;
//            case TabPosition.TAB_SOCIAL:
//                return EventType.SOCIAL;
//            case TabPosition.TAB_SHOW_BIZ:
//                return EventType.SHOW_BIZ;
//            default:
//                return EventType.SPORT;
//        }
//    }

    @Override
    public CharSequence getPageTitle(int position) {
        return _categories.get(position).getNameCategory();
    }
}
