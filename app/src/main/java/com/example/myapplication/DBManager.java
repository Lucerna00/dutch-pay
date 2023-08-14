package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBManager extends SQLiteOpenHelper {
    public DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "CREATE TABLE if not exists people ("
                + "id integer primary key AUTOINCREMENT,"
                + "name text,"
                + "payment integer);";
        String sql2 = "CREATE TABLE if not exists receipt ("
                + "id integer primary key AUTOINCREMENT,"
                + "product text,"
                + "amount integer);";

        db.execSQL(sql1);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE if exists people;";
        String sql2 = "DROP TABLE if exists receipt;";

        db.execSQL(sql1);
        db.execSQL(sql2);
        onCreate(db);
    }

    public void insertPeople(SQLiteDatabase db, String name, int payment){
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("payment", payment);

        db.insert("people", null, contentValues);
    }

    public void insertReceipt(SQLiteDatabase db, String product, int amount){
        ContentValues contentValues = new ContentValues();
        contentValues.put("product", product);
        contentValues.put("amount", amount);

        db.insert("receipt", null, contentValues);
    }

    public void updatePeople(SQLiteDatabase db, int key, String name, int payment){
        String sql = "UPDATE people SET name='" + name + "', payment='" + payment + "' WHERE id=" + key;
        db.execSQL(sql);
    }

    public void updateReceipt(SQLiteDatabase db, int key, String product, int amount){
        String sql = "UPDATE receipt SET product='" + product + "', amount='" + amount + "' WHERE id=" + key;
        db.execSQL(sql);
    }

    public void select(SQLiteDatabase db, ArrayList<Item> peoples, ArrayList<Item> receipts){
        String sql1 = "SELECT * FROM people";
        String sql2 = "SELECT * FROM receipt";

        Cursor c1 = db.rawQuery(sql1, null);
        while(c1.moveToNext()){
            Item people = new People(Integer.parseInt(c1.getString(0)), c1.getString(1), c1.getInt(2));
            peoples.add(peoples.size()-1, people);
        }

        Cursor c2 = db.rawQuery(sql2, null);
        while(c2.moveToNext()){
            Item receipt = new Receipt(Integer.parseInt(c2.getString(0)), c2.getString(1), c2.getInt(2));
            receipts.add(1, receipt);
        }
    }

    public int getPeopleKey(SQLiteDatabase db){
        String sql = "SELECT seq FROM sqlite_sequence WHERE name = 'people'";

        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();
        return Integer.parseInt(c.getString(0));
    }

    public int getReceiptKey(SQLiteDatabase db){
        String sql = "SELECT seq FROM sqlite_sequence WHERE name = 'receipt'";

        Cursor c = db.rawQuery(sql, null);
        c.moveToNext();
        return Integer.parseInt(c.getString(0));
    }

    public void deletePeople(SQLiteDatabase db, int key){
        String sql = "DELETE FROM people WHERE id = " + key;
        db.execSQL(sql);
    }

    public void deleteReceipt(SQLiteDatabase db, int key){
        String sql = "DELETE FROM receipt WHERE id = " + key;
        db.execSQL(sql);
    }
}