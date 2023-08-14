package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter {
    ArrayList<Travel> travels;

    public MainAdapter(ArrayList<Travel> travels) {
        this.travels = travels;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        switch (viewType){
            case 1: {
                View view = inflater.inflate(R.layout.travel_add, parent, false);
                return new AddViewHolder(view);
            }
            default: {
                View view = inflater.inflate(R.layout.travel_card, parent, false);
                return new CardViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Travel travel = travels.get(position);

        if(travel.getViewType() == 1) { // 새로 추가
            AddViewHolder addViewHolder = (AddViewHolder)holder;
            addViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    View dialogView = View.inflate(view.getContext(), R.layout.dialog, null);

                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("여행지와 날짜를 입력하세요");
                    builder.setView(dialogView);
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            EditText etDest = dialogView.findViewById(R.id.et_destination);
                            EditText etDate = dialogView.findViewById(R.id.et_date);

                            Intent detailActivity = new Intent(context, DetailActivity.class);

                            detailActivity.putExtra("dest", etDest.getText().toString());
                            detailActivity.putExtra("date", etDate.getText().toString());
                            ((MainActivity)context).startActivity(detailActivity);

                            dialog.dismiss();
                        }
                    });
                    builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog ad = builder.create();
                    ad.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            ad.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
                            ad.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.lightBlue));
                        }
                    });
                    ad.show();
                }
            });
        }
        else{ // 기존 선택
            CardViewHolder cardViewHolder = (CardViewHolder)holder;
            cardViewHolder.setItem(travel);

            cardViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent detailActivity = new Intent(context, DetailActivity.class);

                    detailActivity.putExtra("id", position + 1);
                    ((MainActivity)context).startActivity(detailActivity);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return travels.size();
    }

    @Override
    public int getItemViewType(int position) {
        return travels.get(position).getViewType();
    }

    class CardViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView destination, date, peoples;

        public CardViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.card_layout);
            destination = itemView.findViewById(R.id.destination);
            date = itemView.findViewById(R.id.date);
            peoples = itemView.findViewById(R.id.people);
        }

        public void setItem(Travel travel){
            destination.setText(travel.getDestination());
            date.setText(travel.getDate());

            String str = "";
            if(travel.getPeoples().size() > 0) {
                str = ((People)(travel.getPeoples().get(0))).getName();
                int cnt = travel.getPeoples().size() - 2;
                if (cnt > 0) {
                    str += (" 외 " + Integer.toString(cnt) + "명");
                }
            }
            peoples.setText(str);
        }
    }

    class AddViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.add_layout);
        }
    }
}