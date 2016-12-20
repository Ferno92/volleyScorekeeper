package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 08/12/2016.
 */

public class VolleyMatchFragment extends Fragment {
    public VolleyMatchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        View rootView = inflater.inflate(R.layout.volley_match,
                container, false);

        return rootView;
    }
    public static VolleyMatchFragment newInstance(){
        VolleyMatchFragment volleyMatchFragment = new VolleyMatchFragment();

        return volleyMatchFragment;
    }

}
