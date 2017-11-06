package com.jinghanit.jcalendar;

public class CalendarModel {

    public int day;//几号
    public boolean isSelect;//是否选中
    public boolean isClick;//是否可点击

    public CalendarModel(int day, boolean isSelect, boolean isClick) {
        this.day = day;
        this.isSelect = isSelect;
        this.isClick = isClick;
    }

}
