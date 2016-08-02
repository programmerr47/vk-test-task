package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.app.Application;

import com.github.programmerr47.vkdiscussionviewer.VkApplication;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiPhoto;
import com.vk.sdk.api.model.VKAttachments;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.globalStorage;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.uiHandler;
import static com.vk.sdk.api.model.VKAttachments.TYPE_PHOTO;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class GetMessagesTask {
    private static final int MESSAGES_DEFAULT_COUNT = 20;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future currentTask;

    private final int peerId;
    private final WeakReference<OnMessagesReceivedListener> weakListener;

    public GetMessagesTask(int chatId, OnMessagesReceivedListener listener) {
        this.peerId = 2000000000 + chatId;
        weakListener = new WeakReference<>(listener);
    }

    public void requestMessages(final int offset) {
        if (currentTask != null ) {
            currentTask.cancel(true);
        }

        currentTask = executorService.submit(new Runnable() {
            @Override
            public void run() {
                VKApi.messages().getChatHistory(VKParameters.from("count", MESSAGES_DEFAULT_COUNT, "offset", offset, "user_id", peerId))
                        .executeSyncWithListener(new VKRequest.VKRequestListener() {
                            @Override
                            public void onComplete(VKResponse response) {
                                super.onComplete(response);
                                VKApiGetMessagesResponse messagesResponse = (VKApiGetMessagesResponse) response.parsedModel;
                                List<Message> messages = new ArrayList<>();
                                for (VKApiMessage apiMessage : messagesResponse.items) {
                                    messages.add(new Message()
                                            .setId(apiMessage.id)
                                            .setAvatarUrl(globalStorage().getUser(apiMessage.user_id).getImageUrl())
                                            .setDate(apiMessage.date)
                                            .setContent(apiMessage.body)
                                            .setImageUrls(getImages(apiMessage)));
                                }
                                GetMessagesTask.this.notify(offset, messages);
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                                GetMessagesTask.this.notify(offset, Collections.<Message>emptyList());
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                            }

                            private List<String> getImages(VKApiMessage apiMessage) {
                                List<String> strings = new ArrayList<String>();
                                for (VKAttachments.VKApiAttachment apiAttachment : apiMessage.attachments) {
                                    if (TYPE_PHOTO.equals(apiAttachment.getType())) {
                                        VKApiPhoto photo = (VKApiPhoto) apiAttachment;
                                        strings.add(photo.photo_604);
                                    }
                                }

                                return strings;
                            }
                        });
            }
        });
    }

    private void notify(final int offset, final List<Message> messageList) {
        uiHandler().post(new Runnable() {
            @Override
            public void run() {
                OnMessagesReceivedListener listener = weakListener.get();
                if (listener != null) {
                    listener.onMessagesReceived(offset, messageList);
                }
            }
        });
    }

    public interface OnMessagesReceivedListener {
        void onMessagesReceived(int offset, List<Message> messageList);
    }
}
