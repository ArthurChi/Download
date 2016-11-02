package com.weibo.cjfire.downloadpractise.db;

import com.weibo.cjfire.downloadpractise.entities.ThreadInfo;

import java.util.List;

/**
 * Created by cjfire on 16/10/22.
 */

public interface ThreadDAO {

    public Boolean insertThread(ThreadInfo threadInfo);
    public Boolean deleteThread(int id, String url);
    public Boolean updateThreat(ThreadInfo threadInfo);
    public List<ThreadInfo>getThread(String url);
    public Boolean isExists(int id, String url);
}
