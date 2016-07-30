package com.github.programmerr47.vkdiscussionviewer.pager;

import android.support.v4.view.ViewPager;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;

/**
 * @author Michael Spitsin
 * @since 2016-02-27
 */
public class VkPageTransformerAuto implements ViewPager.PageTransformer {

    @Override
    public void transformPage(View page, float position) {
        final float translationX;
        final float alpha;
        View pageVeilView = page.findViewById(R.id.veil);
        pageVeilView.setAlpha(0);

        if (position < 0 && position > -1) {
            int pageWidth = page.getWidth();
            float translateValue = position * -pageWidth;
            if (translateValue > -pageWidth) {
                translationX = translateValue;
            } else {
                translationX = 0;
            }
            alpha = 1;
        } else {
            translationX = - position * page.getWidth() / 2;
            alpha = 1 - position;
        }

        page.setTranslationX(translationX);
        page.setAlpha(alpha);
    }
}
