package com.example.myapplication;

import com.example.myapplication.Item;

import java.util.ArrayList;

public class Travel {
    private String destination = "";
    private String date = "";
    private ArrayList<Item> peoples = null;
    private ArrayList<Item> receipts = null;
    private int viewType = 0;

    public Travel(){}
    public Travel(String destination, String date, ArrayList<Item> peoples, ArrayList<Item> receipts){
        this.destination = destination;
        this.date = date;
        this.peoples = peoples;
        this.receipts = receipts;
    }

    public String getDestination(){
        return this.destination;
    }

    public String getDate(){
        return this.date;
    }

    public ArrayList<Item> getPeoples(){
        return this.peoples;
    }

    public ArrayList<Item> getReceipts(){
        return this.receipts;
    }

    public void setViewType(int viewType){
        this.viewType = viewType;
    }

    public int getViewType(){
        return this.viewType;
    }
}
