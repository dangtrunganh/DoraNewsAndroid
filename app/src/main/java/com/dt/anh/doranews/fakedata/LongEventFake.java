package com.dt.anh.doranews.fakedata;

import com.dt.anh.doranews.model.Event;
import com.dt.anh.doranews.model.LongEvent;

import java.util.ArrayList;

public class LongEventFake {
    private static ArrayList<LongEvent> listLongEvent;
    public static ArrayList<LongEvent> getListLongEvent() {
        if (listLongEvent == null) {
            listLongEvent = new ArrayList<>();

            //==================
            LongEvent mLongEvent = new LongEvent("12/1/2019", "Dàn máy bay Nga bị tiêm kích F-16 của Na Uy bám đuôi");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng Vượt mốc 120.000 xe, Mazda tri ân khách hàng lên đến 30 triệu đồng");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Dàn máy bay Nga bị tiêm kích F-16 của Na Uy bám đuôi");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Hoa khôi Sinh viên Việt Nam áo đỏ sao vàng cổ vũ tuyển Việt Nam");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Xiếc Việt bị bỏ rơi trên sân nhà?");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Tòa án Trung Quốc ra phán quyết cấm bán loạt iPhone");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "MBên trong nhà máy sản xuất điện thoại của Vingroup");
            listLongEvent.add(mLongEvent);
            mLongEvent = new LongEvent("2/2/2019", "Các hãng công nghệ lớn nắm những thông tin nào của người dùng?");
            listLongEvent.add(mLongEvent);
        }
        return listLongEvent;
    }
}
