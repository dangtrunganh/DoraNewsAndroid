package com.dt.anh.doranews.util;

public class ConstParamAPI {
    //All const ThresHold for loadMore

    //Long event, event, article có chung START_PAGE, load bắt đầu từ page 1
    public static final int START_PAGE = 1;

    //============LongEvent============
    //ThresHold cho loadMore của LongEvent
    public static final int NUMBER_LONG_EVENT_PER_PAGE = 3;

    //============Event============
    //Số event hiển thị tại mỗi lần call API list các event ở CategoryFragment
    public static final int EVENT_THRESHOLD = 6; //Cả hot và không hot


    //============Article============
    //Số article hiển thị tại mỗi lần call API list các article ở AllNewsActivity
    public static final int ARTICLE_THRESHOLD_ALL_NEWS_ACT = 6;

    //============ArticleInCategoryFragment============
    //Số article hiển thị tại mỗi lần call API list các article ở CategoryFragment
    public static final int ARTICLE_THRESHOLD_CATEGORY_FRG = 4;
}
