package com.github.programmerr47.vkdiscussionviewer.pager;

import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-07-31
 */

public abstract class UiControl {
    protected View view;
    protected final List<Runnable> uiWorks = new ArrayList<>();

    protected Toolbar toolbar;

    public View prepare(Context context) {
        view = onCreateView(context);
        onViewCreated(view);
        return view;
    }

    public void onCreate() {}

    public abstract View onCreateView(Context context);

    public void onViewCreated(View pageView) {}

    public void onResume() {
        executeAllWorks();
    }

    public void onPause() {}

    public void onDestroy() {}

    public View getView() {
        return view;
    }

    private void executeAllWorks() {
        if (uiWorks.size() > 0) {
            for (Runnable runnable : uiWorks) {
                runnable.run();
            }

            uiWorks.clear();
        }
    }
}
