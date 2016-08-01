package com.github.programmerr47.vkdiscussionviewer.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;

import java.util.List;

import static android.graphics.Bitmap.Config.ARGB_8888;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class BitmapUtils {

    private static final int avatarSize = (int) dp(56);
    private static final int avatarHalf = (int)(dp(56) / 2);
    private static final int margin = (int) dp(1);

    public static Bitmap transformsAvatars(List<Bitmap> avatars) {
        if (!avatars.isEmpty()) {
            switch (avatars.size()) {
                case 1:
                    return transform(avatars.get(0));
                case 2:
                    return transform(
                            cropToHalf(avatars.get(0)), cropToHalf(avatars.get(1)));
                case 3:
                    return transform(
                            cropToHalf(avatars.get(0)),
                            halfDown(avatars.get(1)), halfDown(avatars.get(2)));
                default:
                    return transform(
                            halfDown(avatars.get(0)), halfDown(avatars.get(1)),
                            halfDown(avatars.get(2)), halfDown(avatars.get(3)));
            }
        } else {
            return null;
        }
    }

    private static Bitmap transform(Bitmap first) {
        Rect firstSrcRect = new Rect(0, 0, first.getWidth(), first.getHeight());
        Rect firstDstRect = new Rect(0, 0, avatarSize, avatarSize);

        Bitmap result = Bitmap.createBitmap(avatarSize, avatarSize, ARGB_8888);

        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(0x00000000);
        canvas.drawCircle(avatarHalf, avatarHalf, avatarHalf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, paint);
        return result;
    }

    private static Bitmap transform(Bitmap first, Bitmap second) {
        Rect firstSrcRect = new Rect(0, 0, first.getWidth(), first.getHeight());
        Rect firstDstRect = new Rect(0, 0, avatarHalf - margin, avatarSize);

        Rect secondSrcRect = new Rect(0, 0, second.getWidth(), second.getHeight());
        Rect secondDstRect = new Rect(avatarHalf + margin, 0, avatarSize, avatarSize);

        Bitmap result = Bitmap.createBitmap(avatarSize, avatarSize, ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(avatarHalf, avatarHalf, avatarHalf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, paint);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, paint);
        return result;
    }

    private static Bitmap transform(Bitmap first, Bitmap second, Bitmap third) {
        Rect firstSrcRect = new Rect(0, 0, first.getWidth(), first.getHeight());
        Rect firstDstRect = new Rect(0, 0, avatarHalf - margin, avatarSize);

        Rect secondSrcRect = new Rect(0, 0, second.getWidth(), second.getHeight());
        Rect secondDstRect = new Rect(avatarHalf + margin, 0, avatarSize, avatarHalf - margin);

        Rect thirdSrcRect = new Rect(0, 0, third.getWidth(), third.getHeight());
        Rect thirdDstRect = new Rect(avatarHalf + margin, avatarHalf + margin, avatarSize, avatarSize);

        Bitmap result = Bitmap.createBitmap(avatarSize, avatarSize, ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(avatarHalf, avatarHalf, avatarHalf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, paint);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, paint);
        canvas.drawBitmap(third, thirdSrcRect, thirdDstRect, paint);
        return result;
    }

    private static Bitmap transform(Bitmap first, Bitmap second, Bitmap third, Bitmap fourth) {
        Rect firstSrcRect = new Rect(0, 0, first.getWidth(), first.getHeight());
        Rect firstDstRect = new Rect(0, 0, avatarHalf - margin, avatarHalf - margin);

        Rect secondSrcRect = new Rect(0, 0, second.getWidth(), second.getHeight());
        Rect secondDstRect = new Rect(avatarHalf + margin, 0, avatarSize, avatarHalf - margin);

        Rect thirdSrcRect = new Rect(0, 0, third.getWidth(), third.getHeight());
        Rect thirdDstRect = new Rect(0, avatarHalf + margin, avatarHalf - margin, avatarSize);

        Rect fourthSrcRect = new Rect(0, 0, fourth.getWidth(), fourth.getHeight());
        Rect fourthDstRect = new Rect(avatarHalf + margin, avatarHalf + margin, avatarSize, avatarSize);

        Bitmap result = Bitmap.createBitmap(avatarSize, avatarSize, ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(avatarHalf, avatarHalf, avatarHalf, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, paint);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, paint);
        canvas.drawBitmap(third, thirdSrcRect, thirdDstRect, paint);
        canvas.drawBitmap(fourth, fourthSrcRect, fourthDstRect, paint);
        return result;
    }

    private static Bitmap cropToHalf(Bitmap bitmap) {
        int quadWidth = bitmap.getWidth() / 4;
        int halfWidth = bitmap.getWidth() / 2;
        return Bitmap.createBitmap(bitmap, quadWidth, 0, halfWidth,bitmap.getHeight());
    }

    private static Bitmap halfDown(Bitmap origin) {
        return Bitmap.createScaledBitmap(origin, origin.getWidth() / 2, origin.getHeight() / 2, false);
    }
}
