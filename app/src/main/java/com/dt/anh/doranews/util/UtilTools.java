package com.dt.anh.doranews.util;

public class UtilTools {
    public static String convertStringToSlug(String category) {
        category = category.toLowerCase();
        switch (category) {
            case "thế giới":
                return "the-gioi";
            case "pháp luật":
                return "phap-luat";
            case "kinh tế":
                return "kinh-te";
            case "xe cộ":
                return "xe-co";
            case "giải trí":
                return "giai-tri";
            case "xã hội":
                return "xa-hoi";
            case "thể thao":
                return "the-thao";
            case "khoa học":
                return "khoa-hoc";
            case "văn hóa":
                return "van-hoa";
            case "biển đông":
                return "bien-dong";
            case "công nghệ":
                return "cong-nghe";
            case "nhà đất":
                return "nha-dat";
            case "đời sống":
                return "doi-song";
            default:
                return "";
        }
    }
}
