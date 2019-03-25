package com.dt.anh.doranews.service;

import android.support.annotation.IntDef;

@IntDef({StateLevel.PAUSE, StateLevel.PLAY})
public @interface StateLevel {
    int PAUSE = 0;
    int PLAY = 1;
}
