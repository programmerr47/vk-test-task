package com.github.programmerr47.vkdiscussionviewer.imageLoading;

import android.graphics.Bitmap;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
interface AvatarLoaderCacheHolder {
    void put(List<String> key, Bitmap value);
}
