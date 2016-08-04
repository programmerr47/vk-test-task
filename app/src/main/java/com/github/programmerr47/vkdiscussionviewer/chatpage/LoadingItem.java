package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;

/**
 * @author Michael Spitsin
 * @since 2016-08-04
 */
public class LoadingItem implements ChatItem<LoadingItem.Holder> {
    @Override
    public void onBindHolder(Holder holder, int position) {}

    @Override
    public int getType() {
        return 1;
    }

    public static final class Holder extends BindViewHolder {
        public Holder(View itemView) {
            super(itemView);
        }
    }
}
