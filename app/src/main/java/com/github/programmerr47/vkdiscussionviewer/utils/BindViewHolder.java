package com.github.programmerr47.vkdiscussionviewer.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public abstract class BindViewHolder extends RecyclerView.ViewHolder {
    public BindViewHolder(View itemView) {
        super(itemView);
    }

    @SuppressWarnings("unchecked")
    protected <T> T bind(int id) {
        return (T) itemView.findViewById(id);
    }
}
