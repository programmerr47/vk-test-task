package com.github.programmerr47.vkdiscussionviewer.views;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font;
import com.github.programmerr47.vkdiscussionviewer.R;

import static com.github.programmerr47.vkdiscussionviewer.utils.AssetsTypefaceStorage.assetsTypefaceStorage;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public class CustomFontTextView extends TextView {

    private Font font;

    public CustomFontTextView(Context context) {
        super(context);
        init(null);
    }

    public CustomFontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomFontTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

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
            setTypeface(assetsTypefaceStorage().get(font));
        }
    }
}
