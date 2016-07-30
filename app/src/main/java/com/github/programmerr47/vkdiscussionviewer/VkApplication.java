package com.github.programmerr47.vkdiscussionviewer;

import android.app.Application;
import android.content.Context;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class VkApplication extends Application {
    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
    }

    public static Context appContext() {
        return appContext;
    }
}
