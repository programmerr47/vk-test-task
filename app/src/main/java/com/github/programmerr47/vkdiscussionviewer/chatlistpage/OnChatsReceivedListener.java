package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.util.SparseArray;

import com.vk.sdk.api.VKError;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public interface OnChatsReceivedListener {
    void onChatsReceived(List<Chat> chats, SparseArray<Chat> chatMap);
    void onError(VKError error);
}
