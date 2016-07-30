package com.github.programmerr47.vkdiscussionviewer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.Constants.Font;
import com.github.programmerr47.vkdiscussionviewer.R;

import static com.github.programmerr47.vkdiscussionviewer.AndroidUtils.assetsTypeface;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class CustomFontTextView extends TextView {

    private Font font;

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.CustomFontTextView);
            int fontId = typedArray.getInt(R.styleable.CustomFontTextView_fontName, 0);
            Font font = Font.fromId(fontId);
            setFont(font);
            typedArray.recycle();
        }
    }

    @SuppressWarnings("unused")
    public void setFont(Font font) {
        if (this.font != font && font != null && font.getFontName() != null) {
            this.font = font;
            setTypeface(assetsTypeface(getContext(), font));
        }
    }
}
