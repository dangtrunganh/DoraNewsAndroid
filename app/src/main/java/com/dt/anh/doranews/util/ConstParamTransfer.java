package com.dt.anh.doranews.util;

public class ConstParamTransfer {
    public static final String PARAM_ID_EVENT = "PARAM_ID_EVENT";
    public static final String PARAM_ID_LONG_EVENT = "PARAM_ID_LONG_EVENT";
    public static final String ARTICLE = "article";
    public static final String DETAIL_NEWS = "DETAIL_NEWS";
    public static final String MEDIUM = "medium";
    public static final String TITLE_EVENT = "TITLE_EVENT";


    //Truyền từ EmptyActivity, trong trường hợp đã có data category ở local
    //Truyền sang màn MainActivity, với 2 param id_event và id_long_event
    public static final String TRANSFER_EVENT_ID_FR_EMPTY_TO_MAIN = "TRANSFER_EVENT_ID_FR_EMPTY_TO_MAIN";
    public static final String TRANSFER_LONG_EVENT_ID_FR_EMPTY_TO_MAIN = "TRANSFER_EVENT_ID_FR_EMPTY_TO_MAIN";
    //Tiếp, truyền từ màn MainActivity sang màn DetailEventActivity
    public static final String TRANSFER_EVENT_ID_FR_MAIN_TO_DETAIL_EVENT = "TRANSFER_EVENT_ID_FR_MAIN_TO_DETAIL_EVENT";
    public static final String TRANSFER_LONG_EVENT_ID_FR_MAIN_TO_DETAIL_EVENT = "TRANSFER_EVENT_ID_FR_MAIN_TO_DETAIL_EVENT";
}
