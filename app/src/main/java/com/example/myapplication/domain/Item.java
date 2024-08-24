package com.example.myapplication.domain;

public class Item {
    public static final int TRAVEL_ADD = 1;
    public static final int PEOPLE_VIEW = 0;
    public static final int PEOPLE_ADD = 1;
    public static final int RECEIPT_VIEW = 2;
    public static final int RECEIPT_ADD = 3;

    private int viewType;

    public Item(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}
