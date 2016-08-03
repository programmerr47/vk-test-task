package com.github.programmerr47.vkdiscussionviewer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class PhotoAttachmentView extends View {
    private static final int FADE_TRANSITION_DURATION = 300;

    private VkPhotoSet photoSet = new VkPhotoSet();
    private Drawable[] photoSetDrawables = new Drawable[0];
    private BitmapTarget[] targets = new BitmapTarget[0];

    public PhotoAttachmentView(Context context) {
        super(context);
    }

    public PhotoAttachmentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PhotoAttachmentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public PhotoAttachmentView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int w = resolveSizeAndState(photoSet.width(), widthMeasureSpec, 0);
        int h = resolveSizeAndState(photoSet.height(), heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < photoSetDrawables.length; i++) {
            photoSetDrawables[i].draw(canvas);
        }
    }

    public void setPhotoSet(VkPhotoSet photoSet) {
        for (int i = 0; i < targets.length; i++) {
            targets[i].clear();
        }

        VkPhotoSet oldPhotoSet = this.photoSet;
        this.photoSet = photoSet;

        load(photoSet);

        if (oldPhotoSet.width() != photoSet.width() || oldPhotoSet.height() != photoSet.height()) {
            requestLayout();
        }

        invalidate();
    }

    @Override
    protected boolean verifyDrawable(Drawable drawable) {
        for (int i = 0; i < photoSetDrawables.length; i++) {
            if (photoSetDrawables[i] == drawable) {
                return true;
            }
        }

        return super.verifyDrawable(drawable);
    }

    private void load(VkPhotoSet photoSet) {
        photoSetDrawables = new Drawable[photoSet.size()];
        targets = new BitmapTarget[photoSet.size()];
        for (int i = 0; i < photoSet.size(); i++) {
            Drawable placeholderDrawable = new ColorDrawable(0xffaaaaaa);
            placeholderDrawable.setBounds(photoSet.photoRect(i));
            photoSetDrawables[i] = placeholderDrawable;

            Log.v("FUCK", "Load for " + i + " image is started");
            targets[i] = new BitmapTarget(i, this);
            Picasso.with(null).load(photoSet.getPhoto(i).getUrl()).into(targets[i]);
        }
    }

    private void setDrawable(int id, Drawable drawable) {
        photoSetDrawables[id] = drawable;
        invalidate();
    }

    private Drawable getDrawable(int id) {
        return photoSetDrawables[id];
    }

    private static final class BitmapTarget implements Target {
        private final int id;
        private PhotoAttachmentView targetView;

        public BitmapTarget(int id, PhotoAttachmentView targetView) {
            this.id = id;
            this.targetView = targetView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.v("FUCK", "Id: " + id + " onBitmapLoaded");
            if (targetView != null) {
                final Drawable resultDrawable;

                if (from == Picasso.LoadedFrom.MEMORY) {
                    resultDrawable = new BitmapDrawable(targetView.getResources(), bitmap);
                } else {
                    resultDrawable = new TransitionDrawable(new Drawable[]{
                            targetView.getDrawable(id),
                            new BitmapDrawable(targetView.getResources(), bitmap)});
                }
                resultDrawable.setCallback(targetView);

                changeDrawable(resultDrawable);
            }
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.v("FUCK", "Id: " + id + " onBitmapFailed");
            if (targetView != null) {
                changeDrawable(errorDrawable);
            }
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.v("FUCK", "Id: " + id + " onPrepareLoad");
        }

        public void clear() {
            Log.v("FUCK", "Bitmap target with id " + id + " cleared");
            targetView = null;
        }

        private void changeDrawable(Drawable drawable) {
            Rect bounds = targetView.getDrawable(id).getBounds();
            drawable.setBounds(bounds);

            if (drawable instanceof TransitionDrawable) {
                ((TransitionDrawable) drawable).startTransition(FADE_TRANSITION_DURATION);
            }

            targetView.setDrawable(id, drawable);
        }
    }
}
