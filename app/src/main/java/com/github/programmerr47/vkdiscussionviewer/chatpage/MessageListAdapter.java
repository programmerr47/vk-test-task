package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.VkApplication;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatListAdapter;
import com.github.programmerr47.vkdiscussionviewer.utils.AdapterItemsUpdater;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.BitmapUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.CircleTransform;
import com.github.programmerr47.vkdiscussionviewer.utils.DateFormatter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class MessageListAdapter extends RecyclerView.Adapter {
    private List<Message> messageItems = new ArrayList<>();
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
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof MessageItemHolder) {
            MessageItemHolder holder = (MessageItemHolder) viewHolder;

            Message item = messageItems.get(position);
            List<String> imageUrls = item.getImageUrls();

            if (imageUrls.isEmpty()) {
                holder.attachmentPhoto.setVisibility(View.GONE);
            } else {
                holder.attachmentPhoto.setVisibility(View.VISIBLE);
                Picasso.with(null).load(imageUrls.get(0)).into(holder.attachmentPhoto);
            }

            Picasso.with(null).load(item.getAvatarUrl()).transform(CircleTransform.INSTANCE).into(holder.avatarView);

            holder.textView.setText(item.getContent());
            holder.timeView.setText(DateFormatter.formatDate(item.getDate()));
        }
    }

    @Override
    public final int getItemViewType(int position) {
        return position == messageItems.size() ? 1 : 0;
    }

    @Override
    public int getItemCount() {
        return messageItems.size() + (isInProgress ? 1 : 0);
    }

    public void showLoading() {
        isInProgress = true;
        notifyItemInserted(messageItems.size());
    }

    public void hideLoading() {
        isInProgress = false;
        notifyItemRemoved(messageItems.size());
    }

    public void addItems(List<Message> newItems) {
        int offset = messageItems.size();
        messageItems.addAll(newItems);
        notifyItemRangeInserted(offset, newItems.size());
    }

    public static final class MessageItemHolder extends BindViewHolder {
        final ImageView avatarView = bind(R.id.avatar);
        final TextView textView = bind(R.id.text);
        final ImageView attachmentPhoto = bind(R.id.attachment_photo);
        final TextView timeView = bind(R.id.time);

        public MessageItemHolder(View rootView) {
            super(rootView);
        }
    }

    public static final class LoadingItemHolder extends BindViewHolder {
        public LoadingItemHolder(View itemView) {
            super(itemView);
        }
    }
}
