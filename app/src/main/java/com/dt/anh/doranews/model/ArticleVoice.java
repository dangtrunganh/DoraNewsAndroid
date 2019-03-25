package com.dt.anh.doranews.model;

public class ArticleVoice {
//    public static final String ROOT_VOICE_ARTICLE = "http://topica.ai:1234/news/article/voice/";
    public static final String ROOT_VOICE_ARTICLE = "https://tts.vbeecore.com/api/tts?app_id=5c860bec79afe74a5b96976c&key=4e3b79811e2a2babd257c9c612d4d782&voice=hn_female_thutrang_phrase_48k-hsmm&rate=1&time=1552366541716&user_id=47218&service_type=1&input_text=";
    private String title;
    private Integer id;
    private String url;

    private String summary;

//    public ArticleVoice(String title, Integer id) {
//        this.title = title;
//        this.id = id;
//        this.url = ROOT_VOICE_ARTICLE + id;
//    }


    public ArticleVoice(String title, Integer id) {
        this.title = title;
        this.id = id;
//        this.url = ROOT_VOICE_ARTICLE + title;
    }

    public void setSummary(String summary) {
        this.summary = summary;
        this.url = ROOT_VOICE_ARTICLE + summary;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }
}
