package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 08/12/2016.
 */

public class VolleyMatchFragment extends Fragment {
    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();
    String mNameA;
    String mNameB;
    private View mRootView;
    public VolleyMatchFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        this.mRootView = inflater.inflate(R.layout.volley_match,
                container, false);

        this.mNameA = getArguments().getString("nameA");
        this.mNameB = getArguments().getString("nameB");
        displayNameTeamA();
        displayNameTeamB();

        return this.mRootView;
    }
    public static VolleyMatchFragment newInstance(String nameA, String nameB){
        VolleyMatchFragment volleyMatchFragment = new VolleyMatchFragment();

        Bundle args = new Bundle();
        args.putString("nameA", nameA);
        args.putString("nameB", nameB);
        volleyMatchFragment.setArguments(args);

        return volleyMatchFragment;
    }

    /**
     * Displays the name for Team A.
     */
    public void displayNameTeamA() {
        TextView scoreView = (TextView) this.mRootView.findViewById(R.id.team_a_name);
        scoreView.setText(this.mNameA);
    }

    /**
     * Displays the name for Team B.
     */
    public void displayNameTeamB() {
        TextView scoreView = (TextView) this.mRootView.findViewById(R.id.team_b_name);
        scoreView.setText(this.mNameB);
    }

}
