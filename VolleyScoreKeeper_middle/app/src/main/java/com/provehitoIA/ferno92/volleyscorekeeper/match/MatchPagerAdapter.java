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

    public MatchPagerAdapter(FragmentManager fragmentManager, VolleyMatch activity) {
        super(fragmentManager);
        this.mMainActivity = activity;
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
                return VolleyMatchFragment.newInstance(
                        this.mMainActivity.getNameTeamA(),
                        this.mMainActivity.getNameTeamB()
                );
            case 1: // Fragment # 0 - This will show FirstFragment different title
                if(this.mIsEditingLineUp){

                    return EditLineUpFragment.newInstance(
                            this.mMainActivity.getNameTeamA(),
                            this.mMainActivity.getNameTeamB()
                    );
                }else{

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
    public Object instantiateItem(ViewGroup container, int position) {
        Fragment createdFragment = (Fragment) super.instantiateItem(container, position);
        // save the appropriate reference depending on position
        switch (position) {
            case 0:
                this.m1stFragment = (VolleyMatchFragment) createdFragment;
                break;
            case 1:
                if(this.mIsEditingLineUp) {
                    this.m2ndFragment = (EditLineUpFragment) createdFragment;
                }else{
                    this.m2ndFragment = (CurrentLineUpFragment) createdFragment;
                }
                break;
        }
        return createdFragment;
    }

    public Fragment getFirstFragment(){
        return this.m1stFragment;
    }

    public Fragment getSecondFragment(){
        return this.m2ndFragment;
    }

    public void setIsEditingLineUp(boolean isEditingLineUp){
        this.mIsEditingLineUp = isEditingLineUp;
    }
}
