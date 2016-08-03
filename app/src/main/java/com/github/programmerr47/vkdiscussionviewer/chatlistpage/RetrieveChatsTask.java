package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.utils.ApiUtils;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiDialog;
import com.vk.sdk.api.model.VKApiGetDialogResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.uiHandler;
import static com.github.programmerr47.vkdiscussionviewer.utils.ApiUtils.getMessageContent;

/**
 * @author Michael Spitsin
 * @since 2016-08-01
 */
public class RetrieveChatsTask implements Runnable {
    private static final int PART_COUNT = 200;
    private static final int CHAT_MAX_COUNT = 17;

    private final WeakReference<OnChatsReceivedListener> weakListener;

    private Handler handler;
    private List<ChatItem> items = new ArrayList<>();
    private SparseArray<ChatItem> itemMap = new SparseArray<>();

    public RetrieveChatsTask(OnChatsReceivedListener listener) {
        weakListener = new WeakReference<>(listener);
    }

    @Override
    public void run() {
        Looper.prepare();
        handler = new Handler();
        requestDialogsPart(0);
        Looper.loop();
    }

    //TODO add labeling attachments (because of empty body field)
    private void requestDialogsPart(final int offset) {
        Log.v("FUCK", "requestDialogsPArt " + offset);
        VKApi.messages().getDialogs(VKParameters.from("offset", offset, "count", PART_COUNT)).executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                Log.v("FUCK", "requestDialogsPArt response " + offset);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        VKList<VKApiDialog> dialogs = ((VKApiGetDialogResponse) response.parsedModel).items;
                        Log.v("FUCK", "requestDialogsPArt response in handler dialogsize = " + dialogs.size());
                        for (int i = 0; i < dialogs.size(); i++) {
                            VKApiMessage message = dialogs.get(i).message;
                            if (message.chat_id != 0) {
                                ChatItem chatItem = toChatItem(message);
                                items.add(chatItem);
                                itemMap.append(chatItem.getChatId(), chatItem);

                                if (items.size() >= CHAT_MAX_COUNT) {
                                    postResult();
                                    return;
                                }
                            }
                        }

                        requestDialogsPart(offset + PART_COUNT);
                    }
                });
            }

            @Override
            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                super.attemptFailed(request, attemptNumber, totalAttempts);
            }

            @Override
            public void onError(VKError error) {
                super.onError(error);
                if (error.errorCode == VKError.VK_API_ERROR && error.apiError.errorCode == 6) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            requestDialogsPart(offset);
                        }
                    }, 500);
                } else {
                    postResult();
                }

            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            private ChatItem toChatItem(VKApiMessage message) {
                return new ChatItem()
                        .setChatId(message.chat_id)
                        .setDate(message.date)
                        .setLastMessage(getMessageContent(message))
                        .setTitle(message.title)
                        .setUrls(retrieveChatPhoto(message));
            }

            private List<String> retrieveChatPhoto(VKApiMessage message) {
                List<String> urlContainer = new ArrayList<>();
                String avatarUrl = ApiUtils.getAppropriateAvatarUrl(message.src);
                if (avatarUrl != null) {
                    urlContainer.add(avatarUrl);
                }

                return urlContainer;
            }
        });
    }

    private void postResult() {
        uiHandler().post(new Runnable() {
            @Override
            public void run() {
                if (weakListener.get() != null) {
                    weakListener.get().onChatsReceived(items, itemMap);
                }
            }
        });
    }
}
