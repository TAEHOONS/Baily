package com.example.baily;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBlink extends SQLiteOpenHelper{
    public DBlink(Context context, String name,
                  CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 사용자 id
        String sql = "create table user" +
                "(_id integer primary key autoincrement," +
                "id text,pw text,name text, email text,lastbaby text);";
        db.execSQL(sql);
        // 로그인 정보
        sql = "create table thisusing" +
                "(_id integer primary key autoincrement," +
                "id text,baby text,diaryset Integer);";
        db.execSQL(sql);
        // 아기 정보
        sql = "create table baby" +
                "(_id integer primary key autoincrement," +
                "name text,sex text,ybirth Integer,mbirth Integer,dbirthy Integer,headline text" +
                ",tall text,weight text,parents text,imgpath text);";
        db.execSQL(sql);
        // 아기 정보 누적
        sql = "create table growlog" +
                "(_id integer primary key autoincrement," +
                "name text,weight text,tall text,headline text,fever text" +
                ",date text,caldate text,parents text);";
        db.execSQL(sql);
        sql = "CREATE TABLE events " +
                "( id INTEGER PRIMARY KEY AUTOINCREMENT , " +
                "name TEXT,title text, date TEXT, memo TEXT,parents text);";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "drop table if exists student;";
        db.execSQL(sql);
        onCreate(db); // 다시 테이블 생성
    }
}