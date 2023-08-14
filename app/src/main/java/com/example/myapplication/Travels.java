package com.example.myapplication;

import java.util.ArrayList;

public class Travels {
    private static ArrayList<Travel> travels = null;

    private Travels(){}

    public static ArrayList<Travel> getInstance() {
        if (travels == null) {
            travels = new ArrayList<Travel>();
            Travel addButton = new Travel();
            addButton.setViewType(1);
            travels.add(addButton);
        }
        return travels;
    }
}
