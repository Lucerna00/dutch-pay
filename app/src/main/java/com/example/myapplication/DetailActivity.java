package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {
    DBManager dbManager;
    ArrayList<Travel> travels = Travels.getInstance();
    String destination = "";
    String date = "";
    ArrayList<Item> peoples = new ArrayList<>();
    ArrayList<Item> receipts = new ArrayList<>();
    boolean isAdd = false;
    TextView tvTotal = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        dbManager = new DBManager(DetailActivity.this, "newDB.db", null, 1);
        SQLiteDatabase db = dbManager.getReadableDatabase();
        dbManager.onCreate(db);

        TextView tvDest = findViewById(R.id.detail_destination);
        tvTotal = findViewById(R.id.total_amount);

        Intent intent = getIntent();
        int getId = intent.getExtras().getInt("id");

        People addPeopleButton = new People();
        addPeopleButton.setViewType(1);
        this.peoples.add(addPeopleButton);

        Receipt addReceiptButton = new Receipt();
        addReceiptButton.setViewType(3);
        this.receipts.add(addReceiptButton);

        if(getId == 0) { // 새로운 travel
            String getDest = intent.getStringExtra("dest");
            String getDate = intent.getStringExtra("date");
            tvDest.setText(getDest);
            this.destination = getDest;
            this.date = getDate;
            this.isAdd = true;
        }
        else { // 기존 travel
            Travel travel = travels.get(getId-1);
            this.destination = travel.getDestination();
            this.date = travel.getDate();

            // 여기서 db에서 들고와야됨(select + while)
            dbManager.select(db, this.peoples, this.receipts);
            db.close();
        }

        RecyclerView recyclerView = findViewById(R.id.detail_list);
        DetailAdapter detailAdapter = new DetailAdapter(dbManager, peoples, receipts, tvTotal);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(detailAdapter);

        overridePendingTransition(R.anim.vertical_enter, R.anim.none);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        if(this.isAdd) {
            Travel travel = new Travel(destination, date, peoples, receipts);
            travels.add(travels.size() - 1, travel);
        }

        dbManager.close();

        overridePendingTransition(R.anim.none, R.anim.vertical_exit);
    }
}