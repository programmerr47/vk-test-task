package com.github.programmerr47.vkdiscussionviewer.chatpage;

import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class Message {
    private int id;
    private String avatarUrl;
    private long date;
    private String content;
    private VkPhotoSet photoSet;

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

    Message setPhotoSet(VkPhotoSet photoSet) {
        this.photoSet = photoSet;
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

    public VkPhotoSet getPhotoSet() {
        return photoSet;
    }
}
