package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.os.Handler;
import android.os.Looper;
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
    private List<Chat> items = new ArrayList<>();
    private SparseArray<Chat> itemMap = new SparseArray<>();

    private Runnable delayedPostAction;

    public RetrieveChatsTask(OnChatsReceivedListener listener) {
        weakListener = new WeakReference<>(listener);
    }

    @Override
    public void run() {
        if (Looper.myLooper() == null) {
            Looper.prepare();
        }
        handler = new Handler();
        if (delayedPostAction != null) {
            handler.post(delayedPostAction);
            delayedPostAction = null;
        }
        Looper.loop();
    }

    public void request() {
        Runnable postAction = new Runnable() {
            @Override
            public void run() {
                requestDialogsPart(0);
            }
        };

        if (handler == null) {
            delayedPostAction = postAction;
        } else {
            handler.post(postAction);
        }
    }

    //TODO add labeling attachments (because of empty body field)
    private void requestDialogsPart(final int offset) {
        VKApi.messages().getDialogs(VKParameters.from("offset", offset, "count", PART_COUNT)).executeSyncWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(final VKResponse response) {
                super.onComplete(response);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        VKList<VKApiDialog> dialogs = ((VKApiGetDialogResponse) response.parsedModel).items;
                        for (int i = 0; i < dialogs.size(); i++) {
                            VKApiMessage message = dialogs.get(i).message;
                            if (message.chat_id != 0) {
                                Chat chat = toChatItem(message);
                                items.add(chat);
                                itemMap.append(chat.getChatId(), chat);

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
//                    postResult();
                    postError(error);
                }

            }

            @Override
            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                super.onProgress(progressType, bytesLoaded, bytesTotal);
            }

            private Chat toChatItem(VKApiMessage message) {
                return new Chat()
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

    private void postError(final VKError error) {
        uiHandler().post(new Runnable() {
            @Override
            public void run() {
                if (weakListener.get() != null) {
                    weakListener.get().onError(error);
                }
            }
        });
    }
}
