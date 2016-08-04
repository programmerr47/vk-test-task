package com.github.programmerr47.vkdiscussionviewer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.chatpage.MessageItem;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import static android.text.Layout.Alignment.ALIGN_NORMAL;
import static android.text.TextUtils.isEmpty;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.appContext;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.color;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.sp;
import static com.github.programmerr47.vkdiscussionviewer.utils.AssetsTypefaceStorage.assetsTypefaceStorage;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_REGULAR;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class MessageView extends View {
    private static final Drawable INBOX_BG = appContext().getResources().getDrawable(R.drawable.inbox_message_bg);
    private static final Drawable OUTBOX_BG = appContext().getResources().getDrawable(R.drawable.outbox_message_bg);

    private static final int FADE_TRANSITION_DURATION = 300;

    private static TextPaint ownerMessagePaint;
    private static TextPaint otherMessagePaint;
    private static TextPaint timePaint;

    private final float avatarSize = dp(getContext(), 40);
    private final float textImageDistance = dp(getContext(), 16);

    private MessageItem message;
    private Drawable[] photoSetDrawables = new Drawable[0];
    private BitmapTarget[] targets = new BitmapTarget[0];

    private StaticLayout textLayout;
    private StaticLayout timeLayout;

    public MessageView(Context context) {
        super(context);
        init();
    }

    public MessageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public MessageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MessageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int originWidth = Math.max(message.getPhotoSet().width(), getTextLayoutWidth());
        int originHeight = message.getPhotoSet().height();
        if (textLayout != null) {
            if (originHeight != 0) {
                originHeight += textImageDistance;
            }

            originHeight += textLayout.getHeight();
        }

        int w = resolveSizeAndState(originWidth, widthMeasureSpec, 0);
        int h = resolveSizeAndState(originHeight, heightMeasureSpec, 0);
        setMeasuredDimension(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (textLayout != null) {
            textLayout.draw(canvas);
        }

        if (textLayout != null) {
            canvas.save();
            canvas.translate(0, textLayout.getHeight() + textImageDistance);
        }

        for (int i = 0; i < photoSetDrawables.length; i++) {
            photoSetDrawables[i].draw(canvas);
        }

        if (textLayout != null) {
            canvas.restore();
        }
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

    public void setMessage(MessageItem message) {
        this.message = message;

        Drawable messageBg = message.isOwner() ? OUTBOX_BG : INBOX_BG;
        TextPaint messagePaint = message.isOwner() ? ownerMessagePaint : otherMessagePaint;
        float avatarSize = message.isOwner() ? 0 : (int)this.avatarSize;

        String time = message.getDateFormatted();
        int timeWidth = (int) Math.ceil(timePaint.measureText(time, 0, time.length()));
        timeLayout = new StaticLayout(time, timePaint, timeWidth, ALIGN_NORMAL, 1.0f, 0.0f, false);

        String text = message.getContent();
        if (!isEmpty(text)) {
            final int textMaxWidth = (int)(AndroidUtils.screenSize.x - messageBg.getMinimumWidth() - dp(40) - timeWidth - avatarSize);
            int textWidth = (int) Math.ceil(messagePaint.measureText(text, 0, text.length()));
            if (textWidth > textMaxWidth) {
                textWidth = textMaxWidth;
            }

            textLayout = new StaticLayout(text, messagePaint, textWidth, ALIGN_NORMAL, 1.0f, 0.0f, false);
        } else {
            textLayout = null;
        }

        updatePhotoSet(message.getPhotoSet());
        requestLayout();
        invalidate();
    }

    private void updatePhotoSet(VkPhotoSet photoSet) {
        for (int i = 0; i < targets.length; i++) {
            targets[i].clear();
        }

        load(photoSet);
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

    private void init() {
        if (ownerMessagePaint == null) {
            ownerMessagePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            ownerMessagePaint.setColor(color(R.color.text_primary_dark));
            fillMessagePaint(ownerMessagePaint);

            otherMessagePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            otherMessagePaint.setColor(color(R.color.text_primary));
            fillMessagePaint(otherMessagePaint);

            timePaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
            timePaint.setColor(color(R.color.text_secondary_dark));
            timePaint.setTextSize(sp(12));
            timePaint.setTypeface(assetsTypefaceStorage().get(ROBOTO_REGULAR));
        }
    }

    private void fillMessagePaint(Paint paint) {
        paint.setTextSize(sp(16));
        paint.setTypeface(assetsTypefaceStorage().get(ROBOTO_REGULAR));
    }

    private int getTextLayoutWidth() {
        if (textLayout != null) {
            int width = 0;
            for (int i = 0; i < textLayout.getLineCount(); i++) {
                width = Math.max(width, (int)textLayout.getLineWidth(i));
            }

            if (width == 0) {
                width = textLayout.getWidth();
            }

            return width;
        } else {
            return 0;
        }
    }

    private static final class BitmapTarget implements Target {
        private final int id;
        private MessageView targetView;

        public BitmapTarget(int id, MessageView targetView) {
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
