package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by l.fernandez on 20/12/2016.
 */

public class MatchPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;

    public MatchPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }
    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return VolleyMatchFragment.newInstance();
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return EditLineUpFragment.newInstance("name a", "name b");
            case 2: // Fragment # 1 - This will show SecondFragment
                return CurrentLineUpFragment.newInstance(new ArrayList<String>(), new ArrayList<String>() ,"name a", "name b");
            default:
                return null;
        }
    }

}
