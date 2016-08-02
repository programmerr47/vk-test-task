package com.github.programmerr47.vkdiscussionviewer;

import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.model.User;

import java.util.Collection;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class GlobalStorage {
    private final SparseArray<User> userMap = new SparseArray<>();

    public void cacheUser(User user) {
        userMap.put(user.getId(), user);
    }

    public User getUser(int id) {
        return userMap.get(id);
    }
}
