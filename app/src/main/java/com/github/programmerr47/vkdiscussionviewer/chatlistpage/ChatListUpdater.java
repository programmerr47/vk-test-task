package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.util.SparseArray;

import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class ChatListUpdater implements OnChatsReceivedListener {
    private static final ExecutorService REQUEST_EXECUTOR = Executors.newSingleThreadExecutor();

    private final OnChatsPreparedListener listener;
    private Future currentTask;

    public ChatListUpdater(OnChatsPreparedListener listener) {
        this.listener = listener;
    }

    public void requestChats() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }

        currentTask = REQUEST_EXECUTOR.submit(new RetrieveChatsTask(this));
    }

    @Override
    public void onChatsReceived(final List<ChatItem> chats, final SparseArray<ChatItem> chatMap) {
        String idSequence = toIdSequence(chats);
        VKApi.messages().getChatUsers(VKParameters.from("chat_ids", idSequence, "fields", "photo")).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject responseObject = response.json.optJSONObject("response");
                for (Iterator<String> it = responseObject.keys(); it.hasNext();){
                    String key = it.next();
                    JSONArray jsonArray = responseObject.optJSONArray(key);
                    List<String> urls = new ArrayList<>();
                    for (int arrayIndex = 0; arrayIndex < jsonArray.length(); arrayIndex++) {
                        String photoUrl = jsonArray.optJSONObject(arrayIndex).optString("photo");
                        urls.add(photoUrl);

                        if (urls.size() >= 4) {
                            break;
                        }
                    }
                    chatMap.get(Integer.parseInt(key)).setUrls(urls);
                    chatMap.get(Integer.parseInt(key)).setParticipantsCount(jsonArray.length());
                }
                listener.onChatsReady(chats);
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
        });
    }

    private String toIdSequence(List<ChatItem> chats) {
        StringBuilder resultBuilder = new StringBuilder("" + chats.get(0).getChatId());
        for (int i = 1; i < chats.size(); i++) {
            resultBuilder.append(",").append(chats.get(i).getChatId());
        }

        return resultBuilder.toString();
    }
}
