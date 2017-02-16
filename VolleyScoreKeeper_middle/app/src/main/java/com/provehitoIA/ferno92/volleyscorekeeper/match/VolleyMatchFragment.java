package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatImageButton;
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
    String mNameA;
    String mNameB;
    private View mRootView;
    private Listener mListener;

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
        bindClickOnButtons();

        return this.mRootView;
    }

    private void bindClickOnButtons() {
        AppCompatImageButton leftUpButton = (AppCompatImageButton) mRootView.findViewById(R.id.arrow_up_left);
        AppCompatImageButton rightUpButton = (AppCompatImageButton) mRootView.findViewById(R.id.arrow_up_right);
        leftUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddOneForTeam(view);
            }
        });
        rightUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onAddOneForTeam(view);
            }
        });

        AppCompatImageButton leftDownButton = (AppCompatImageButton) mRootView.findViewById(R.id.arrow_down_left);
        AppCompatImageButton rightDownButton = (AppCompatImageButton) mRootView.findViewById(R.id.arrow_down_right);
        leftDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRemoveOneFromTeam(view);
            }
        });
        rightDownButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onRemoveOneFromTeam(view);
            }
        });

        AppCompatButton resetScoreButton = (AppCompatButton) mRootView.findViewById(R.id.restart_set_button);
        AppCompatButton saveGameButton = (AppCompatButton) mRootView.findViewById(R.id.save_game_button);
        resetScoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onResetScore();
            }
        });
        saveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onSaveMatch();
            }
        });
    }

    public static VolleyMatchFragment newInstance(String nameA, String nameB, Listener listener){
        VolleyMatchFragment volleyMatchFragment = new VolleyMatchFragment();

        volleyMatchFragment.mListener = listener;
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

    public interface Listener {
        void onAddOneForTeam(View view);

        void onRemoveOneFromTeam(View view);

        void onResetScore();

        void onSaveMatch();
    }

}
