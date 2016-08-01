package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.pager.Page;
import com.github.programmerr47.vkdiscussionviewer.utils.CustomTypefaceSpan;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.color;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.plural;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_MEDIUM;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_REGULAR;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class ChatPage extends Page {
    private final ChatItem chatItem;

    public ChatPage(ChatItem chatItem) {
        this.chatItem = chatItem;
    }

    private Toolbar toolbar;

    @Override
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.page_chat, null);
    }

    @Override
    public void onViewCreated(View pageView) {
        toolbar = bind(R.id.toolbar);

        prepareToolbar();
    }

    private void prepareToolbar() {
        CustomTypefaceSpan robotoMediumSpan = new CustomTypefaceSpan(ROBOTO_MEDIUM);
        SpannableStringBuilder toolbarTitleBuilder = new SpannableStringBuilder(chatItem.getTitle());
        toolbarTitleBuilder.setSpan(robotoMediumSpan, 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarTitleBuilder.setSpan(new AbsoluteSizeSpan(16, true), 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(toolbarTitleBuilder);

        String toolbarSubtitleOrigin = plural(
                toolbar.getContext(), R.plurals.chat_participant,
                chatItem.getParticipantsCount(), chatItem.getParticipantsCount());
        CustomTypefaceSpan robotoRegularSpan = new CustomTypefaceSpan(ROBOTO_REGULAR);
        SpannableStringBuilder toolbarSubtitleBuilder = new SpannableStringBuilder(toolbarSubtitleOrigin);
        toolbarSubtitleBuilder.setSpan(robotoRegularSpan, 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarSubtitleBuilder.setSpan(new AbsoluteSizeSpan(14, true), 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarSubtitleBuilder.setSpan(new ForegroundColorSpan(color(R.color.text_secondary_dark)), 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setSubtitle(toolbarSubtitleBuilder);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(closePageListener);
    }
}
