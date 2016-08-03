package com.github.programmerr47.vkdiscussionviewer.chatlistpage;

import android.util.SparseArray;

import com.github.programmerr47.vkdiscussionviewer.model.User;
import com.github.programmerr47.vkdiscussionviewer.utils.ApiUtils;
import com.squareup.picasso.Picasso;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.api.VKParameters;
import com.vk.sdk.api.VKRequest;
import com.vk.sdk.api.VKResponse;
import com.vk.sdk.api.model.VKApiPhotoSize;
import com.vk.sdk.api.model.VKPhotoSizes;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.github.programmerr47.vkdiscussionviewer.VkApplication.globalStorage;
import static com.vk.sdk.VKAccessToken.currentToken;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */
public class ChatListUpdater implements OnChatsReceivedListener {
    private final ExecutorService requestExecutor = Executors.newSingleThreadExecutor();

    private final OnChatsPreparedListener listener;
    private Future currentTask;

    public ChatListUpdater(OnChatsPreparedListener listener) {
        this.listener = listener;
    }

    public void requestChats() {
        if (currentTask != null) {
            currentTask.cancel(true);
        }

        currentTask = requestExecutor.submit(new RetrieveChatsTask(this));
    }

    @Override
    public void onChatsReceived(final List<ChatItem> chats, final SparseArray<ChatItem> chatMap) {
        String idSequence = toIdSequence(chats);
        VKApi.messages().getChatUsers(VKParameters.from("chat_ids", idSequence, "fields", "photo,photo_50,photo_100,photo_200")).executeWithListener(new VKRequest.VKRequestListener() {
            @Override
            public void onComplete(VKResponse response) {
                super.onComplete(response);
                JSONObject responseObject = response.json.optJSONObject("response");
                for (Iterator<String> it = responseObject.keys(); it.hasNext();){
                    String key = it.next();
                    JSONArray jsonArray = responseObject.optJSONArray(key);
                    ChatItem chat = chatMap.get(Integer.parseInt(key));
                    chat.setParticipantsCount(jsonArray.length());

                    List<String> urls = new ArrayList<>();
                    for (int arrayIndex = 0; arrayIndex < jsonArray.length(); arrayIndex++) {
                        JSONObject userJson = jsonArray.optJSONObject(arrayIndex);
                        VKPhotoSizes userAvatarPhotoSizes = photoSizesFromJson(userJson);
                        int id = userJson.optInt("id");
                        String photoUrl = ApiUtils.getAppropriatePhotoUrl(userAvatarPhotoSizes);

                        globalStorage().cacheUser(new User().setId(id).setImageUrl(photoUrl));

                        if (id != currentToken().userIdInt || jsonArray.length() == 1) {
                            urls.add(photoUrl);
                        }

                        if (urls.size() >= 4) {
                            break;
                        }
                    }

                    if (chat.getUrls().isEmpty()) {
                        chat.setUrls(urls);
                    }
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

            private VKPhotoSizes photoSizesFromJson(JSONObject response) {
                VKPhotoSizes photoSizes = new VKPhotoSizes();
                if (response.has("photo_50")) {
                    photoSizes.add(VKApiPhotoSize.create(response.optString("photo_50"), VKApiPhotoSize.S, 50, 50));
                }
                if (response.has("photo_100")) {
                    photoSizes.add(VKApiPhotoSize.create(response.optString("photo_100"), VKApiPhotoSize.M, 100, 100));
                }
                if (response.has("photo_200")) {
                    photoSizes.add(VKApiPhotoSize.create(response.optString("photo_200"), VKApiPhotoSize.X, 200, 200));
                }

                if (photoSizes.isEmpty()) {
                    photoSizes.add(VKApiPhotoSize.create(response.optString("photo"), VKApiPhotoSize.S, 50, 50));
                } else {
                    photoSizes.sort();
                }

                return photoSizes;
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
