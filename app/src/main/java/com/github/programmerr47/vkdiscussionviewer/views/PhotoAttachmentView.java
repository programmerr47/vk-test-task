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

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class PhotoAttachmentView extends View {
    private static final int FADE_TRANSITION_DURATION = 300;

    private VkPhotoSet photoSet = new VkPhotoSet();
    private Drawable[] photoSetDrawables = new Drawable[0];

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
        VkPhotoSet oldPhotoSet = this.photoSet;
        this.photoSet = photoSet;
        photoSetDrawables = load(photoSet);

        if (oldPhotoSet.width() != photoSet.width() || oldPhotoSet.height() != photoSet.height()) {
            requestLayout();
        }

        invalidate();
    }

    private Drawable[] load(VkPhotoSet photoSet) {
        Drawable[] placeholders = new Drawable[photoSet.size()];
        for (int i = 0; i < photoSet.size(); i++) {
            Drawable placeholderDrawable = new ColorDrawable(0xffaaaaaa);
            placeholderDrawable.setBounds(photoSet.photoRect(i));

            Log.v("FUCK", "Load for " + i + " image is started");
            Picasso.with(null).load(photoSet.getPhoto(i).getUrl()).into(new BitmapTarget(i, this));

            placeholders[i] = placeholderDrawable;
        }

        return placeholders;
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
        private final PhotoAttachmentView targetView;

        public BitmapTarget(int id, PhotoAttachmentView targetView) {
            this.id = id;
            this.targetView = targetView;
        }

        @Override
        public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
            Log.v("FUCK", "Id: " + id + " onBitmapLoaded");
            final Drawable resultDrawable;

            resultDrawable = new BitmapDrawable(targetView.getResources(), bitmap);
//            if (from == Picasso.LoadedFrom.MEMORY) {
//            } else {
//                resultDrawable = new TransitionDrawable(new Drawable[]{
//                        targetView.getDrawable(id),
//                        new BitmapDrawable(targetView.getResources(), bitmap)});
//            }

            changeDrawable(resultDrawable);
        }

        @Override
        public void onBitmapFailed(Drawable errorDrawable) {
            Log.v("FUCK", "Id: " + id + " onBitmapFailed");
            changeDrawable(errorDrawable);
        }

        @Override
        public void onPrepareLoad(Drawable placeHolderDrawable) {
            Log.v("FUCK", "Id: " + id + " onPrepareLoad");
        }

        private void changeDrawable(Drawable drawable) {
            Rect bounds = targetView.getDrawable(id).getBounds();
            drawable.setBounds(bounds);
            targetView.setDrawable(id, drawable);

            if (drawable instanceof TransitionDrawable) {
                ((TransitionDrawable) drawable).startTransition(FADE_TRANSITION_DURATION);
            }
        }
    }
}
