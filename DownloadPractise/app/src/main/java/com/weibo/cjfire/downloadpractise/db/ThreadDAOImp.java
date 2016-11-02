package com.weibo.cjfire.downloadpractise.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.weibo.cjfire.downloadpractise.entities.ThreadInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cjfire on 16/10/22.
 */

public class ThreadDAOImp implements ThreadDAO {

    private static final String TABLE_NAME = "thread_info";
    private DBHelper mHelper = null;

    public ThreadDAOImp(Context context) {
        mHelper = new DBHelper(context);
    }

    @Override
    public Boolean insertThread(ThreadInfo threadInfo) {

        long result = -1;

        ContentValues values = new ContentValues();

        values.put("thread_id", threadInfo.getId());
        values.put("url", threadInfo.getUrl());
        values.put("start", threadInfo.getStart());
        values.put("end", threadInfo.getEnd());
        values.put("finished", threadInfo.getFinished());

        SQLiteDatabase db = mHelper.getWritableDatabase();
        result = db.insert(TABLE_NAME, null, values);
        db.close();

        return result == -1 ? false : true;
    }

    @Override
    public Boolean deleteThread(int id, String url) {

        int affectRows = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();
        affectRows = db.delete(TABLE_NAME, "thread_id = ? AND url = ?", new String[] {String.valueOf(id), url} );
        db.close();

        return affectRows == 0 ? false : true;
    }

    @Override
    public Boolean updateThreat(ThreadInfo threadInfo) {

        int affectRows = 0;
        SQLiteDatabase db = mHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("finished", threadInfo.getFinished());

        affectRows = db.update(TABLE_NAME, values, "url = ? AND thread_id = ?", new String[] {threadInfo.getUrl(), String.valueOf(threadInfo.getId())});
        db.close();

        return affectRows == 0 ? false : true;
    }

    @Override
    public List<ThreadInfo> getThread(String url) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        List<ThreadInfo> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME, null, null, null, null, null, null);

        while (cursor.moveToNext()) {

            ThreadInfo threadInfo = new ThreadInfo();
            threadInfo.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            threadInfo.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            threadInfo.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            threadInfo.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            threadInfo.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(threadInfo);
        }

        cursor.close();
        db.close();
        return list;
    }

    @Override
    public Boolean isExists(int id, String url) {

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, "url = ? AND thread_id = ?", new String[] {url, String.valueOf(id)}, null, null, null);

        boolean exist = cursor.moveToNext();

        cursor.close();
        db.close();

        return exist;
    }
}
