package com.github.programmerr47.vkdiscussionviewer.chatpage;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public interface OnChatsPreparedListener {
    void onChatsReady(List<ChatItem> chats);
}
