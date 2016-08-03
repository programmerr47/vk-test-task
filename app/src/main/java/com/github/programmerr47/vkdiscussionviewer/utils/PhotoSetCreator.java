package com.github.programmerr47.vkdiscussionviewer.utils;

import android.graphics.Point;

import com.github.programmerr47.vkdiscussionviewer.model.VkPhoto;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKAttachments.VKApiAttachment;

import java.util.ArrayList;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;
import static com.vk.sdk.api.model.VKAttachments.TYPE_PHOTO;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class PhotoSetCreator {
    private static final int PHOTOSET_W_MAX = (int) dp(198);
    private static final int PHOTOSET_H_MAX = (int) dp(298);

    private static final int margin = (int) dp(1);

    public static VkPhotoSet createPhotoSet(VKAttachments messageAttachments) {
        List<VKApiPhoto> photos = new ArrayList<>();
        for (VKApiAttachment attachment : messageAttachments) {
            if (TYPE_PHOTO.equals(attachment.getType())) {
                photos.add((VKApiPhoto) attachment);
            }
        }

        VkPhotoSet photoSet = new VkPhotoSet();

        if (photos.size() >= 1) {
            VKApiPhoto apiPhoto = photos.get(0);
            VkPhoto photo = new VkPhoto()
                    .setId(apiPhoto.getId());

            if (apiPhoto.width > PHOTOSET_W_MAX || apiPhoto.height > PHOTOSET_H_MAX) {
                float newHeight;
                float newWidth;

                if (apiPhoto.width > PHOTOSET_W_MAX) {
                    newHeight = 1f * apiPhoto.height * PHOTOSET_W_MAX / apiPhoto.width;
                    newWidth = PHOTOSET_W_MAX;

                    if (newHeight > PHOTOSET_H_MAX) {
                        newWidth = 1f * newWidth * PHOTOSET_H_MAX / newHeight;
                        newHeight = PHOTOSET_H_MAX;
                    }
                } else {
                    newWidth = 1f * apiPhoto.width * PHOTOSET_H_MAX / apiPhoto.height;
                    newHeight = PHOTOSET_H_MAX;
                }

                photo.setWidth((int) newWidth).setHeight((int) newHeight);
            } else {
                photo.setWidth(apiPhoto.width).setHeight(apiPhoto.height);
            }

            photo.setUrl(ApiUtils.getAppropriatePhotoUrl(apiPhoto.src, photo.getWidth(), photo.getHeight()));

            photoSet.placePhoto(photo, new Point(margin, margin));
        }

        return photoSet;
    }
}
