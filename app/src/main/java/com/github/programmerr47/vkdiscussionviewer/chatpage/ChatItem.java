package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

/**
 * @author Michael Spitsin
 * @since 2016-08-04
 */
public interface ChatItem<T extends RecyclerView.ViewHolder> {
    RecyclerView.ViewHolder onCreateHolder(LayoutInflater inflater);
    void onBindHolder(T holder, int position);
    int getType();
}
