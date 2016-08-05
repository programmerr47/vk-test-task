package com.github.programmerr47.vkdiscussionviewer.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.View;

import static android.util.TypedValue.COMPLEX_UNIT_DIP;
import static android.util.TypedValue.COMPLEX_UNIT_SP;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.appContext;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class AndroidUtils {

    public static final Point screenSize = new Point();

    private AndroidUtils() {}

    public static boolean hasKitKat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static Typeface assetsTypeface(Constants.Font font) {
        return assetsTypeface(appContext(), font);
    }

    public static Typeface assetsTypeface(Context context, Constants.Font font) {
        return Typeface.createFromAsset(context.getAssets(), Constants.ASSETS_FONTS_DIR + font.getFontName());
    }

    public static String plural(int id, int quantity, Object... formatArgs) {
        return plural(appContext(), id, quantity, formatArgs);
    }

    public static String plural(@NonNull Context context, int id, int quantity, Object... formatArgs) {
        return context.getResources().getQuantityString(id, quantity, formatArgs);
    }

    public static String string(int id, Object... formatArgs) {
        return string(appContext(), id, formatArgs);
    }

    public static String string(@NonNull Context context, int id, Object... formatArgs) {
        return context.getResources().getString(id, formatArgs);
    }

    public static int integer(int id) {
        return integer(appContext(), id);
    }

    public static int integer(@NonNull Context context, int id) {
        return context.getResources().getInteger(id);
    }

    public static float dimen(int dimenId) {
        return dimen(appContext(), dimenId);
    }

    public static float dimen(@NonNull Context context, int dimenId) {
        return context.getResources().getDimension(dimenId);
    }

    @ColorInt
    public static int color(@ColorRes int colorId) {
        return color(appContext(), colorId);
    }

    @ColorInt
    public static int color(@NonNull Context context, @ColorRes int colorId) {
        return context.getResources().getColor(colorId);
    }

    public static float dp(float dp) {
        return dp(appContext(), dp);
    }

    public static float dp(@NonNull Context context, float dp) {
        Resources res = context.getResources();
        return TypedValue.applyDimension(COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static float sp(float sp) {
        return sp(appContext(), sp);
    }

    public static float sp(@NonNull Context context, float sp) {
        Resources res = context.getResources();
        return TypedValue.applyDimension(COMPLEX_UNIT_SP, sp, res.getDisplayMetrics());
    }

    @SuppressWarnings("unchecked")
    public static <T> T bind(@NonNull View parent, int viewId) {
        return (T) parent.findViewById(viewId);
    }
}
