package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.Chat;
import com.github.programmerr47.vkdiscussionviewer.chatpage.ChatItem;
import com.github.programmerr47.vkdiscussionviewer.pager.Page;
import com.github.programmerr47.vkdiscussionviewer.utils.CustomTypefaceSpan;

import java.util.List;

import static android.support.v7.widget.LinearLayoutManager.VERTICAL;
import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.avatarLoader;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.color;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.plural;
import static com.github.programmerr47.vkdiscussionviewer.utils.AnimationUtils.hideView;
import static com.github.programmerr47.vkdiscussionviewer.utils.AnimationUtils.showView;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_MEDIUM;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_REGULAR;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class ChatPage extends Page implements GetMessagesTask.OnMessagesReceivedListener {
    private final MessageListAdapter adapter = new MessageListAdapter();
    private final Chat chat;
    private final GetMessagesTask messagesTask;

    private RecyclerView messagesView;
    private LinearLayoutManager messagesLayoutManager;
    private ProgressBar progressBar;
    private TextView emptyMessagesLabel;
    private ImageView avatarView;

    private boolean isMessagesLoading;
    private boolean isHistoryFullyLoaded;

    public ChatPage(Chat chat) {
        this.chat = chat;
        messagesTask = new GetMessagesTask(chat.getChatId(), this);
        uiWorks.add(new Runnable() {
            @Override
            public void run() {
                isMessagesLoading = true;
                messagesTask.requestMessages(0);
            }
        });
    }

    private Toolbar toolbar;

    @Override
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.page_chat, null);
    }

    @Override
    public void onViewCreated(View pageView) {
        toolbar = bind(R.id.toolbar);
        messagesView = bind(R.id.list);
        progressBar = bind(R.id.progress);
        emptyMessagesLabel = bind(R.id.empty_text);
        avatarView = bind(R.id.avatar);

        prepareToolbar();
        prepareList();
        avatarLoader().load(chat, avatarView);
    }

    @Override
    public void onMessagesReceived(int offset, List<ChatItem> chatItems) {
        if (offset > 0) {
            adapter.hideLoading();
        }

        isMessagesLoading = false;

        hideView(progressBar);
        if (chatItems.isEmpty()) {
            isHistoryFullyLoaded = true;

            if (adapter.getItemCount() == 0) {
                showView(emptyMessagesLabel);
            } else {
                adapter.addOldestDate();
            }
        } else {
            adapter.addItems(chatItems);
        }
    }

    private void prepareToolbar() {
        CustomTypefaceSpan robotoMediumSpan = new CustomTypefaceSpan(ROBOTO_MEDIUM);
        SpannableStringBuilder toolbarTitleBuilder = new SpannableStringBuilder(chat.getTitle());
        toolbarTitleBuilder.setSpan(robotoMediumSpan, 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarTitleBuilder.setSpan(new AbsoluteSizeSpan(16, true), 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(toolbarTitleBuilder);

        String toolbarSubtitleOrigin = plural(
                toolbar.getContext(), R.plurals.chat_participant,
                chat.getParticipantsCount(), chat.getParticipantsCount());
        CustomTypefaceSpan robotoRegularSpan = new CustomTypefaceSpan(ROBOTO_REGULAR);
        SpannableStringBuilder toolbarSubtitleBuilder = new SpannableStringBuilder(toolbarSubtitleOrigin);
        toolbarSubtitleBuilder.setSpan(robotoRegularSpan, 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarSubtitleBuilder.setSpan(new AbsoluteSizeSpan(14, true), 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarSubtitleBuilder.setSpan(new ForegroundColorSpan(color(R.color.text_secondary_dark)), 0, toolbarSubtitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setSubtitle(toolbarSubtitleBuilder);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_left_white_24dp);
        toolbar.setNavigationOnClickListener(closePageListener);
    }

    private void prepareList() {
        messagesLayoutManager = new LinearLayoutManager(view.getContext(), VERTICAL, true);
        messagesView.setLayoutManager(messagesLayoutManager);
        messagesView.setAdapter(adapter);

        messagesView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 && !isHistoryFullyLoaded) {
                    int count = messagesLayoutManager.findLastVisibleItemPosition() - messagesLayoutManager.findFirstVisibleItemPosition() + 1;
                    if (adapter.getItemCount() < messagesLayoutManager.findLastVisibleItemPosition() + count && !isMessagesLoading) {
                        isMessagesLoading = true;
                        adapter.showLoading();
                        messagesTask.requestMessages(adapter.getItemCount());
                    }
                }
            }
        });
    }
}
