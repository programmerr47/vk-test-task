package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.AdapterItemsUpdater;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.DateFormatter;

import java.util.Collections;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.avatarLoader;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public final class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemHolder> {
    private final AdapterItemsUpdater itemsUpdater = new AdapterItemsUpdater(this);

    private final OnChatClickedListener listener;

    private List<Chat> chats = Collections.emptyList();

    public ChatListAdapter(OnChatClickedListener listener) {
        this.listener = listener;
    }

    @Override
    public ChatItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChatItemHolder(inflater.inflate(R.layout.item_chat, null), listener);
    }

    @Override
    public void onBindViewHolder(ChatItemHolder holder, int position) {
        final Chat item = chats.get(position);
        holder.bindChatId(item);

        avatarLoader().load(item, holder.avatarView);
        holder.lastMessageView.setText(item.getLastMessage());
        holder.titleView.setText(item.getTitle());
        holder.timeView.setText(DateFormatter.formatDate(item.getDate()));
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }

    public void updateItems(List<Chat> newItems) {
        int lastSize = chats.size();
        chats = newItems;
        itemsUpdater.updateItems(0, chats.size(), lastSize);
    }

    public static final class ChatItemHolder extends BindViewHolder implements View.OnClickListener {
        final ImageView avatarView = bind(R.id.avatar);
        final TextView titleView = bind(R.id.title);
        final TextView lastMessageView = bind(R.id.last_message);
        final TextView timeView = bind(R.id.time);

        final OnChatClickedListener listener;

        Chat chat;

        public ChatItemHolder(View rootView, OnChatClickedListener listener) {
            super(rootView);
            this.listener = listener;
            rootView.setOnClickListener(this);
        }

        public void bindChatId(Chat chat) {
            this.chat = chat;
        }


        @Override
        public void onClick(View view) {
            listener.onChatClicked(chat);
        }
    }

    public interface OnChatClickedListener {
        void onChatClicked(Chat chat);
    }
}
