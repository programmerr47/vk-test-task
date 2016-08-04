package com.github.programmerr47.vkdiscussionviewer.model;

import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class VkPhotoSet {
    private List<VkPhoto> photos = new ArrayList<>();
    private List<Rect> positions = new ArrayList<>();

    private int width;
    private int height;

    public void placePhoto(VkPhoto photo, Rect position) {
        photos.add(photo);
        positions.add(position);

        if (position.left >= width) {
            width = position.left + photo.getWidth();
        }

        if (position.top >= height) {
            height = position.top + photo.getHeight();
        }
    }

    public VkPhoto getPhoto(int index) {
        return photos.get(index);
    }

    public Rect photoRect(int index) {
        return positions.get(index);
    }

    public int size() {
        return photos.size();
    }

    public boolean isEmpty() {
        return photos.size() == 0;
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }
}
