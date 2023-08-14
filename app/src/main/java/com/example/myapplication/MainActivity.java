package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    MainAdapter mainAdapter;
    ArrayList<Travel> travels = Travels.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(travels.size() == 1)
            createDate();

        recyclerView = findViewById(R.id.travel_list);
        mainAdapter = new MainAdapter(travels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(mainAdapter);
    }

    public void createDate(){
        String destination = "일본";
        String date = "08.23~08.26";

        ArrayList<Item> peoples = new ArrayList<>();
        peoples.add(new People(0, "박준석", 42000));
        peoples.add(new People(1, "구하은", 40000));
        People addPeopleButton = new People();
        addPeopleButton.setViewType(1);
        peoples.add(addPeopleButton);

        ArrayList<Item> receipts = new ArrayList<>();
        Receipt addReceiptButton = new Receipt();
        addReceiptButton.setViewType(3);
        receipts.add(addReceiptButton);
        receipts.add(new Receipt(0, "세븐일레븐", 1200));
        receipts.add(new Receipt(1, "돈키호테", 10000));

        travels.add(travels.size()-1, new Travel(destination, date, peoples, receipts));
    }

    @Override
    public void onRestart(){
        super.onRestart();
        recyclerView.setAdapter(mainAdapter);
    }
}