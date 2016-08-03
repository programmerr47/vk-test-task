package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.chatpage.LoadingItem.LoadingItemHolder;
import com.github.programmerr47.vkdiscussionviewer.chatpage.MessageItem.MessageItemHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private static final LoadingItem LOADING_ITEM = new LoadingItem();

    private List<ChatItem> chatItems = new ArrayList<>();
    private boolean isInProgress;

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == 0) {
            return new MessageItemHolder(inflater.inflate(R.layout.item_message, null));
        } else {
            return new LoadingItemHolder(inflater.inflate(R.layout.item_loading, null));
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        chatItems.get(position).onBindHolder(viewHolder, position);
    }

    @Override
    public final int getItemViewType(int position) {
        return chatItems.get(position).getType();
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public void showLoading() {
        chatItems.add(LOADING_ITEM);
        notifyItemInserted(chatItems.size() - 1);
    }

    public void hideLoading() {
        chatItems.remove(chatItems.size() - 1);
        notifyItemRemoved(chatItems.size());
    }

    public void addItems(List<ChatItem> newItems) {
        int offset = chatItems.size();
        chatItems.addAll(newItems);
        notifyItemRangeInserted(offset, newItems.size());
    }
}
