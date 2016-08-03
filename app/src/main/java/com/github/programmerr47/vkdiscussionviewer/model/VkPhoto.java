package com.github.programmerr47.vkdiscussionviewer.model;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class VkPhoto {
    private int id;
    private String url;
    private int height;
    private int width;

    public VkPhoto setId(int id) {
        this.id = id;
        return this;
    }

    public VkPhoto setUrl(String url) {
        this.url = url;
        return this;
    }

    public VkPhoto setHeight(int height) {
        this.height = height;
        return this;
    }

    public VkPhoto setWidth(int width) {
        this.width = width;
        return this;
    }

    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }
}
