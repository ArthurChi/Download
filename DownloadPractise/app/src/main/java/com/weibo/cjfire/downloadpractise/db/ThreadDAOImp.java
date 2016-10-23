package com.weibo.cjfire.downloadpractise.db;

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

    private DBHelper mHelper = null;

    public ThreadDAOImp(Context context) {
        mHelper = new DBHelper(context);
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("INSERT INTO thread_info (thread_id, url, start, end, finished) VALUES(?,?,?,?,?)" ,
                new Object[]{
                threadInfo.getId(),
                threadInfo.getUrl(),
                threadInfo.getStart(),
                threadInfo.getEnd(),
                threadInfo.getFinished()});
        db.close();
    }

    @Override
    public void deleteThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("DELETE From thread_info WHERE thread_id = ? AND url = ?",
                new Object[]{threadInfo.getId(), threadInfo.getUrl()});
        db.close();
    }

    @Override
    public void updateThreat(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();
        db.execSQL("UPDATE thread_info SET finished = ? WHERE url = ? AND thread_id = ?",
                new Object[]{threadInfo.getFinished(), threadInfo.getUrl(), threadInfo.getId()});
        db.close();
    }

    @Override
    public List<ThreadInfo> getThread(String url) {

        SQLiteDatabase db = mHelper.getReadableDatabase();
        List<ThreadInfo> list = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM thread_info WHERE url = ?",
                new String[]{url});

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
    public boolean isExists(ThreadInfo threadInfo) {

        SQLiteDatabase db = mHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery("SELECT * FROM thread_info WHERE url = ? AND thread_id = ? ",
                new String[]{threadInfo.getUrl(), String.valueOf(threadInfo.getId())});

        boolean exist = cursor.moveToNext();

        cursor.close();
        db.close();

        return exist;
    }
}
