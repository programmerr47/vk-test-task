package com.github.programmerr47.vkdiscussionviewer.model;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class User {
    private int id;
    private String imageUrl;

    public User setId(int id) {
        this.id = id;
        return this;
    }

    public User setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
