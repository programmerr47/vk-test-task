package com.github.programmerr47.vkdiscussionviewer.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

import com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font;

import static com.github.programmerr47.vkdiscussionviewer.utils.AssetsTypefaceStorage.assetsTypefaceStorage;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class CustomTypefaceSpan extends TypefaceSpan {
    private Typeface mNewTypeface;

    public CustomTypefaceSpan(Font font) {
        this(assetsTypefaceStorage().get(font));
    }

    public CustomTypefaceSpan(Typeface newTypeface) {
        super("");
        this.mNewTypeface = newTypeface;
    }

    @Override
    public void updateDrawState(@NonNull TextPaint ds) {
        applyCustomTypeFace(ds, mNewTypeface);
    }

    @Override
    public void updateMeasureState(@NonNull TextPaint paint) {
        applyCustomTypeFace(paint, mNewTypeface);
    }

    private void applyCustomTypeFace(Paint paint, Typeface tf) {
        int oldStyle;
        Typeface old = paint.getTypeface();
        if (old == null) {
            oldStyle = 0;
        } else {
            oldStyle = old.getStyle();
        }

        int fake = oldStyle & ~tf.getStyle();
        if ((fake & Typeface.BOLD) != 0) {
            paint.setFakeBoldText(true);
        }

        if ((fake & Typeface.ITALIC) != 0) {
            paint.setTextSkewX(-0.25f);
        }

        paint.setTypeface(tf);
    }
}
