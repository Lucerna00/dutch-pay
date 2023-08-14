package com.example.myapplication;

import com.example.myapplication.Item;

public class Receipt implements Item {
    private int id;
    private String product;
    private int amount;
    private int viewType = 2;

    public Receipt(){}

    public Receipt(int id, String name, int amount){
        this.id = id;
        this.product = name;
        this.amount = amount;
    }

    public int getId(){
        return this.id;
    }

    public void setProduct(String product){
        this.product = product;
    }

    public String getProduct(){
        return this.product;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getAmount(){
        return this.amount;
    }

    public void setViewType(int viewType){
        this.viewType = viewType;
    }

    public int getViewType(){
        return this.viewType;
    }
}
