package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.vk.sdk.api.methods.VKApiMessages;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public final class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemHolder> {

    @Override
    public ChatItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ChatItemHolder(inflater.inflate(R.layout.item_chat, parent));
    }

    @Override
    public void onBindViewHolder(ChatItemHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static final class ChatItemHolder extends BindViewHolder {
        final ImageView avatarView = bind(R.id.avatar);
        final TextView titleView = bind(R.id.title);
        final TextView lastMessageView = bind(R.id.last_message);
        final TextView timeView = bind(R.id.time);

        public ChatItemHolder(View rootView) {
            super(rootView);
        }
    }
}
