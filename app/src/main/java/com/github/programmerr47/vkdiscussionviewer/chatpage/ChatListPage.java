package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.pager.Page;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public final class ChatListPage extends Page {

    private final ChatListUpdater updater = new ChatListUpdater();
    private RecyclerView recyclerView;

    @Override
    public View onCreateView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.page_chat_list, null);
    }

    @Override
    public void onViewCreated(View pageView) {
        updater.requestChats();
    }
}
