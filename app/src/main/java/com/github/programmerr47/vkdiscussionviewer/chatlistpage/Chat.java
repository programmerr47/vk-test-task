package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class Chat {
    private int chatId;
    private String title;
    private String lastMessage;
    private long date;
    private List<String> urls;
    private int participantsCount;

    Chat setChatId(int chatId) {
        this.chatId = chatId;
        return this;
    }

    Chat setTitle(String title) {
        this.title = title;
        return this;
    }

    Chat setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
        return this;
    }

    Chat setDate(long date) {
        this.date = date;
        return this;
    }

    Chat setUrls(List<String> urls) {
        this.urls = urls;
        return this;
    }

    Chat setParticipantsCount(int count) {
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
