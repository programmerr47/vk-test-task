package com.github.programmerr47.vkdiscussionviewer.utils;

import android.support.v7.widget.RecyclerView;

import static java.lang.Math.abs;
import static java.lang.Math.min;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class AdapterItemsUpdater {
    private final RecyclerView.Adapter adapter;

    public AdapterItemsUpdater(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
    }

    public void updateItems(int startIndex, int newSize, int oldSize) {
        int diff = abs(oldSize - newSize);
        int commonPart = min(oldSize, newSize);

        if (commonPart > 0) {
            adapter.notifyItemRangeChanged(startIndex, commonPart);
        }

        if (diff > 0) {
            if (oldSize < newSize) {
                adapter.notifyItemRangeInserted(commonPart + startIndex, diff);
            } else {
                adapter.notifyItemRangeRemoved(commonPart + startIndex, diff);
            }
        }
    }
}
