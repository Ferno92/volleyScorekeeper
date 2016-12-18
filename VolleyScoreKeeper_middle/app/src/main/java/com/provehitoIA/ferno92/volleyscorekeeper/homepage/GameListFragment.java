package com.provehitoIA.ferno92.volleyscorekeeper.homepage;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by lucas on 20/11/2016.
 */

public class GameListFragment extends Fragment {
    public GameListFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(com.provehitoIA.ferno92.volleyscorekeeper.R.layout.fragment_game_list,
                container, false);
        return rootView;
    }

}
