package com.hulian.firstpage.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {
    public static final String MODEL_TABLE = "model";
    public static final String MODEL_COMPONET_TABLE = "modelComponet";
    public static final String SIMPLESENCE_TABLE = "simpleSence";
    public static final String SENCE_COMPONET_TABLE = "senceComponet";
    public static final String SENCE_PAGE_TABLE = "sencePage";

    public DBOpenHelper(Context context) {
        super(context, "firstPage.db", null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + MODEL_TABLE + " (_id integer primary key autoincrement,code text,id integer,name text,cover text)");
        db.execSQL("create table " + MODEL_COMPONET_TABLE + " (_id integer primary key autoincrement,id integer,type text,z_index integer,x integer,y integer,w integer,h integer,integer,anchor_x integer,anchor_y integer,pic_mode text,pic_num integer,pics text,modelId integer)");
        db.execSQL("create table " + SENCE_PAGE_TABLE + " (_id integer primary key autoincrement,id text,pageIndex integer,senceID text,template_id integer)");
        db.execSQL("create table " + SIMPLESENCE_TABLE + " (_id integer primary key autoincrement,id text,title text,view_times integer,share_times integer,upload_time integer,cover text,description text,music text,isPublic text,isRecommond text,shareUri boolean ,viewUri text,isUpload text)");
        db.execSQL("create table " + SENCE_COMPONET_TABLE + " (_id integer primary key autoincrement,id text,type text,z_index integer,x integer,y integer,w integer,h integer,integer,anchor_x integer,anchor_y integer,rotate real,pic_mode text,pic_num integer,pics text,senceID text,text_content text,text_color integer,pageIndex integer)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
    }

}
