package com.github.programmerr47.vkdiscussionviewer;

import android.graphics.Typeface;
import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.Constants.Font;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public enum AssetsTypefaceStorage {
    INSTANCE;

    public static AssetsTypefaceStorage assetsTypefaceStorage() {
        return INSTANCE;
    }

    private final SparseArray<Typeface> typefaces = new SparseArray<>();

    public Typeface get(Font font) {
        Typeface cached = typefaces.get(font.getId());

        if (cached == null) {
            cached = AndroidUtils.assetsTypeface(font);
            typefaces.put(font.getId(), cached);
        }

        return cached;
    }
}
