package com.github.programmerr47.vkdiscussionviewer.chatpage;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class ChatListUpdater {
    private static final ExecutorService REQUEST_EXECUTOR = Executors.newSingleThreadExecutor();

    private Future currentTask;

    public void requestChats() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }

        currentTask = REQUEST_EXECUTOR.submit(new RetrieveChatsTask());
    }

    private static final class RetrieveChatsTask implements Runnable {
        private static final int PART_COUNT = 3;
        private static final int CHAT_MAX_COUNT = 17;

        private List<ChatItem> items = new ArrayList<>();

        @Override
        public void run() {
            requestDialogsPart(0);
            int t = 5;
        }

        private void requestDialogsPart(final int offset) {
            VKApi.messages().getDialogs(VKParameters.from("offset", offset, "count", PART_COUNT)).executeSyncWithListener(new VKRequest.VKRequestListener() {
                @Override
                public void onComplete(VKResponse response) {
                    super.onComplete(response);
                    VKList<VKApiDialog> dialogs = ((VKApiGetDialogResponse) response.parsedModel).items;
                    for (int i = 0; i < dialogs.size(); i++) {
                        VKApiMessage message = dialogs.get(i).message;
                        if (message.chat_id != 0) {
                            items.add(toChatItem(message));

                            if (items.size() >= CHAT_MAX_COUNT) {
                                return;
                            }
                        }
                    }

                    requestDialogsPart(offset + PART_COUNT);
                    int t = 5;
                }

                @Override
                public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                    super.attemptFailed(request, attemptNumber, totalAttempts);
                }

                @Override
                public void onError(VKError error) {
                    super.onError(error);
                }

                @Override
                public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                    super.onProgress(progressType, bytesLoaded, bytesTotal);
                }

                private ChatItem toChatItem(VKApiMessage message) {
                    return new ChatItem()
                            .setChatId(message.chat_id)
                            .setDate(message.date)
                            .setLastMessage(message.body)
                            .setTitle(message.title);
                }
            });
        }
    }
}
