package com.github.programmerr47.vkdiscussionviewer.chatpage;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.view.MarginLayoutParamsCompat;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.programmerr47.vkdiscussionviewer.R;
import com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils;
import com.github.programmerr47.vkdiscussionviewer.utils.Constants;
import com.github.programmerr47.vkdiscussionviewer.views.CustomFontTextView;

import static android.view.Gravity.CENTER;
import static android.view.Gravity.CENTER_VERTICAL;
import static android.view.Gravity.END;
import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.github.programmerr47.vkdiscussionviewer.VkApplication.appContext;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.color;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.dp;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.hasLollipop;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.sp;
import static com.github.programmerr47.vkdiscussionviewer.utils.Constants.Font.ROBOTO_REGULAR;

/**
 * @author Michael Spitsin
 * @since 2016-08-05
 */
public class ChatPageLayout extends LinearLayout {
    private static final Drawable CHAT_BG = appContext().getResources().getDrawable(R.drawable.chat_bg);
    private static final Drawable HEADER_SHADOW = appContext().getResources().getDrawable(R.drawable.header_shadow);
    private static final Drawable PRIMARY_BG = new ColorDrawable(color(R.color.colorPrimary));

    private Toolbar toolbar;
    private ImageView avatarView;
    private RecyclerView messageListView;
    private ProgressBar progressBar;
    private TextView emptyMessageView;

    public ChatPageLayout(Context context) {
        super(context);
        init();
    }

    public ChatPageLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChatPageLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ChatPageLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public ImageView getAvatarView() {
        return avatarView;
    }

    public RecyclerView getMessageListView() {
        return messageListView;
    }

    public ProgressBar getProgressBar() {
        return progressBar;
    }

    public TextView getEmptyMessageView() {
        return emptyMessageView;
    }

    private void init() {
        setOrientation(VERTICAL);
        FrameLayout toolbarContainer = setupToolbarPart();
        FrameLayout listContainer = setupListPart();

        addView(toolbarContainer, linearParams(MATCH_PARENT, WRAP_CONTENT));
        addView(listContainer, linearParams(MATCH_PARENT, MATCH_PARENT));
    }

    private FrameLayout setupToolbarPart() {
        FrameLayout toolbarContainer = new FrameLayout(getContext());
        toolbarContainer.setBackgroundDrawable(PRIMARY_BG);

        if (hasLollipop()) {
            toolbarContainer.setElevation(dp(4));
        }

        toolbar = initToolbar();
        avatarView = new ImageView(getContext());
        FrameLayout.LayoutParams avatarParams = new FrameLayout.LayoutParams((int)dp(42), (int)dp(42));

        avatarParams.gravity = CENTER_VERTICAL | END;
        MarginLayoutParamsCompat.setMarginEnd(avatarParams, (int)dp(8));

        toolbarContainer.addView(toolbar, frameParams(MATCH_PARENT, (int)dp(56)));
        toolbarContainer.addView(avatarView, avatarParams);

        return toolbarContainer;
    }

    private Toolbar initToolbar() {
        Toolbar toolbar = new Toolbar(getContext());
        toolbar.setBackgroundDrawable(PRIMARY_BG);
        toolbar.setTitleTextColor(color(R.color.text_primary_dark));
        toolbar.setPopupTheme(R.style.AppTheme_PopupOverlay);
        return toolbar;
    }

    private FrameLayout setupListPart() {
        FrameLayout listContainer = new FrameLayout(getContext());

        ImageView bg = initBackground();
        messageListView = initMessageViewList();
        emptyMessageView = initEmptyMessageView();
        progressBar = new ProgressBar(getContext());

        FrameLayout.LayoutParams wrapCenterParams = frameParams(WRAP_CONTENT, WRAP_CONTENT);
        wrapCenterParams.gravity = CENTER;

        listContainer.addView(bg, frameParams(MATCH_PARENT, MATCH_PARENT));
        listContainer.addView(messageListView, frameParams(MATCH_PARENT, MATCH_PARENT));
        listContainer.addView(emptyMessageView, wrapCenterParams);
        listContainer.addView(progressBar, wrapCenterParams);

        if (!hasLollipop()) {
            addCompatShadowForLowerVersions(listContainer);
        }

        return listContainer;
    }

    private ImageView initBackground() {
        ImageView imageView = new ImageView(getContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageDrawable(CHAT_BG);
        return imageView;
    }

    private RecyclerView initMessageViewList() {
        RecyclerView messageViewList = new RecyclerView(getContext());
        messageViewList.setVerticalScrollBarEnabled(true);
        return messageViewList;
    }

    private TextView initEmptyMessageView() {
        CustomFontTextView emptyMessageView = new CustomFontTextView(getContext());
        emptyMessageView.setVisibility(GONE);
        emptyMessageView.setTextSize(sp(14));
        emptyMessageView.setFont(ROBOTO_REGULAR);
        emptyMessageView.setTextColor(color(R.color.text_secondary_dark));
        emptyMessageView.setText(R.string.message_list_empty);
        return emptyMessageView;
    }

    private void addCompatShadowForLowerVersions(FrameLayout container) {
        View view = new View(getContext());
        view.setBackgroundDrawable(HEADER_SHADOW);

        container.addView(view, frameParams(MATCH_PARENT, (int)dp(8)));
    }

    private LinearLayout.LayoutParams linearParams(int width, int height) {
        return new LinearLayout.LayoutParams(width, height);
    }

    private FrameLayout.LayoutParams frameParams(int width, int height) {
        return new FrameLayout.LayoutParams(width, height);
    }
}
