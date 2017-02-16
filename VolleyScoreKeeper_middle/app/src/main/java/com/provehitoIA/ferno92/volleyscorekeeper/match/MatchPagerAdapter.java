package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by l.fernandez on 20/12/2016.
 */

public class MatchPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 2;
    private VolleyMatchFragment m1stFragment;
    private Fragment m2ndFragment;
    private VolleyMatch mMainActivity;
    private boolean mIsEditingLineUp = true;
    SecondPageListener listener = new SecondPageListener();
    private final FragmentManager mFragmentManager;

    public MatchPagerAdapter(FragmentManager fragmentManager, VolleyMatch activity, boolean isLineUpFilled) {
        super(fragmentManager);
        this.mFragmentManager = fragmentManager;
        this.mMainActivity = activity;
        this.mIsEditingLineUp = !isLineUpFilled;
    }

    // Returns total number of pages
    @Override
    public int getCount() {
        return NUM_ITEMS;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return VolleyMatchFragment.newInstance(
                        this.mMainActivity.getNameTeamA(),
                        this.mMainActivity.getNameTeamB(),
                        this.mMainActivity
                );
            case 1:
                if (this.mIsEditingLineUp) {

                    return EditLineUpFragment.newInstance(
                            this.mMainActivity.getNameTeamA(),
                            this.mMainActivity.getNameTeamB(), listener
                    );
                } else {

                    return CurrentLineUpFragment.newInstance(
                            this.mMainActivity.getNameTeamA(),
                            this.mMainActivity.getNameTeamB(),
                            this.mMainActivity.getLineUpA(),
                            this.mMainActivity.getLineUpB()
                    );
                }
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object)
    {
        if (object instanceof EditLineUpFragment &&
                m2ndFragment instanceof CurrentLineUpFragment) {
            return POSITION_NONE;
        }
        if (object instanceof CurrentLineUpFragment &&
                m2ndFragment instanceof EditLineUpFragment) {
            return POSITION_NONE;
        }
        return POSITION_UNCHANGED;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                this.m1stFragment = (VolleyMatchFragment) createdFragment;
                break;
            case 1:
                if (this.mIsEditingLineUp) {
                    this.m2ndFragment = (EditLineUpFragment) createdFragment;
                } else {
                    this.m2ndFragment = (CurrentLineUpFragment) createdFragment;
                }
                break;
        }
        return createdFragment;
    }

    public Fragment getFirstFragment() {
        return this.m1stFragment;
    }

    public Fragment getSecondFragment() {
        return this.m2ndFragment;
    }

    public void setIsEditingLineUp(boolean isEditingLineUp) {
        this.mIsEditingLineUp = isEditingLineUp;
    }

    public boolean getIsEditingLineUp() {
        return this.mIsEditingLineUp;
    }

    private final class SecondPageListener implements
            SecondPageFragmentListener {
        public void onSwitchToNextFragment() {
            mFragmentManager.beginTransaction().remove(m2ndFragment)
                    .commitNow();
            m2ndFragment = new CurrentLineUpFragment();

            notifyDataSetChanged();
        }
    }
}
