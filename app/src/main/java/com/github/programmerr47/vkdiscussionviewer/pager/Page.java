package com.github.programmerr47.vkdiscussionviewer.pager;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;

import com.github.programmerr47.vkdiscussionviewer.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static com.github.programmerr47.vkdiscussionviewer.utils.AndroidUtils.color;

/**
 * @author Michael Spitsin
 * @since 2/10/2016.
 */
public abstract class Page extends UiControl {
    protected PagerListener pagerListener;

    protected boolean isTransitionAnimating;
    protected final ClosePageListener closePageListener = new ClosePageListener();

    public View prepare(Context context) {
        View originPageView = onCreateView(context);
        view = wrapPageView(context, originPageView);
        onViewCreated(originPageView);
        return view;
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

    protected final class ClosePageListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            pagerListener.closePage();
        }
    }
}
