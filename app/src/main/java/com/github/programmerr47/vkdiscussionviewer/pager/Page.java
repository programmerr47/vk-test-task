package com.github.programmerr47.vkdiscussionviewer.pager;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.github.programmerr47.vkdiscussionviewer.R;

import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.github.programmerr47.vkdiscussionviewer.AndroidUtils.color;

/**
 * @author Michael Spitsin
 * @since 2/10/2016.
 */
public abstract class Page {

    public static final int VEIL_ID = 1;

    private View pageView;
    protected PagerListener pagerListener;

    protected boolean isTransitionAnimating;
    protected final List<Runnable> uiWorks = new ArrayList<>();
    protected final UpClickListener closePageListener = new UpClickListener();

    protected Toolbar toolbar;

    public View createView(Context context) {
        View originPageView = onCreateView(context);
        pageView = wrapPageView(context, originPageView);
        onViewCreated(originPageView);
        return pageView;
    }

    public void onCreate() {}

    public abstract View onCreateView(Context context);

    public void onViewCreated(View pageView) {}

    public void onResume() {
        executeAllWorks();
    }

    public void onPause() {}

    public void onDestroy() {}

    public View getPageView() {
        return pageView;
    }

    public void setPagerListener(PagerListener pagerListener) {
        this.pagerListener = pagerListener;
    }

    public boolean isTransitionAnimating() {
        return isTransitionAnimating;
    }

    public void setTransitionAnimating(boolean isTransitionAnimating) {
        this.isTransitionAnimating = isTransitionAnimating;
    }

    public boolean hasBackStack() {
        return false;
    }

    public void onBackPressed() {

    }

    private View wrapPageView(Context context, View pageView) {
        LayoutParams params = new LayoutParams(MATCH_PARENT, MATCH_PARENT);
        FrameLayout wrapperLayout = new FrameLayout(context);
        wrapperLayout.setLayoutParams(params);

        View blackVeilView = new View(context);
        blackVeilView.setId(R.id.veil);
        blackVeilView.setBackgroundColor(color(R.color.vk_black));
        blackVeilView.setAlpha(0.0f);

        wrapperLayout.addView(pageView);
        wrapperLayout.addView(blackVeilView);

        return wrapperLayout;
    }

    private void executeAllWorks() {
        if (uiWorks.size() > 0) {
            for (Runnable runnable : uiWorks) {
                runnable.run();
            }

            uiWorks.clear();
        }
    }

    protected final class UpClickListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            pagerListener.closePage();
        }
    }
}
