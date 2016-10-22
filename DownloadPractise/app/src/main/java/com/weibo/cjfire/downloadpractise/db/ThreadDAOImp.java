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
    private SQLiteDatabase mDB = null;

    public ThreadDAOImp(Context context) {
        mHelper = new DBHelper(context);
        mDB = mHelper.getWritableDatabase();
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        mDB.execSQL("INSERT INTO thread_info(thread_id, url, start, end, finished) VALUES(?,?,?,?,?)", new Object[](threadInfo.getId(), threadInfo.getUrl(), threadInfo.getStart(), threadInfo.getEnd(), threadInfo.getFinished()));
        mDB.close();
    }

    @Override
    public void deleteThread(String url, int thread_id) {
        mDB.execSQL("DELETE From thread_info WHERE thread_id = ? AND url = ?", new Object[](thread_id, url));
        mDB.close();
    }

    @Override
    public void updateThreat(String url, int thread_id, int finished) {
        mDB.execSQL("UPDATE thread_info SET finished = ? WHERE url = ? AND thread_id = ?", new Object[](finished, url, thread_id));
        mDB.close();
    }

    @Override
    public List<ThreadInfo> getThread(String url) {

        List<ThreadInfo> list = new ArrayList<>();
        Cursor cursor = mDB.rawQuery("SELECT * FROM thread_info WHERE url = ?", new String[](url))

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
        mDB.close();
        return list;
    }

    @Override
    public boolean isExists(String url, int thread_id) {

        Cursor cursor = mDB.rawQuery("SELECT * FROM thread_info WHERE url = ? AND thread_id = ?", new Object[](url, thread_id))

        boolean exist = cursor.moveToNext();

        cursor.close();
        mDB.close();

        return exist;
    }
}
