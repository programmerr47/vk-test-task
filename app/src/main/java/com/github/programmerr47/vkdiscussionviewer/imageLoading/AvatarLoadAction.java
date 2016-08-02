package com.github.programmerr47.vkdiscussionviewer.imageLoading;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.widget.ImageView;

import com.github.programmerr47.vkdiscussionviewer.VkApplication;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.utils.BitmapUtils;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.imageLoading.AvatarLoader.PLACEHOLDER_DRAWABLE;
import static com.github.programmerr47.vkdiscussionviewer.imageLoading.AvatarLoader.TRANSITION_DURATION;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class AvatarLoadAction implements Runnable {
    private final WeakReference<ImageView> weakImageView;
    private final ChatItem data;
    private final AvatarLoaderCacheHolder cacheHolder;

    public AvatarLoadAction(ChatItem data, ImageView imageView, AvatarLoaderCacheHolder cacheHolder) {
        weakImageView = new WeakReference<>(imageView);
        this.data = data;
        this.cacheHolder = cacheHolder;
    }

    @Override
    public void run() {
        List<String> avatarUrls = data.getUrls();

        List<Bitmap> bitmaps = new ArrayList<>();
        for (String url : avatarUrls) {
            try {
                bitmaps.add(Picasso.with(null).load(url).get());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        final Bitmap result = BitmapUtils.transformsAvatars(bitmaps);
        final TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[] {
                PLACEHOLDER_DRAWABLE, new BitmapDrawable(result)
        });

        VkApplication.uiHandler().post(new Runnable() {
            @Override
            public void run() {
                cacheHolder.put(data.getUrls(), result);
                ImageView imageView = weakImageView.get();
                if (imageView != null) {
                    imageView.setImageDrawable(transitionDrawable);
                    transitionDrawable.startTransition(TRANSITION_DURATION);
                }
            }
        });
    }
}
