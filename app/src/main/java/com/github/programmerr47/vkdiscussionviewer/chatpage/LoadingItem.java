package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;

/**
 * @author Michael Spitsin
 * @since 2016-08-04
 */
public class LoadingItem implements ChatItem<LoadingItem.LoadingItemHolder> {
    @Override
    public RecyclerView.ViewHolder onCreateHolder(LayoutInflater inflater) {
        return new LoadingItemHolder(inflater.inflate(R.layout.item_loading, null));
    }

    @Override
    public void onBindHolder(LoadingItemHolder holder, int position) {}

    @Override
    public int getType() {
        return 1;
    }

    public static final class LoadingItemHolder extends BindViewHolder {
        public LoadingItemHolder(View itemView) {
            super(itemView);
        }
    }
}
