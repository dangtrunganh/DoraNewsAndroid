package com.dt.anh.doranews.model;

import com.dt.anh.doranews.model.result.eventdetailresult.Article;
import com.dt.anh.doranews.util.ConstParamTransfer;

public class ArticleVoice {
//    public static final String ROOT_VOICE_ARTICLE = "http://topica.ai:1234/news/article/voice/";
    public static final String ROOT_VOICE_ARTICLE = "https://tts.vbeecore.com/api/tts?app_id=5c860bec79afe74a5b96976c&key=4e3b79811e2a2babd257c9c612d4d782&voice=hn_female_thutrang_phrase_48k-hsmm&rate=1&time=1552366541716&user_id=47218&service_type=1&input_text=";
    private String title;
    private Integer id;
    private String urlVoice;

    private String summary;
    private String urlImage;

    public static ArticleVoice convertFromArticleToArticleVoice(Article article) {
        ArticleVoice articleVoice = new ArticleVoice(article.getId(), article.getTitle(), article.getImage());
        String summarization = "";
        for (int i = 0; i < article.getMedias().size(); i++) {
            if (article.getMedias().get(i).getType().equals(ConstParamTransfer.MEDIUM)) {
                summarization = article.getMedias().get(i).getBody().get(0).getContent();
                //set summary, đồng thời setUrlVoice
                articleVoice.setSummary(summarization);
                articleVoice.setUrlVoice(ROOT_VOICE_ARTICLE + summarization);
                break;
            }
        }
        return articleVoice;
    }

    public ArticleVoice(String title, Integer id) {
        this.title = title;
        this.id = id;
    }

    public ArticleVoice(Integer id, String title, String urlImage) {
        this.id = id;
        this.title = title;
        this.urlImage = urlImage;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getTitle() {
        return title;
    }

    public Integer getId() {
        return id;
    }

    public String getUrlVoice() {
        return urlVoice;
    }

    public void setUrlVoice(String urlVoice) {
        this.urlVoice = urlVoice;
    }

    public String getSummary() {
        return summary;
    }

    public String getUrlImage() {
        return urlImage;
    }
}
