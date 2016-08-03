package com.github.programmerr47.vkdiscussionviewer;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.view.Display;
import android.view.WindowManager;

import com.github.programmerr47.vkdiscussionviewer.imageLoading.AvatarLoader;
import com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils;
import com.squareup.picasso.Picasso;
import com.vk.sdk.VKSdk;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class VkApplication extends Application {
    private static Context appContext;
    private static Handler uiHandler;
    private static GlobalStorage globalStorage;
    private static AvatarLoader avatarLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();
        uiHandler = new Handler();
        globalStorage = new GlobalStorage();
        avatarLoader = new AvatarLoader();
        VKSdk.initialize(appContext);
        Picasso.setSingletonInstance(new Picasso.Builder(appContext).build());
        checkScreenSize();
    }

    public static Context appContext() {
        return appContext;
    }

    public static Handler uiHandler() {
        return uiHandler;
    }

    public static GlobalStorage globalStorage() {
        return globalStorage;
    }

    public static AvatarLoader avatarLoader() {
        return avatarLoader;
    }

    private void checkScreenSize() {
        WindowManager manager = (WindowManager) appContext.getSystemService(Context.WINDOW_SERVICE);
        if (manager != null) {
            Display display = manager.getDefaultDisplay();
            display.getSize(AndroidUtils.screenSize);
        }
    }
}
