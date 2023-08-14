package com.example.myapplication;

import com.example.myapplication.Item;

public class People implements Item {
    private int id;
    private String name;
    private int payment;
    private int viewType = 0;

    public People(){}

    public People(int id, String name, int payment){
        this.id = id;
        this.name = name;
        this.payment = payment;
    }

    public int getId(){
        return this.id;
    }

    public void setName(String name){
        this.name = name;
    }

    public String getName(){
        return this.name;
    }

    public void setPayment(int payment){
        this.payment = payment;
    }

    public int getPayment(){
        return this.payment;
    }

    public void setViewType(int viewType){
        this.viewType = viewType;
    }

    public int getViewType(){
        return this.viewType;
    }
}
