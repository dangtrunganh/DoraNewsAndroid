package com.dt.anh.doranews.model;

import android.support.annotation.StringDef;

import static com.dt.anh.doranews.model.EventType.CULTURE;
import static com.dt.anh.doranews.model.EventType.FOOT_BALL;
import static com.dt.anh.doranews.model.EventType.LAW;
import static com.dt.anh.doranews.model.EventType.SHOW_BIZ;
import static com.dt.anh.doranews.model.EventType.SOCIAL;
import static com.dt.anh.doranews.model.EventType.SPORT;

@StringDef({SPORT, CULTURE, LAW, FOOT_BALL, SOCIAL, SHOW_BIZ})
public @interface EventType {
    String SPORT = "SPORT";
    String CULTURE = "CULTURE";
    String LAW = "LAW";
    String FOOT_BALL = "FOOT_BALL";
    String SOCIAL= "SOCIAL";
    String SHOW_BIZ = "SHOW_BIZ";
}
