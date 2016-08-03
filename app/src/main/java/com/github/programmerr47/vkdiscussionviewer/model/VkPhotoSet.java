package com.github.programmerr47.vkdiscussionviewer.model;

import android.graphics.Point;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-03
 */
public class VkPhotoSet {
    private List<VkPhoto> photos = new ArrayList<>();
    private List<Point> positions = new ArrayList<>();

    private int width;
    private int height;

    public void placePhoto(VkPhoto photo, Point position) {
        photos.add(photo);
        positions.add(position);

        if (position.x >= width) {
            width = position.x + photo.getWidth();
        }

        if (position.y >= height) {
            height = position.y + photo.getHeight();
        }
    }

    public VkPhoto getPhoto(int index) {
        return photos.get(index);
    }

    public Point getPosition(int index) {
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

    public Rect photoRect(int index) {
        return new Rect(
                positions.get(index).x,
                positions.get(index).y,
                positions.get(index).x + photos.get(index).getWidth(),
                positions.get(index).y + photos.get(index).getHeight()
        );
    }
}
