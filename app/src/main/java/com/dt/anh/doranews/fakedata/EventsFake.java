package com.dt.anh.doranews.fakedata;

import com.dt.anh.doranews.model.Event;

import java.util.ArrayList;

public class EventsFake {
    private static ArrayList<Event> listEvent;
    public static ArrayList<Event> getListEvent() {
        if (listEvent == null) {
            listEvent = new ArrayList<>();

            //==================
            Event mEvent = new Event("CĐV Việt Nam sang Malaysia dù chưa mua được vé vào sân Bukit Jalil", "https://upload.wikimedia.org/wikipedia/commons/f/fb/Football_trip.jpg", 20, "2 ngày trước", "THỂ THAO");
            listEvent.add(mEvent);
            mEvent = new Event("Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng", "https://upload.wikimedia.org/wikipedia/commons/d/d3/Japan-1454396_640.jpg", 20, "3 ngày trước", "THỂ THAO");
            listEvent.add(mEvent);
            mEvent = new Event("Dàn máy bay Nga bị tiêm kích F-16 của Na Uy bám đuôi", "https://upload.wikimedia.org/wikipedia/commons/2/2e/European-parliament-brussels-inside.JPG", 15, "4 ngày trước", "XÃ HỘI");
            listEvent.add(mEvent);
            mEvent = new Event("Hoa khôi Sinh viên Việt Nam áo đỏ sao vàng cổ vũ tuyển Việt Nam", "", 25, "3 ngày trước", "THỂ THAO");
            listEvent.add(mEvent);
            mEvent = new Event("Xiếc Việt bị bỏ rơi trên sân nhà?", "https://upload.wikimedia.org/wikipedia/commons/3/31/New_McDonald%27s_restaurant_in_Mount_Pleasant%2C_Iowa.jpg", 14, "2 ngày trước", "VĂN HÓA");
            listEvent.add(mEvent);
            mEvent = new Event("Tòa án Trung Quốc ra phán quyết cấm bán loạt iPhone", "https://upload.wikimedia.org/wikipedia/commons/7/7b/Hollywood_sign_hill_view.jpg", 32, "5 ngày trước", "ÂM NHẠC");
            listEvent.add(mEvent);
            mEvent = new Event("MBên trong nhà máy sản xuất điện thoại của Vingroup", "https://upload.wikimedia.org/wikipedia/commons/5/50/Laurent_Koscielny_clashes_with_Heurelho_Gomes.jpg", 12, "6 ngày trước", "THỂ THAO");
            listEvent.add(mEvent);
            mEvent = new Event("Các hãng công nghệ lớn nắm những thông tin nào của người dùng?", "https://upload.wikimedia.org/wikipedia/commons/d/d3/IBM_Blue_Gene_P_supercomputer.jpg", 18, "8 ngày trước", "PHÁP LUẬT");
            listEvent.add(mEvent);
        }
        return listEvent;
    }

}