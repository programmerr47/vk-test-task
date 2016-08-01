package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.VkApplication;
import com.github.programmerr47.vkdiscussionviewer.utils.AdapterItemsUpdater;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.BitmapUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.DateFormatter;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public final class ChatListAdapter extends RecyclerView.Adapter<ChatListAdapter.ChatItemHolder> {
    private final Loader loader = new Loader();
    private final AdapterItemsUpdater itemsUpdater = new AdapterItemsUpdater(this);

    private final OnChatClickedListener listener;

    private List<ChatItem> chatItems = Collections.emptyList();

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
        final ChatItem item = chatItems.get(position);
        holder.bindChatId(item);

        loader.load(item, holder.avatarView);
        holder.lastMessageView.setText(item.getLastMessage());
        holder.titleView.setText(item.getTitle());
        holder.timeView.setText(DateFormatter.formatDate(item.getDate()));
    }

    @Override
    public int getItemCount() {
        return chatItems.size();
    }

    public void updateItems(List<ChatItem> newItems) {
        int lastSize = chatItems.size();
        chatItems = newItems;
        itemsUpdater.updateItems(0, chatItems.size(), lastSize);
    }

    public static final class ChatItemHolder extends BindViewHolder implements View.OnClickListener {
        final ImageView avatarView = bind(R.id.avatar);
        final TextView titleView = bind(R.id.title);
        final TextView lastMessageView = bind(R.id.last_message);
        final TextView timeView = bind(R.id.time);

        final OnChatClickedListener listener;

        ChatItem chat;

        public ChatItemHolder(View rootView, OnChatClickedListener listener) {
            super(rootView);
            this.listener = listener;
            rootView.setOnClickListener(this);
        }

        public void bindChatId(ChatItem chat) {
            this.chat = chat;
        }


        @Override
        public void onClick(View view) {
            listener.onChatClicked(chat);
        }
    }

    //TODO replace it with some caching mechanism and more smooth opening of bitmaps
    private static final class Loader {
        private final ExecutorService executorService = Executors.newSingleThreadExecutor();

        public void load(final ChatItem item, final ImageView imageView) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    List<String> avatarUrls = item.getUrls();

                    List<Bitmap> bitmaps = new ArrayList<>();
                    for (String url : avatarUrls) {
                        try {
                            bitmaps.add(Picasso.with(null).load(url).get());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    final Bitmap result = BitmapUtils.transformsAvatars(bitmaps);
                    VkApplication.uiHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            imageView.setImageBitmap(result);
                        }
                    });
                }
            });
        }
    }

    public interface OnChatClickedListener {
        void onChatClicked(ChatItem chat);
    }
}
