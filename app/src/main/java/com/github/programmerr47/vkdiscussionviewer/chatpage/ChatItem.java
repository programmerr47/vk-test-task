package com.github.programmerr47.vkdiscussionviewer.chatpage;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class ChatItem {
    private int chatId;
    private String[] urls;
    private String title;
    private String lastMessage;
    private long date;

    ChatItem setChatId(int chatId) {
        this.chatId = chatId;
        return this;
    }

    ChatItem setUrls(String[] urls) {
        this.urls = urls;
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

    public int getChatId() {
        return chatId;
    }

    public String[] getUrls() {
        return urls;
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
}
