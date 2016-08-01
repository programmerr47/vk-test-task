package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class ChatItem {
    private int chatId;
    private String title;
    private String lastMessage;
    private long date;
    private List<String> urls;
    private int participantsCount;

    ChatItem setChatId(int chatId) {
        this.chatId = chatId;
        return this;
    }

    ChatItem setTitle(String title) {
        this.title = title;
        return this;
    }

    ChatItem setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    ChatItem setDate(long date) {
        this.date = date;
        return this;
    }

    ChatItem setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    ChatItem setParticipantsCount(int count) {
        this.participantsCount = count;
        return this;
    }

    public int getChatId() {
        return chatId;
    }

    public String getTitle() {
        return title;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public long getDate() {
        return date;
    }

    public List<String> getUrls() {
        return urls;
    }

    public int getParticipantsCount() {
        return participantsCount;
    }
}
