package com.github.programmerr47.vkdiscussionviewer.utils;

import com.vk.sdk.api.model.VKApiPhotoSize;
import com.vk.sdk.api.model.VKPhotoSizes;

import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.AVATAR_DEFAULT_SIZE;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class ApiUtils {

    public static String getAppropriatePhotoUrl(VKPhotoSizes photoSizes) {
        for (VKApiPhotoSize photoSize : photoSizes) {
            if (Math.min(photoSize.width, photoSize.height) >= AVATAR_DEFAULT_SIZE) {
                return photoSize.src;
            }
        }

        if (!photoSizes.isEmpty()) {
            return photoSizes.get(photoSizes.size() - 1).src;
        }

        return null;
    }
}
