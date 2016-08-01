package com.github.programmerr47.vkdiscussionviewer;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class VkApplication extends Application {
    private static Context appContext;
    private static Handler uiHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        uiHandler = new Handler();
        VKSdk.initialize(appContext);
        Picasso.setSingletonInstance(new Picasso.Builder(appContext).build());
    }

    public static Context appContext() {
        return appContext;
    }

    public static Handler uiHandler() {
        return uiHandler;
    }
}
