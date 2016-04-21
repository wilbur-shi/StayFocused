package com.mdb.wyn.stayfocused;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

/**
 * Created by Wilbur on 4/6/2016.
 */
public class MyPagerAdapter extends FragmentPagerAdapter {
    private int NUM_TABS = 3;
    private Fragment currentFragment;

    public MyPagerAdapter(FragmentManager fm, int tabs) {
        super(fm);
        NUM_TABS = tabs;
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        if (getCurrentFragment() != object) {
            currentFragment = (Fragment) object;
        }
        super.setPrimaryItem(container, position, object);
    }

    @Override
    public Fragment getItem(int pos) {
        switch (pos) {
            case 0: return new TimerFragment();
            case 1: return new AlarmFragment();
            case 2: return new SettingsFragment();
            default: return new TimerFragment();
        }
    }

    @Override
    public int getCount() {
        return NUM_TABS;
    }

}
