package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.model.VkPhotoSet;
import com.github.programmerr47.vkdiscussionviewer.utils.BindViewHolder;
import com.github.programmerr47.vkdiscussionviewer.utils.CircleTransform;
import com.github.programmerr47.vkdiscussionviewer.views.MessageView;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static android.widget.RelativeLayout.ALIGN_PARENT_RIGHT;
import static android.widget.RelativeLayout.LEFT_OF;
import static android.widget.RelativeLayout.RIGHT_OF;
import static com.vk.sdk.VKAccessToken.currentToken;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class MessageItem implements ChatItem<MessageItem.Holder> {
    private final static DateFormat TIME_FORMAT = new SimpleDateFormat("k:mm", Locale.ENGLISH);

    private int id;
    private int userId;
    private String avatarUrl;
    private long date;
    private String dateFormatted;
    private String content;
    private VkPhotoSet photoSet = new VkPhotoSet();

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
        this.dateFormatted = TIME_FORMAT.format(date * 1000);
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
    public void onBindHolder(Holder holder, int position) {
        holder.messageView.setMessage(this);

        if (userId != currentToken().userIdInt) {

            RelativeLayout.LayoutParams timeViewParams = (RelativeLayout.LayoutParams) holder.timeView.getLayoutParams();
            timeViewParams.addRule(LEFT_OF, 0);
            timeViewParams.addRule(RIGHT_OF, holder.messageView.getId());
            holder.timeView.setLayoutParams(timeViewParams);

            RelativeLayout.LayoutParams messageContentParams = (RelativeLayout.LayoutParams) holder.messageView.getLayoutParams();
            messageContentParams.addRule(ALIGN_PARENT_RIGHT, 0);
            holder.messageView.setLayoutParams(messageContentParams);
        } else {
            RelativeLayout.LayoutParams timeViewParams = (RelativeLayout.LayoutParams) holder.timeView.getLayoutParams();
            timeViewParams.addRule(RIGHT_OF, 0);
            timeViewParams.addRule(LEFT_OF, holder.messageView.getId());
            holder.timeView.setLayoutParams(timeViewParams);

            RelativeLayout.LayoutParams messageContentParams = (RelativeLayout.LayoutParams) holder.messageView.getLayoutParams();
            messageContentParams.addRule(ALIGN_PARENT_RIGHT);
            holder.messageView.setLayoutParams(messageContentParams);
        }

        holder.timeView.setText(dateFormatted);
    }

    @Override
    public int getType() {
        return 0;
    }

    public int getId() {
        return id;
    }

    public long getDate() {
        return date;
    }

    public String getDateFormatted() {
        return dateFormatted;
    }

    public String getContent() {
        return content;
    }

    public VkPhotoSet getPhotoSet() {
        return photoSet;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public boolean isOwner() {
        return userId == currentToken().userIdInt;
    }

    public static final class Holder extends BindViewHolder {
        final MessageView messageView = bind(R.id.attachment_photo);
        final TextView timeView = bind(R.id.time);

        public Holder(View rootView) {
            super(rootView);
        }
    }
}
