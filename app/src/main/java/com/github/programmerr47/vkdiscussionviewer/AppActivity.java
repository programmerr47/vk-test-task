package com.github.programmerr47.vkdiscussionviewer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.programmerr47.vkdiscussionviewer.chatpage.ChatListPage;
import com.vk.sdk.VKAccessToken;
import com.vk.sdk.VKCallback;
import com.vk.sdk.VKScope;
import com.vk.sdk.VKSdk;
import com.vk.sdk.api.VKApi;
import com.vk.sdk.api.VKError;
import com.vk.sdk.util.VKUtil;

public class AppActivity extends AppCompatActivity {

    private AppFragment appFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        appFragment = (AppFragment) getFragmentManager().findFragmentById(R.id.app_fragment);

        //String[] str = VKUtil.getCertificateFingerprint(this, "com.github.programmerr47.vkdiscussionviewer");

        if (!VKSdk.isLoggedIn()) {
            VKSdk.login(this, VKScope.MESSAGES);
        }
    }

    @Override
    public void onBackPressed() {
        if (appFragment.hasBackStack()) {
            appFragment.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (!VKSdk.onActivityResult(requestCode, resultCode, data, new VKCallback<VKAccessToken>() {
            @Override
            public void onResult(VKAccessToken res) {
                appFragment.init();
            }

            @Override
            public void onError(VKError error) {
                if (error.errorCode == VKError.VK_CANCELED) {
                    finish();
                } else {
                    //TODO
                    throw new IllegalStateException("Have no behaviour for error: " + error);
                }
            }
        })) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
