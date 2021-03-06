package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.model.User;
import com.github.programmerr47.vkdiscussionviewer.utils.ApiUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.DateUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.PhotoSetCreator;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.methods.VKApiUsers;
import com.vk.sdk.api.model.VKApiGetMessagesResponse;
import com.vk.sdk.api.model.VKApiMessage;
import com.vk.sdk.api.model.VKApiUserFull;
import com.vk.sdk.api.model.VKList;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.globalStorage;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.uiHandler;
import static com.github.programmerr47.vkdiscussionviewer.utils.ApiUtils.getMessageContent;

/**
 * @author Michael Spitsin
 * @since 2016-08-02
 */
public class GetMessagesTask {
    public static final int APPEND_NEW_ITEMS = 0;
    public static final int REWRITE_CACHED = 1;
    public static final int UPDATE_START_PART = 2;
    public static final int ERROR = 3;

    private static final int MESSAGES_DEFAULT_COUNT = 20;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future currentTask;

    private final int chatId;
    private final int peerId;
    private final WeakReference<OnMessagesReceivedListener> weakListener;

    public GetMessagesTask(int chatId, OnMessagesReceivedListener listener) {
        this.chatId = chatId;
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
                                SparseArray<List<MessageItem>> messagesByUserId = new SparseArray<>();

                                VKApiGetMessagesResponse messagesResponse = (VKApiGetMessagesResponse) response.parsedModel;
                                List<ChatItem> chatItems = new ArrayList<>();
                                MessageItem newestCachedMessageItem = getFirstCachedMessage();
                                long lastDate = -1;

                                for (final VKApiMessage apiMessage : messagesResponse.items) {
                                    if (lastDate != -1) {
                                        int comparison = DateUtils.compareDatesByDay(lastDate, apiMessage.date);
                                        if (comparison != 0) {
                                            chatItems.add(new DateItem(lastDate));
                                        }
                                    }

                                    if (newestCachedMessageItem != null && newestCachedMessageItem.getId() == apiMessage.id) {
                                        globalStorage().cacheMessageNewSmallPart(chatId, chatItems);
                                        GetMessagesTask.this.notify(offset, chatItems, UPDATE_START_PART);
                                        return;
                                    }

                                    String avatarUrl;
                                    if (!globalStorage().hasUser(apiMessage.user_id)) {
                                        avatarUrl = "";
                                    } else {
                                        avatarUrl = globalStorage().getUser(apiMessage.user_id).getImageUrl();
                                    }

                                    MessageItem messageItem = new MessageItem()
                                            .setId(apiMessage.id)
                                            .setUserId(apiMessage.user_id)
                                            .setAvatarUrl(avatarUrl)
                                            .setDate(apiMessage.date)
                                            .setContent(getMessageContent(apiMessage))
                                            .setPhotoSet(PhotoSetCreator.createPhotoSet(apiMessage.attachments));

                                    if (!globalStorage().hasUser(apiMessage.user_id)) {
                                        if (messagesByUserId.indexOfKey(apiMessage.user_id) < 0) {
                                            messagesByUserId.put(apiMessage.user_id, new ArrayList<MessageItem>());
                                        }

                                        messagesByUserId.get(apiMessage.user_id).add(messageItem);
                                    }

                                    chatItems.add(messageItem);

                                    lastDate = apiMessage.date;
                                }

                                if (messagesResponse.items.size() < MESSAGES_DEFAULT_COUNT && !messagesResponse.items.isEmpty()) {
                                    chatItems.add(new DateItem(lastDate));
                                }

                                if (offset == 0 && newestCachedMessageItem != null) {
                                    globalStorage().rewriteChat(chatId, chatItems);
                                    GetMessagesTask.this.notify(offset, chatItems, REWRITE_CACHED);
                                } else {
                                    globalStorage().cachePart(chatId, chatItems);
                                    GetMessagesTask.this.notify(offset, chatItems, APPEND_NEW_ITEMS);
                                }
                                uploadMissingUsers(messagesByUserId);
                            }

                            @Override
                            public void attemptFailed(VKRequest request, int attemptNumber, int totalAttempts) {
                                super.attemptFailed(request, attemptNumber, totalAttempts);
                            }

                            @Override
                            public void onError(VKError error) {
                                super.onError(error);
                                GetMessagesTask.this.notify(offset, Collections.<ChatItem>emptyList(), ERROR);
                            }

                            @Override
                            public void onProgress(VKRequest.VKProgressType progressType, long bytesLoaded, long bytesTotal) {
                                super.onProgress(progressType, bytesLoaded, bytesTotal);
                            }

                            private MessageItem getFirstCachedMessage() {
                                List<ChatItem> cachedMessages = globalStorage().getChatHistory(chatId);
                                for (int i = 0; i < cachedMessages.size(); i++) {
                                    if (cachedMessages.get(i) instanceof MessageItem) {
                                        return (MessageItem) cachedMessages.get(i);
                                    }
                                }

                                return null;
                            }
                        });
            }
        });
    }

    private void notify(final int offset, final List<ChatItem> chatItems, final int taskId) {
        uiHandler().post(new Runnable() {
            @Override
            public void run() {
                OnMessagesReceivedListener listener = weakListener.get();
                if (listener != null) {
                    listener.onMessagesReceived(offset, chatItems, taskId);
                }
            }
        });
    }

    private void uploadMissingUsers(final SparseArray<List<MessageItem>> messagesByUserId) {
        if (messagesByUserId.size() != 0) {
            final String idSequence = toIdSequence(messagesByUserId);
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    VKApi.users()
                            .get(VKParameters.from("user_ids", idSequence, "fields", "photo,photo_50,photo_100,photo_200"))
                            .executeSyncWithListener(new VKRequest.VKRequestListener() {
                                @Override
                                public void onComplete(VKResponse response) {
                                    super.onComplete(response);
                                    VKList<VKApiUserFull> apiUsers = (VKList<VKApiUserFull>) response.parsedModel;
                                    for (final VKApiUserFull apiUser : apiUsers) {
                                        final String photoUrl = ApiUtils.getAppropriateAvatarUrl(apiUser.photo);
                                        globalStorage().cacheUser(new User().setId(apiUser.id).setImageUrl(photoUrl));

                                        uiHandler().post(new Runnable() {
                                            @Override
                                            public void run() {
                                                List<MessageItem> messageItems = messagesByUserId.get(apiUser.id);
                                                for (int i = 0; i < messageItems.size(); i++) {
                                                    messageItems.get(i).updateDelayedAvatar(photoUrl);
                                                }
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
        }
    }

    private static String toIdSequence(SparseArray<List<MessageItem>> messagesByUserId) {
        StringBuilder resultBuilder = new StringBuilder("" + messagesByUserId.keyAt(0));
        for (int i = 1; i < messagesByUserId.size(); i++) {
            resultBuilder.append(",").append(messagesByUserId.keyAt(i));
        }

        return resultBuilder.toString();
    }

    public interface OnMessagesReceivedListener {
        void onMessagesReceived(int offset, List<ChatItem> chatItems, int taskId);
    }
}
