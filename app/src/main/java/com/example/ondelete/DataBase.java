package com.example.ondelete;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBase extends SQLiteOpenHelper {

    public static final String db_name = "Clicker";
    public static final int db_version = 4;
    public static final String db_table_main = "Score";
    public static final String db_column_main = "ScoreCount";
    public static final String db_column_cakes = "CakesCount";
    public static final String db_column_ice = "IceCreamCount";
    public static final String db_column_nut = "NutCount";
    public static final String db_column_general = "GeneralCount";

    public DataBase(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query_main = String.format("CREATE TABLE %s (ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                " %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL, %s INTEGER NOT NULL);",db_table_main,db_column_main,db_column_cakes,db_column_ice,db_column_nut,db_column_general);
        db.execSQL(query_main);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query_main = String.format("DROP TABLE IF EXISTS %s",db_table_main);
        db.execSQL(query_main);
        onCreate(db);
    }

    /** Add new data in main table */
    public void insertData(int data1, int data2, int data3, int data4, int data5){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_main, data1);
        values.put(db_column_cakes, data2);
        values.put(db_column_ice, data3);
        values.put(db_column_nut, data4);
        values.put(db_column_general, data5);
        db.insertWithOnConflict(db_table_main, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    /** Update data in main table */
    public void updateData(String column, int value){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = String.format("UPDATE %s SET %s = %d",db_table_main,column,value);
        db.execSQL(query);
    }

    /** Get count of data in main table */
    public boolean getAllData(){
        final String query = String.format("select count(*) as count from %s",db_table_main);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int tmp = cursor.getInt(cursor.getColumnIndex("count"));
        if (tmp<=0) {
            cursor.close();
            return true;
        }else {
            cursor.close();
            return false;
        }
    }

    /** Select first column from main table */
    public int getData(String column){
        final String query = String.format("select %s from %s", column,db_table_main);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        int tmp = cursor.getInt(cursor.getColumnIndex(column));
        cursor.close();
        Log.i("CURSOR", tmp+"");
        return tmp;
    }
}
