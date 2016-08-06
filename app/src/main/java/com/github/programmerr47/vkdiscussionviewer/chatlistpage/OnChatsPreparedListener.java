package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import com.vk.sdk.api.VKError;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public interface OnChatsPreparedListener {
    void onChatsReady(List<Chat> chats);
    void onError(VKError vkError);
}
