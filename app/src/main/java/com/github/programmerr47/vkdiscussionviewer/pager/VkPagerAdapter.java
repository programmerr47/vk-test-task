package com.github.programmerr47.vkdiscussionviewer.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.github.programmerr47.vkgroups.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Michael Spitsin
 * @since 2016-01-09
 */
public class VkPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;
    private FragmentManager fm;

    public VkPagerAdapter(FragmentManager fm, Fragment rootFragment) {
        super(fm);
        this.fm = fm;
        fragmentList = new ArrayList<>();
        fragmentList.add(rootFragment);
    }

    public VkPagerAdapter(FragmentManager fm, List<Fragment> savedList) {
        super(fm);
        this.fm = fm;
        fragmentList = savedList;
    }

    @Override
    public Fragment getItem(int i) {
        return fragmentList.get(i);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public void removeLast() {
        int listSize = fragmentList.size();

        fm.beginTransaction().remove(fm.findFragmentByTag(getFragmentTag(listSize - 1))).commit();

        if (listSize > 0) {
            fragmentList.remove(listSize - 1);
        }
        notifyDataSetChanged();
    }

    public void addFragment(Fragment fragment) {
        fragmentList.add(fragment);
        notifyDataSetChanged();
    }

    public List<Fragment> getFragmentList() {
        return fragmentList;
    }

    private String getFragmentTag(int pos){
        return "android:fragment_pager:"+ R.id.main_pager+":"+pos;
    }
}
