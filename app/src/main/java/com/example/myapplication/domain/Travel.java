package com.example.myapplication.domain;

import java.util.ArrayList;

public class Travel extends Item {
    private String destination = "";
    private String date = "";
    private ArrayList<Item> peoples = null;
    private ArrayList<Item> receipts = null;

    public Travel() {
    }

    public Travel(String destination, String date, ArrayList<Item> peoples, ArrayList<Item> receipts) {
        this.destination = destination;
        this.date = date;
        this.peoples = peoples;
        this.receipts = receipts;
    }

    public String getDestination() {
        return this.destination;
    }

    public String getDate() {
        return this.date;
    }

    public ArrayList<Item> getPeoples() {
        return this.peoples;
    }

    public ArrayList<Item> getReceipts() {
        return this.receipts;
    }
}
