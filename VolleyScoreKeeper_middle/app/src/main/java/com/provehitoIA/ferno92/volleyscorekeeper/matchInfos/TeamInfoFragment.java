package com.provehitoIA.ferno92.volleyscorekeeper.matchInfos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

/**
 * Created by lucas on 27/11/2016.
 */

public class TeamInfoFragment extends Fragment {
    private MatchInfo mMatchInfo;
    private View mRootView;
    public TeamInfoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        mRootView = inflater.inflate(R.layout.match_info,
                container, false);
        mMatchInfo = ((MatchInfo) getActivity());

        bindOnClick();
        return mRootView;
    }

    private void bindOnClick() {
        AppCompatButton getGymButton = (AppCompatButton) mRootView.findViewById(R.id.get_gym_position);
        getGymButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMatchInfo.getLocation(view);
            }
        });
        AppCompatButton startMatchButton = (AppCompatButton) mRootView.findViewById(R.id.start_new_match);
        startMatchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mMatchInfo.startNewMatch(view);
            }
        });
    }


}
