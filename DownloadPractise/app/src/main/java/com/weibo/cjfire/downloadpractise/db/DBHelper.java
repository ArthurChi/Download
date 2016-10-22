package com.weibo.cjfire.downloadpractise.db;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cjfire on 16/10/22.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "download.db";
    private static int VERSION = "1";
    private static final String SQL_CREATE = "create table if exists thread_info(id integer primary key autoincrement, " +
    "thread_id integer, url text, start integer, end integer, finishe integer)";
    private static final String SQL_DROP = "drop table if exists thread_info";

    public DBHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP);
        db.execSQL(SQL_CREATE);
    }
}
