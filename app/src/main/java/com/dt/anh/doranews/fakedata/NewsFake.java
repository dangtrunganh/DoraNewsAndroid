package com.dt.anh.doranews.fakedata;

import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.News;

import java.util.ArrayList;

public class NewsFake {
    private static ArrayList<News> listNews;

    public static ArrayList<News> getListNews() {
        if (listNews == null) {
            listNews = new ArrayList<>();

            //==================
            News mNews = new News("CĐV Việt Nam sang Malaysia dù chưa mua được vé vào sân Bukit Jalil", "https://upload.wikimedia.org/wikipedia/commons/f/fb/Football_trip.jpg", "Lường trước sức mạnh của các cầu thủ Iran, HLV Lippi đã sớm xây dựng lối đá phòng ngự phản công cho các cầu thủ Trung Quốc. Chính vì thế mà không khó cho các cầu thủ Iran kiểm soát bóng nhiều hơn và gây áp lực về phía khung thành đội bóng áo đỏ ngay từ đầu trận.");
            listNews.add(mNews);
            mNews = new News("Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng", "https://upload.wikimedia.org/wikipedia/commons/d/d3/Japan-1454396_640.jpg", "Thủ thành Selecao đã trở thành mục tiêu theo đuổi sát sao của 2 gã khổng lồ Premier League hồi mùa hè sau một chiến dịch thành công cùng AS Roma mùa qua.\n" +
                    "Chelsea đã mất thủ thành Thibaut Courtois vào tay Real Madrid nên sẵn lòng chi bộn để giành chữ ký của Alisson, nhưng cuối cùng Liverpool đã thắng trong cuộc đua khi biến thủ thành Brazil thành người trấn giữ khung thành đắt giá nhất thế giới với 73 triệu Euro. Chelsea ôm hận đành phải ký với Kepa Arrizabalaga của Athletic Bilbao với giá kỷ lục 80 triệu Euro.");
            listNews.add(mNews);
            mNews = new News("Dàn máy bay Nga bị tiêm kích F-16 của Na Uy bám đuôi", "https://upload.wikimedia.org/wikipedia/commons/2/2e/European-parliament-brussels-inside.JPG", "Champions League. Nhưng thực tình là tôi ái mộ lịch sử của Liverpool, CLB sở hữu 5 chiếc cúp Champions League trong phòng truyền thống của họ. Tôi thực sự muốn là một phần trong lích sử của họ");
            listNews.add(mNews);
//            mNews = new News("Hoa khôi Sinh viên Việt Nam áo đỏ sao vàng cổ vũ tuyển Việt Nam", "https://upload.wikimedia.org/wikipedia/commons/7/7b/Hollywood_sign_hill_view.jpg");
//            listNews.add(mNews);
//            mNews = new News("Xiếc Việt bị bỏ rơi trên sân nhà?", "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg");
//            listNews.add(mNews);
//            mNews = new News("Tòa án Trung Quốc ra phán quyết cấm bán loạt iPhone", "https://upload.wikimedia.org/wikipedia/commons/7/7b/Hollywood_sign_hill_view.jpg");
//            listNews.add(mNews);
//            mNews = new News("MBên trong nhà máy sản xuất điện thoại của Vingroup", "https://upload.wikimedia.org/wikipedia/commons/5/50/Laurent_Koscielny_clashes_with_Heurelho_Gomes.jpg");
//            listNews.add(mNews);
//            mNews = new News("Các hãng công nghệ lớn nắm những thông tin nào của người dùng?", "https://upload.wikimedia.org/wikipedia/commons/d/d3/IBM_Blue_Gene_P_supercomputer.jpg");
//            listNews.add(mNews);
        }
        return listNews;
    }
}
