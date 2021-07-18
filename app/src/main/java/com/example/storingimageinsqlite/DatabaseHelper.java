package com.example.storingimageinsqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static String database_name = "name.db";
    public static String table_name = "tableimage";


    public DatabaseHelper(Context context) {
        super(context, database_name, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table "+table_name+" (name text, image blob);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+table_name);
    }

    public boolean insert(String name,byte [] img){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",name);
        contentValues.put("image",img);
        long inserted = db.insert(table_name,null,contentValues);
        if(inserted==-1){
            return false;
        }
        else{
            return true;
        }
    }

    public Cursor ViewData(){
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cur=db.rawQuery("select * from "+table_name,null);
        Log.d("TAG","Inside Show...........1");
        return cur;
    }



}
