package com.weibo.cjfire.downloadpractise;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.weibo.cjfire.downloadpractise.db.ThreadDAOImp;
import com.weibo.cjfire.downloadpractise.entities.FileInfo;
import com.weibo.cjfire.downloadpractise.entities.ThreadInfo;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getContext;
import static android.support.test.InstrumentationRegistry.getTargetContext;
import static org.junit.Assert.assertEquals;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = getTargetContext();

        assertEquals("com.weibo.cjfire.downloadpractise", appContext.getPackageName());

        ThreadDAOImp daoImp = new ThreadDAOImp(getTargetContext());

        // Insert
        ThreadInfo threadInfo = new ThreadInfo(1, "www.baidu.com", 0, 100, 0);
        assertEquals(true, daoImp.insertThread(threadInfo));

        // Update
        threadInfo.setFinished(1);
        assertEquals(true, daoImp.updateThreat(threadInfo));

        // Select
        assertEquals(true, daoImp.isExists(1, "www.baidu.com"));

        // Delete
        assertEquals(true, daoImp.deleteThread(1, "www.baidu.com"));
    }
}
