package com.github.programmerr47.vkdiscussionviewer;

import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.chatpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.model.User;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
//TODO use lru cache or something 'limited' cache to avoid items overflow
//TODO add more smart mechanism of caching messages, not just clear all, if first count don't match
public class GlobalStorage {
    private final SparseArray<User> userMap = new SparseArray<>();
    private final SparseArray<List<ChatItem>> chatMap = new SparseArray<>();

    public void cacheUser(User user) {
        userMap.put(user.getId(), user);
    }

    public User getUser(int id) {
        return userMap.get(id);
    }

    public boolean hasUser(int id) {
        return userMap.indexOfKey(id) >= 0;
    }

    public void cacheMessageNewSmallPart(int chatId, List<ChatItem> chatPart) {
        lazyGetChat(chatId).addAll(0, chatPart);
    }

    public void rewriteChat(int chatId, List<ChatItem> freshPart) {
        List<ChatItem> chatCachedHistory = lazyGetChat(chatId);
        chatCachedHistory.clear();
        chatCachedHistory.addAll(freshPart);
    }

    public void cachePart(int chatId, List<ChatItem> newPart) {
        lazyGetChat(chatId).addAll(newPart);
    }

    public List<ChatItem> getChatHistory(int chatId) {
        return lazyGetChat(chatId);
    }

    private List<ChatItem> lazyGetChat(int chatId) {
        List<ChatItem> history = chatMap.get(chatId);
        if (history == null) {
            history = new ArrayList<>();
            chatMap.put(chatId, history);
        }

        return history;
    }
}
