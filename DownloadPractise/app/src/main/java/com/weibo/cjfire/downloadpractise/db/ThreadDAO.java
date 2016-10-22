package com.weibo.cjfire.downloadpractise.db;

import com.weibo.cjfire.downloadpractise.entities.ThreadInfo;

import java.util.List;

/**
 * Created by cjfire on 16/10/22.
 */

public interface ThreadDAO {

    public void insertThread(ThreadInfo threadInfo);
    public void deleteThread(String url, int thread_id);
    public void updateThreat(String url, int thread_id, int finished);
    public List<ThreadInfo>getThread(String url);
    public boolean isExists(String url, int thread_id);
}
