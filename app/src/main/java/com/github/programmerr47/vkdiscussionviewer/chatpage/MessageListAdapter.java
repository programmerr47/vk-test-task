package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.AdapterItemsUpdater;
import com.github.programmerr47.vkdiscussionviewer.utils.DateUtils;

import java.util.ArrayList;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.globalStorage;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private static final LoadingItem LOADING_ITEM = new LoadingItem();

    private final AdapterItemsUpdater updater = new AdapterItemsUpdater(this);
    private final List<ChatItem> chatItems;

    public MessageListAdapter(int chatId) {
        chatItems = new ArrayList<>(globalStorage().getChatHistory(chatId));
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        switch (viewType) {
            case 1:
                return new LoadingItem.Holder(inflater.inflate(R.layout.item_loading, parent, false));
            case 2:
                return new DateItem.Holder(inflater.inflate(R.layout.item_date, parent, false));
            default:
                return new MessageItem.Holder(inflater.inflate(R.layout.item_message, parent, false));
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

    public void rewrite(List<ChatItem> newItems) {
        int oldSize = chatItems.size();
        chatItems.clear();
        chatItems.addAll(newItems);
        updater.updateItems(0, newItems.size(), oldSize);
    }

    public void addSmallPartToBegin(List<ChatItem> items) {
        chatItems.addAll(0, items);
        notifyItemRangeInserted(0, items.size());
    }

    public void addItems(List<ChatItem> newItems) {
        int offset = chatItems.size();
        MessageItem lastMessage = getDisplayingMessage(chatItems.size() - 1);
        MessageItem firstNewMessage = getMessage(newItems, 0);

        int additionalCount = 0;
        if (lastMessage != null && firstNewMessage != null) {
            int comparison = DateUtils.compareDatesByDay(lastMessage.getDate(), firstNewMessage.getDate());
            if (comparison != 0) {
                chatItems.add(new DateItem(lastMessage.getDate()));
                additionalCount = 1;
            }
        }

        chatItems.addAll(newItems);
        notifyItemRangeInserted(offset, newItems.size() + additionalCount);
    }

    public void addOldestDate() {
        MessageItem messageItem = getDisplayingMessage(chatItems.size() - 1);
        if (messageItem != null) {
            DateItem oldestDateItem = new DateItem(messageItem.getDate());
            chatItems.add(oldestDateItem);
            notifyItemInserted(chatItems.size() - 1);
        }
    }

    private MessageItem getDisplayingMessage(int index) {
        return getMessage(chatItems, index);
    }

    private MessageItem getMessage(List<ChatItem> chatItems, int index) {
        if (index > 0 && chatItems.get(index) instanceof MessageItem) {
            return (MessageItem) chatItems.get(index);
        } else {
            return null;
        }
    }
}
