package org.forzaverita.iverbs.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Map;

public class CommonPagerAdapter extends FragmentPagerAdapter {

    private final Map<Integer, TitledFragment> fragments;

    public CommonPagerAdapter(FragmentManager fm, Map<Integer, TitledFragment> fragments) {
        super(fm);
        this.fragments = fragments;
    }

    @Override
    public Fragment getItem(int item) {
        return fragments.get(item);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).getTitle();
    }

}
