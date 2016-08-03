package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.VkApplication;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatListAdapter;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.github.programmerr47.vkdiscussionviewer.utils.AdapterItemsUpdater;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.BitmapUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.CircleTransform;
import com.github.programmerr47.vkdiscussionviewer.utils.DateFormatter;
import com.github.programmerr47.vkdiscussionviewer.views.PhotoAttachmentView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

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
            VkPhotoSet photoSet = item.getPhotoSet();

            if (photoSet.isEmpty()) {
                holder.attachmentPhoto.setVisibility(GONE);
            } else {
                holder.attachmentPhoto.setVisibility(VISIBLE);
            }

            holder.attachmentPhoto.setPhotoSet(photoSet);
            Picasso.with(null).load(item.getAvatarUrl()).transform(CircleTransform.INSTANCE).into(holder.avatarView);

            if (isEmpty(item.getContent())) {
                holder.textView.setVisibility(GONE);
            } else {
                holder.textView.setText(item.getContent());
                holder.textView.setVisibility(VISIBLE);
            }

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
        final PhotoAttachmentView attachmentPhoto = bind(R.id.attachment_photo);
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
