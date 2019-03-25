package com.dt.anh.doranews.adapter.viewpager;

import android.support.annotation.IntDef;

@IntDef({TabPosition.TAB_SPORT, TabPosition.TAB_CULTURE,
        TabPosition.TAB_LAW, TabPosition.TAB_FOOT_BALL,
        TabPosition.TAB_SOCIAL, TabPosition.TAB_SHOW_BIZ})
public @interface TabPosition {
    int TAB_SPORT = 0;
    int TAB_CULTURE = 1;
    int TAB_LAW = 2;
    int TAB_FOOT_BALL = 3;
    int TAB_SOCIAL = 4;
    int TAB_SHOW_BIZ = 5;
    int TOTAL_TAB = 6;
}