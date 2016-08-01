package com.github.programmerr47.vkdiscussionviewer.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class AnimationUtils {

    public static void hideView(final View view) {
        view.setVisibility(VISIBLE);
        view.animate()
                .alpha(0)
                .setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.animate().setListener(null);
                        view.setVisibility(GONE);
                    }
                })
                .start();
    }

    public static void showView(final View view) {
        view.setVisibility(VISIBLE);
        view.animate()
                .alpha(1)
                .setInterpolator(new DecelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        view.animate().setListener(null);
                    }
                })
                .start();
    }
}
