package com.github.programmerr47.vkdiscussionviewer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.github.programmerr47.vkdiscussionviewer.chatpage.ChatPage;
import com.github.programmerr47.vkdiscussionviewer.pager.FixedSpeedScroller;
import com.github.programmerr47.vkdiscussionviewer.pager.Page;
import com.github.programmerr47.vkdiscussionviewer.pager.PagerListener;
import com.github.programmerr47.vkdiscussionviewer.pager.VkPageTransformerAuto;
import com.github.programmerr47.vkdiscussionviewer.pager.VkPagerTransformerManual;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-07-31.
 */
public class AppFragment extends Fragment implements PagerListener, ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private VkPageAdapter adapter;
    private List<Page> pages = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);

        adapter = new VkPageAdapter(pages);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_app, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        pager = (ViewPager) view.findViewById(R.id.app_pager);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        for (Page page : pages) {
            page.prepare(getActivity());
        }

        pager.setOverScrollMode(View.OVER_SCROLL_NEVER);
        setManualTransformer();
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        pages.get(pages.size() - 1).onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        pages.get(pages.size() - 1).onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        for (Page page : pages) {
            page.onDestroy();
        }
    }

    @Override
    public void openPage(Page newPage) {
        newPage.onCreate();
        newPage.setPagerListener(this);
        newPage.prepare(getActivity());
        adapter.addPage(newPage);

        newPage.setTransitionAnimating(true);
        setCurrentPage(pages.size() - 1, true);
    }

    @Override
    public void closePage() {
        setCurrentPage(pages.size() - 2, true);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {
        if (state == ViewPager.SCROLL_STATE_IDLE) {
            setManualTransformer();

            if (pager.getCurrentItem() < pages.size() - 1) {
                getLastPage().onPause();
                getLastPage().onDestroy();
                adapter.removeLast();
            } else {
                getLastPage().setTransitionAnimating(false);
                getLastPage().onResume();
            }
        } else if (state == ViewPager.SCROLL_STATE_DRAGGING && getLastPage().isTransitionAnimating()) {
            getLastPage().setTransitionAnimating(false);
            getLastPage().onResume();
        }
    }

    public void init(Page rootPage) {
        openPage(rootPage);
    }

    public boolean hasBackStack() {
        return pages.size() > 1 || pages.get(0).hasBackStack();
    }

    public void onBackPressed() {
        if (getLastPage().hasBackStack()) {
            getLastPage().onBackPressed();
        } else {
            setCurrentPage(pages.size() - 2, true);
        }
    }

    private void setCustomScroller(Interpolator interpolator) {
        try {
            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(pager.getContext(), interpolator);
            mScroller.set(pager, scroller);
        } catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException e) {
            //ignore
        }
    }

    private Page getLastPage() {
        return pages.get(pages.size() - 1);
    }

    private void setManualTransformer() {
        pager.setPageTransformer(false, new VkPagerTransformerManual());
        setCustomScroller(null);
    }

    private void setCurrentPage(int item, boolean smoothScroll) {
        if (pager.getCurrentItem() > item) {
            setCustomScroller(new AccelerateInterpolator(3f));
        } else if (pager.getCurrentItem() < item) {
            setCustomScroller(new DecelerateInterpolator(3f));
        } else {
            setCustomScroller(null);
        }

        pager.setPageTransformer(false, new VkPageTransformerAuto());
        pager.setCurrentItem(item, smoothScroll);
    }

    public static final class VkPageAdapter extends PagerAdapter {

        private List<Page> pages;

        public VkPageAdapter(List<Page> pages) {
            this.pages = pages;
        }

        @Override
        public Object instantiateItem(ViewGroup collection, int position){
            View v = pages.get(position).getView();
            collection.addView(v, 0);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup collection, int position, Object view){
            collection.removeView((View) view);
        }

        @Override
        public int getItemPosition(Object object){
            return PagerAdapter.POSITION_NONE;
        }

        @Override
        public int getCount(){
            return pages.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object){
            return view.equals(object);
        }

        public Page getPage(int position) {
            return pages.get(position);
        }

        public void addPage(Page page) {
            pages.add(page);
            notifyDataSetChanged();
        }

        public void removePage(int position) {
            pages.remove(position);
            notifyDataSetChanged();
        }

        public void removeLast() {
            removePage(pages.size() - 1);
        }
    }
}
