package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.chatlistpage.ChatListAdapter.OnChatClickedListener;
import com.github.programmerr47.vkdiscussionviewer.chatpage.ChatPage;
import com.github.programmerr47.vkdiscussionviewer.pager.Page;
import com.github.programmerr47.vkdiscussionviewer.utils.CustomTypefaceSpan;

import java.util.ArrayList;
import java.util.List;

import static android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.string;
import static com.github.programmerr47.vkdiscussionviewer.utils.AnimationUtils.hideView;
import static com.github.programmerr47.vkdiscussionviewer.utils.AnimationUtils.showView;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_MEDIUM;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public final class ChatListPage extends Page implements OnChatsPreparedListener, OnChatClickedListener {

    private final ChatListUpdater updater = new ChatListUpdater(this);
    private ChatListAdapter adapter = new ChatListAdapter(this);
    private List<ChatItem> chats = new ArrayList<>();
    private boolean isChatsLoaded;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TextView emptyListLabel;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.page_chat_list, null);
    }

    @Override
    public void onViewCreated(View pageView) {
        toolbar = bind(R.id.toolbar);
        recyclerView = bind(R.id.list);
        emptyListLabel = bind(R.id.empty_text);
        progressBar = bind(R.id.progress);

        prepareToolbar();
        prepareList();

        if (isChatsLoaded) {
            progressBar.setVisibility(GONE);
            if (chats.isEmpty()) {
                emptyListLabel.setVisibility(VISIBLE);
            } else {
                emptyListLabel.setVisibility(GONE);
            }
        } else {
            progressBar.setVisibility(VISIBLE);
            emptyListLabel.setVisibility(GONE);
            updater.requestChats();
        }
    }

    @Override
    public void onChatsReady(List<ChatItem> chats) {
        isChatsLoaded = true;
        this.chats = chats;
        adapter.updateItems(chats);

        hideView(progressBar);
        if (chats.isEmpty()) {
            showView(emptyListLabel);
        }
    }

    @Override
    public void onChatClicked(ChatItem chatItem) {
        pagerListener.openPage(new ChatPage(chatItem));
    }

    private void prepareToolbar() {
        String toolbarTitleOrigin = string(toolbar.getContext(), R.string.chat_list_title);
        CustomTypefaceSpan robotoMediumSpan = new CustomTypefaceSpan(ROBOTO_MEDIUM);
        SpannableStringBuilder toolbarTitleBuilder = new SpannableStringBuilder(toolbarTitleOrigin);
        toolbarTitleBuilder.setSpan(robotoMediumSpan, 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbarTitleBuilder.setSpan(new AbsoluteSizeSpan(20, true), 0, toolbarTitleBuilder.length(), SPAN_EXCLUSIVE_EXCLUSIVE);
        toolbar.setTitle(toolbarTitleBuilder);
    }

    private void prepareList() {
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        recyclerView.setAdapter(adapter);
    }
}
