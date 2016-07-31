package com.github.programmerr47.vkdiscussionviewer.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class VkViewPager extends ViewPager {

    private static Drawable layerShadowDrawable;

    public VkViewPager(Context context) {
        super(context);
        init();
    }

    public VkViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        boolean result = super.drawChild(canvas, child, drawingTime);
        layerShadowDrawable.setBounds((int) child.getX() - layerShadowDrawable.getIntrinsicWidth(), child.getTop(), (int) child.getX(), child.getBottom());
        layerShadowDrawable.setAlpha((int)(0xFF * child.getAlpha()));
        layerShadowDrawable.draw(canvas);
        return result;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }

    private void init() {
        if (layerShadowDrawable == null) {
            layerShadowDrawable = getResources().getDrawable(R.drawable.layer_shadow);
        }
    }
}
