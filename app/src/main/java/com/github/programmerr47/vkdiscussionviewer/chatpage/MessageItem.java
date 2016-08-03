package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.CircleTransform;
import com.github.programmerr47.vkdiscussionviewer.utils.DateFormatter;
import com.github.programmerr47.vkdiscussionviewer.views.PhotoAttachmentView;
import com.squareup.picasso.Picasso;

import static android.text.TextUtils.isEmpty;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_LEFT;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;
import static com.vk.sdk.VKAccessToken.currentToken;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class MessageItem implements ChatItem<MessageItem.MessageItemHolder> {
    private int id;
    private int userId;
    private String avatarUrl;
    private long date;
    private String content;
    private VkPhotoSet photoSet;

    MessageItem setId(int id) {
        this.id = id;
        return this;
    }

    public MessageItem setUserId(int userId) {
        this.userId = userId;
        return this;
    }

    MessageItem setAvatarUrl(String url) {
        this.avatarUrl = url;
        return this;
    }

    MessageItem setDate(long date) {
        this.date = date;
        return this;
    }

    MessageItem setContent(String content) {
        this.content = content;
        return this;
    }

    MessageItem setPhotoSet(VkPhotoSet photoSet) {
        this.photoSet = photoSet;
        return this;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHolder(LayoutInflater inflater) {
        return new MessageItemHolder(inflater.inflate(R.layout.item_message, null));
    }

    @Override
    public void onBindHolder(MessageItemHolder holder, int position) {
        if (photoSet.isEmpty()) {
            holder.attachmentPhoto.setVisibility(GONE);
        } else {
            holder.attachmentPhoto.setVisibility(VISIBLE);
        }

        holder.attachmentPhoto.setPhotoSet(photoSet);

        if (isEmpty(content)) {
            holder.textView.setVisibility(GONE);
        } else {
            holder.textView.setText(content);
            holder.textView.setVisibility(VISIBLE);
        }

        RelativeLayout.LayoutParams timeViewParams = (RelativeLayout.LayoutParams) holder.timeView.getLayoutParams();
        RelativeLayout.LayoutParams messageContentParams = (RelativeLayout.LayoutParams) holder.messageContent.getLayoutParams();
        if (userId != currentToken().userIdInt) {
            holder.messageContent.setBackgroundResource(R.drawable.inbox_message_bg);

            Picasso.with(null).load(avatarUrl).transform(CircleTransform.INSTANCE).into(holder.avatarView);
            holder.avatarView.setVisibility(VISIBLE);

            timeViewParams.addRule(ALIGN_PARENT_LEFT, 0);
            timeViewParams.addRule(LEFT_OF, 0);
            timeViewParams.addRule(ALIGN_PARENT_RIGHT);
            timeViewParams.addRule(RIGHT_OF, holder.messageContent.getId());
            holder.timeView.setGravity(Gravity.LEFT);
            messageContentParams.addRule(ALIGN_PARENT_RIGHT, 0);
            messageContentParams.addRule(RIGHT_OF, holder.avatarView.getId());
        } else {
            holder.messageContent.setBackgroundResource(R.drawable.outbox_message_bg);

            holder.avatarView.setVisibility(GONE);

            timeViewParams.addRule(ALIGN_PARENT_RIGHT, 0);
            timeViewParams.addRule(RIGHT_OF, 0);
            timeViewParams.addRule(ALIGN_PARENT_LEFT);
            timeViewParams.addRule(LEFT_OF, holder.messageContent.getId());
            holder.timeView.setGravity(Gravity.RIGHT);
            messageContentParams.addRule(RIGHT_OF, 0);
            messageContentParams.addRule(ALIGN_PARENT_RIGHT);
        }
        holder.timeView.setLayoutParams(timeViewParams);

        holder.timeView.setText(DateFormatter.formatDate(date));
    }

    @Override
    public int getType() {
        return 0;
    }

    public static final class MessageItemHolder extends BindViewHolder {
        final ImageView avatarView = bind(R.id.avatar);
        final View messageContent = bind(R.id.message_content);
        final TextView textView = bind(R.id.text);
        final PhotoAttachmentView attachmentPhoto = bind(R.id.attachment_photo);
        final TextView timeView = bind(R.id.time);


        public MessageItemHolder(View rootView) {
            super(rootView);
        }
    }
}
