package com.github.programmerr47.vkdiscussionviewer.imageLoading;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.util.LruCache;
import android.widget.ImageView;

import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public final class AvatarLoader implements AvatarLoaderCacheHolder {
    static final ColorDrawable PLACEHOLDER_DRAWABLE = new ColorDrawable(0x00000000);
    static final int TRANSITION_DURATION = 300;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private LruCache<List<String>, Bitmap> memoryCache = new LruCache<List<String>, Bitmap>(2 * 1024 * 1024) {
        @Override
        protected int sizeOf(List<String> key, Bitmap value) {
            final int bitmapSize = getBitmapSize(value);
            return bitmapSize == 0 ? 1 : bitmapSize;
        }

        private int getBitmapSize(Bitmap bitmap) {
            // From KitKat onward use getAllocationByteCount() as allocated bytes can potentially be
            // larger than bitmap byte count.
            if (AndroidUtils.hasKitKat()) {
                return bitmap.getAllocationByteCount();
            }

            return bitmap.getByteCount();
        }
    };

    private Map<ImageView, Future> tasks = new WeakHashMap<>();

    public void load(final ChatItem item, final ImageView imageView) {
        Future activeTask = tasks.remove(imageView);
        if (activeTask != null) {
            activeTask.cancel(true);
        }

        Bitmap cached = memoryCache.get(item.getUrls());
        if (cached == null) {
            imageView.setImageDrawable(PLACEHOLDER_DRAWABLE);
            activeTask = executorService.submit(new AvatarLoadAction(item, imageView, this));
            tasks.put(imageView, activeTask);
        } else {
            imageView.setImageBitmap(cached);
        }
    }

    @Override
    public void put(List<String> key, Bitmap value) {
        memoryCache.put(key, value);
    }
}
