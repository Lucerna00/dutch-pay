package com.example.myapplication;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DetailAdapter extends RecyclerView.Adapter {
    DBManager dbManager;
    ArrayList<Item> peoples;
    ArrayList<Item> receipts;
    ArrayList<Item> items;
    TextView tvTotal;


    public DetailAdapter(DBManager dbManager, ArrayList<Item> peoples, ArrayList<Item> receipts, TextView tvTotal){
        this.dbManager = dbManager;
        this.peoples = peoples;
        this.receipts = receipts;
        makeItems(peoples, receipts);
        this.tvTotal = tvTotal;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        totalUpdate();

        switch (viewType){
            case 0: { // 사람
                View view = inflater.inflate(R.layout.people, parent, false);
                return new PeopleViewHolder(view);
            }
            case 1: { // 사람 추가
                View view = inflater.inflate(R.layout.detail_add, parent, false);
                return new AddPeopleViewHolder(view);
            }
            case 2: { // 영수증
                View view = inflater.inflate(R.layout.receipt, parent, false);
                return new ReceiptViewHolder(view);
            }
            case 3: { // 영수증 추가
                View view = inflater.inflate(R.layout.detail_add, parent, false);
                return new AddReceiptViewHolder(view);
            }
            default: { // 구분선
                View view = inflater.inflate(R.layout.divider, parent, false);
                return new DividerViewHolder(view);
            }
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        Item item = items.get(position);
        int split = (int)(Double.parseDouble(tvTotal.getText().toString())/(peoples.size()-1));

        switch (item.getViewType()) {
            case 0: { // 사람
                SQLiteDatabase db = dbManager.getWritableDatabase();
                PeopleViewHolder peopleViewHolder = (PeopleViewHolder) holder;
                People people = (People)(items.get(position));
                peopleViewHolder.name.setText(people.getName());
                peopleViewHolder.payment.setText(Integer.toString(people.getPayment()));

                int diff = people.getPayment()-split;
                int color = ContextCompat.getColor(peopleViewHolder.cardView.getContext(), R.color.black);

                if(diff > 0) {
                    color = ContextCompat.getColor(peopleViewHolder.cardView.getContext(), R.color.blue);
                }
                else if(diff < 0) {
                    color = ContextCompat.getColor(peopleViewHolder.cardView.getContext(), R.color.red);
                    diff *= -1;
                }
                peopleViewHolder.difference.setText(Integer.toString(diff));
                peopleViewHolder.difference.setTextColor(color);
                peopleViewHolder.differenceUnit.setTextColor(color);

                peopleViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        View dialogView = View.inflate(view.getContext(), R.layout.people_dialog, null);
                        EditText etName = dialogView.findViewById(R.id.et_name);
                        EditText etPayment = dialogView.findViewById(R.id.et_payment);
                        etName.setText(people.getName());
                        etPayment.setText(Integer.toString(people.getPayment()));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("이름과 지불액을 입력하세요");
                        builder.setView(dialogView);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String name = etName.getText().toString();
                                int payment = Integer.parseInt(etPayment.getText().toString());

                                people.setName(name);
                                people.setPayment(payment);

                                // db에 반영
                                dbManager.updatePeople(db, people.getId(), name, payment);

                                makeItems(peoples, receipts);

                                notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        builder.create().setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {

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

                peopleViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) { // 사람 삭제
                        Context context = view.getContext();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("삭제하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // db에 삭제
                                dbManager.deletePeople(db, people.getId());

                                peoples.remove(people);

                                makeItems(peoples, receipts);

                                notifyDataSetChanged();

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
                        return true;
                    }
                });
                break;
            }
            case 1: { // 사람 추가
                SQLiteDatabase db = dbManager.getWritableDatabase();
                AddPeopleViewHolder addPeopleViewHolder = (AddPeopleViewHolder) holder;
                addPeopleViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        View dialogView = View.inflate(view.getContext(), R.layout.people_dialog, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("이름과 지불액을 입력하세요");
                        builder.setView(dialogView);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etName = dialogView.findViewById(R.id.et_name);
                                EditText etPayment = dialogView.findViewById(R.id.et_payment);
                                String name = etName.getText().toString();
                                int payment = Integer.parseInt(etPayment.getText().toString());

                                // db에 저장
                                dbManager.insertPeople(db, name, payment);

                                Item newPeople = new People(dbManager.getPeopleKey(db), name, payment);
                                peoples.add(peoples.size()-1, newPeople);

                                makeItems(peoples, receipts);

                                notifyDataSetChanged();

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
                break;
            }
            case 2: { // 영수증
                SQLiteDatabase db = dbManager.getWritableDatabase();
                ReceiptViewHolder receiptViewHolder = (ReceiptViewHolder) holder;
                Receipt receipt = (Receipt)(items.get(position));
                receiptViewHolder.product.setText(receipt.getProduct());
                receiptViewHolder.amount.setText(Integer.toString(receipt.getAmount()));

                receiptViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        View dialogView = View.inflate(view.getContext(), R.layout.receipt_dialog, null);
                        EditText etProduct = dialogView.findViewById(R.id.et_product);
                        EditText etAmount = dialogView.findViewById(R.id.et_amount);
                        etProduct.setText(receipt.getProduct());
                        etAmount.setText(Integer.toString(receipt.getAmount()));

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("상품명과 금액을 입력하세요");
                        builder.setView(dialogView);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String product = etProduct.getText().toString();
                                int amount = Integer.parseInt(etAmount.getText().toString());

                                receipt.setProduct(product);
                                receipt.setAmount(amount);

                                // db에 반영
                                dbManager.updateReceipt(db, receipt.getId(), product, amount);

                                makeItems(peoples, receipts);

                                totalUpdate();

                                notifyDataSetChanged();

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

                receiptViewHolder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) { // 사람 삭제
                        Context context = view.getContext();

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("삭제하시겠습니까?");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // db에 삭제
                                dbManager.deleteReceipt(db, receipt.getId());

                                receipts.remove(receipt);

                                makeItems(peoples, receipts);

                                totalUpdate();

                                notifyDataSetChanged();

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
                        return true;
                    }
                });
                break;
            }
            case 3: { // 영수증 추가
                SQLiteDatabase db = dbManager.getWritableDatabase();
                AddReceiptViewHolder addReceiptViewHolder = (AddReceiptViewHolder) holder;
                addReceiptViewHolder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Context context = view.getContext();
                        View dialogView = View.inflate(view.getContext(), R.layout.receipt_dialog, null);

                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                        builder.setTitle("상품명과 금액을 입력하세요");
                        builder.setView(dialogView);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                EditText etProduct = dialogView.findViewById(R.id.et_product);
                                EditText etAmount = dialogView.findViewById(R.id.et_amount);
                                String product = etProduct.getText().toString();
                                int amount = Integer.parseInt(etAmount.getText().toString());

                                // db에 저장
                                dbManager.insertReceipt(db, product, amount);

                                Item newReceipt = new Receipt(dbManager.getReceiptKey(db), product, amount);
                                receipts.add(1, newReceipt);

                                makeItems(peoples, receipts);

                                totalUpdate();

                                notifyDataSetChanged();

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
                break;
            }
        }
    }

    public void makeItems(ArrayList<Item> peoples, ArrayList<Item> receipts) {
        this.items = new ArrayList<Item>();
        this.items.addAll(peoples);
        People divider = new People();
        divider.setViewType(4);
        this.items.add(divider);
        this.items.addAll(receipts);
    }

    public void totalUpdate() {
        int totalAmount = 0;

        for(int i=1;i<receipts.size();i++){
            totalAmount += ((Receipt)(receipts.get(i))).getAmount();
        }
        tvTotal.setText(Integer.toString(totalAmount));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }

    class PeopleViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView name, payment, difference, differenceUnit;

        public PeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.people_layout);
            name = itemView.findViewById(R.id.name);
            payment = itemView.findViewById(R.id.payment);
            difference = itemView.findViewById(R.id.difference);
            differenceUnit = itemView.findViewById(R.id.difference_unit);
        }
    }

    class AddPeopleViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public AddPeopleViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.detail_add_layout);
        }
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView product, amount;

        public ReceiptViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.receipt_layout);
            product = itemView.findViewById(R.id.product);
            amount = itemView.findViewById(R.id.amount);
        }
    }

    class AddReceiptViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public AddReceiptViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.detail_add_layout);
        }
    }

    class DividerViewHolder extends RecyclerView.ViewHolder {
        public DividerViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}