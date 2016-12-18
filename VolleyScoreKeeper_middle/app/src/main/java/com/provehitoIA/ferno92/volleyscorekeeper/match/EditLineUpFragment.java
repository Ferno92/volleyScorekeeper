package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 08/12/2016.
 */

public class EditLineUpFragment extends Fragment {
    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();
    String mNameA;
    String mNameB;
    View mRootView;
    public EditLineUpFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        mRootView = inflater.inflate(R.layout.edit_line_up,
                container, false);
        Button lineUpEditedButton = (Button) mRootView.findViewById(R.id.lineup_edited_button);
        lineUpEditedButton.setVisibility(View.GONE);

        mNameA = getArguments().getString("nameA");
        mNameB = getArguments().getString("nameB");
        TextView nameATextView = (TextView) mRootView.findViewById(R.id.line_up_titleA);
        TextView nameBTextView = (TextView) mRootView.findViewById(R.id.line_up_titleB);
        nameATextView.setText(mNameA);
        nameBTextView.setText(mNameB);
        return mRootView;
    }

    public void getLineUp(){
        mLineUpA = new ArrayList<String>();
        mLineUpB = new ArrayList<String>();
        LinearLayout teamA = (LinearLayout) mRootView.findViewById(R.id.team_a);
        for( int i = 0; i < teamA.getChildCount(); i++ ) {
            if (teamA.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if(linearLayoutChild.getChildAt(j) instanceof EditText){
                        if(((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false){
                            mLineUpA.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                        }
                    }
                }
            }
        }

        LinearLayout teamB = (LinearLayout) mRootView.findViewById(R.id.team_b);
        for( int i = 0; i < teamB.getChildCount(); i++ ) {
            if (teamB.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if(linearLayoutChild.getChildAt(j) instanceof  EditText) {
                        if(((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false){
                            mLineUpB.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                        }
                    }
                }
            }
        }

    }

    public ArrayList<String> getCurrentLineUpA(){
        return mLineUpA;
    }
    public ArrayList<String> getCurrentLineUpB(){
        return mLineUpB;
    }
}