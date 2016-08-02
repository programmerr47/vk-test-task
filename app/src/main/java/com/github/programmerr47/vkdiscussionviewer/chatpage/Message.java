package com.github.programmerr47.vkdiscussionviewer.chatpage;

import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class Message {
    private int id;
    private String avatarUrl;
    private long date;
    private String content;
    private List<String> imageUrls;

    Message setId(int id) {
        this.id = id;
        return this;
    }

    Message setAvatarUrl(String url) {
        this.avatarUrl = url;
        return this;
    }

    Message setDate(long date) {
        this.date = date;
        return this;
    }

    Message setContent(String content) {
        this.content = content;
        return this;
    }

    Message setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public long getDate() {
        return date;
    }

    public String getContent() {
        return content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }
}
