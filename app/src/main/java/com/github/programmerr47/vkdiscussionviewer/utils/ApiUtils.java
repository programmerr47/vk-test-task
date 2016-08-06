package com.github.programmerr47.vkdiscussionviewer.utils;

import android.text.TextUtils;

import com.github.programmerr47.vkdiscussionviewer.model.User;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhotoSize;
import com.vk.sdk.api.model.VKAttachments;
import com.vk.sdk.api.model.VKAttachments.VKApiAttachment;
import com.vk.sdk.api.model.VKPhotoSizes;

import java.util.ArrayList;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.globalStorage;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;
import static com.vk.sdk.api.model.VKAttachments.TYPE_ALBUM;
import static com.vk.sdk.api.model.VKAttachments.TYPE_APP;
import static com.vk.sdk.api.model.VKAttachments.TYPE_AUDIO;
import static com.vk.sdk.api.model.VKAttachments.TYPE_DOC;
import static com.vk.sdk.api.model.VKAttachments.TYPE_LINK;
import static com.vk.sdk.api.model.VKAttachments.TYPE_NOTE;
import static com.vk.sdk.api.model.VKAttachments.TYPE_PHOTO;
import static com.vk.sdk.api.model.VKAttachments.TYPE_POLL;
import static com.vk.sdk.api.model.VKAttachments.TYPE_POST;
import static com.vk.sdk.api.model.VKAttachments.TYPE_POSTED_PHOTO;
import static com.vk.sdk.api.model.VKAttachments.TYPE_VIDEO;
import static com.vk.sdk.api.model.VKAttachments.TYPE_WIKI_PAGE;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class ApiUtils {
    private static final int AVATAR_DEFAULT_SIZE = (int) dp(56);

    public static String getAppropriateAvatarUrl(VKPhotoSizes photoSizes) {
        return getAppropriatePhotoUrl(photoSizes, AVATAR_DEFAULT_SIZE, AVATAR_DEFAULT_SIZE);
    }

    public static String getAppropriatePhotoUrl(VKPhotoSizes photoSizes, int targetWidth, int targetHeight) {
        for (VKApiPhotoSize photoSize : photoSizes) {
            if (photoSize.width >= targetWidth && photoSize.height >= targetHeight) {
                return photoSize.src;
            }
        }

        if (!photoSizes.isEmpty()) {
            return photoSizes.get(photoSizes.size() - 1).src;
        }

        return null;
    }

    public static String getMessageContent(VKApiMessage message) {
        if (!isEmpty(message.body)) {
            return message.body;
        }

        if (!isEmpty(message.action)) {
            return message.action;
        }

        boolean hasPhoto = false;
        int albumCount = 0;
        int appCount = 0;
        int soundCount = 0;
        int fileCount = 0;
        int linkCount = 0;
        int readCount = 0;
        int videoCount = 0;

        for (VKApiAttachment attachment : message.attachments) {
            switch (attachment.getType()) {
                case TYPE_ALBUM:
                case TYPE_POSTED_PHOTO:
                    albumCount++;
                    break;
                case TYPE_APP:
                    appCount++;
                    break;
                case TYPE_AUDIO:
                    soundCount++;
                    break;
                case TYPE_DOC:
                    fileCount++;
                    break;
                case TYPE_LINK:
                    linkCount++;
                    break;
                case TYPE_NOTE:
                case TYPE_POLL:
                case TYPE_POST:
                case TYPE_WIKI_PAGE:
                    readCount++;
                    break;
                case TYPE_PHOTO:
                    hasPhoto = true;
                    break;
                case TYPE_VIDEO:
                    videoCount++;
                    break;
            }
        }

        StringBuilder builder  = new StringBuilder();
        appendPositive(builder, "\uD83C\uDFA4", soundCount);
        appendPositive(builder, "\uD83C\uDFAC", videoCount);
        appendPositive(builder, "\uD83D\uDCC4", readCount);
        appendPositive(builder, "\uD83D\uDCBE", fileCount);
        appendPositive(builder, "\uD83D\uDCCE", linkCount);
        appendPositive(builder, "\uD83D\uDF7E", appCount);
        appendPositive(builder, "\uD83D\uDFF7", albumCount);

        if (builder.length() == 0 && !hasPhoto) {
            return "<Unknown Message>";
        } else {
            return builder.toString();
        }
    }

    private static void appendPositive(StringBuilder builder, String symbol, int count) {
        if (count > 0) {
            builder.append(symbol).append(": ").append(count).append(", ");
        }
    }
}
