package com.example.myapplication.domain;

public class People extends Item {
    private int id;
    private String name;
    private int payment;

    public People() {
    }

    public People(int id, String name, int payment) {
        this.id = id;
        this.name = name;
        this.payment = payment;
    }

    public int getId() {
        return this.id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPayment(int payment) {
        this.payment = payment;
    }

    public int getPayment() {
        return this.payment;
    }
}
