package com.github.programmerr47.vkdiscussionviewer.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
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
        Bitmap avatarSymbiont = mergeAvatars(avatars);
        return CircleTransform.INSTANCE.transform(avatarSymbiont);
    }

    private static Bitmap mergeAvatars(List<Bitmap> avatars) {
        if (avatars.isEmpty()) {
            throw new IllegalArgumentException("Avatar list must be not empty");
        }

        switch (avatars.size()) {
            case 1:
                return avatars.get(0);
            case 2:
                return transform(
                        cropToHalf(avatars.get(0)), cropToHalf(avatars.get(1)));
            case 3:
                return transform(
                        cropToHalf(avatars.get(0)),
                        avatars.get(1), avatars.get(2));
            default:
                return transform(
                        avatars.get(0), avatars.get(1),
                        avatars.get(2), avatars.get(3));
        }
    }

    private static Bitmap transform(Bitmap first, Bitmap second) {
        Rect firstSrcRect = new Rect(0, 0, first.getWidth(), first.getHeight());
        Rect firstDstRect = new Rect(0, 0, avatarHalf - margin, avatarSize);

        Rect secondSrcRect = new Rect(0, 0, second.getWidth(), second.getHeight());
        Rect secondDstRect = new Rect(avatarHalf + margin, 0, avatarSize, avatarSize);

        Bitmap result = Bitmap.createBitmap(avatarSize, avatarSize, ARGB_8888);
        Canvas canvas = new Canvas(result);
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, null);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, null);
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
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, null);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, null);
        canvas.drawBitmap(third, thirdSrcRect, thirdDstRect, null);
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
        canvas.drawBitmap(first, firstSrcRect, firstDstRect, null);
        canvas.drawBitmap(second, secondSrcRect, secondDstRect, null);
        canvas.drawBitmap(third, thirdSrcRect, thirdDstRect, null);
        canvas.drawBitmap(fourth, fourthSrcRect, fourthDstRect, null);
        return result;
    }

    private static Bitmap cropToHalf(Bitmap bitmap) {
        int quadWidth = bitmap.getWidth() / 4;
        int halfWidth = bitmap.getWidth() / 2;
        return Bitmap.createBitmap(bitmap, quadWidth, 0, halfWidth,bitmap.getHeight());
    }
}
