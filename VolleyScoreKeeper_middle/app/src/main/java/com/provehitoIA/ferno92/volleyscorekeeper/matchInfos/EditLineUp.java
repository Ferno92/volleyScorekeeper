package com.provehitoIA.ferno92.volleyscorekeeper.matchInfos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 31/10/2016.
 */

public class EditLineUp extends AppCompatActivity {
    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();
    ScrollView mScrollView;
    EditText mFocusEdit;

    String mTeamAName;
    String mTeamBName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_line_up);
        mScrollView = (ScrollView) findViewById(R.id.line_up_scroll);
        mFocusEdit = (EditText) findViewById(R.id.focus_edit);
        focusOnView();
        setTitleLineUp();
        setPreviousLineUp();
    }

    public void backToMatchInfo(View v){
        Intent intent = new Intent(EditLineUp.this, MatchInfo.class);
        intent.putExtra("teamA", mTeamAName);
        intent.putExtra("teamB", mTeamBName);
        intent.putExtra("mLineUpA", mLineUpA);
        intent.putExtra("mLineUpB", mLineUpB);
        startActivity(intent);
    }

    public void saveLineUp(View v){
        mLineUpA = new ArrayList<String>();
        mLineUpB = new ArrayList<String>();
        LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
        for( int i = 0; i < teamA.getChildCount(); i++ ) {
            if (teamA.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if(linearLayoutChild.getChildAt(j) instanceof  EditText){
                        if(((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false){
                            mLineUpA.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                        }
                    }
                }
            }
        }

        LinearLayout teamB = (LinearLayout) findViewById(R.id.team_b);
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

        if(mLineUpA.size() == 6 && mLineUpB.size() == 6){
            // Back to match info
            backToMatchInfo(v);
        }else{
            Context context = getApplicationContext();
            CharSequence text = "Missing team element number! Please, check your inserted number again.";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        }
    }


    public void setTitleLineUp(){
        mTeamAName = getIntent().getExtras().getString("teamA");
        mTeamBName = getIntent().getExtras().getString("teamB");

        TextView titleLineUpA = (TextView) findViewById(R.id.line_up_titleA);
        titleLineUpA.setText("Line-Up " + mTeamAName);
        TextView titleLineUpB = (TextView) findViewById(R.id.line_up_titleB);
        titleLineUpB.setText("Line-Up " + mTeamBName);
    }

    public void setPreviousLineUp(){
        mLineUpA = getIntent().getExtras().getStringArrayList("mLineUpA");
        mLineUpB = getIntent().getExtras().getStringArrayList("mLineUpB");

        if(mLineUpA.size() == 6 && mLineUpB.size()== 6){
            LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
            for( int i = 0; i < teamA.getChildCount(); i++ ) {
                if (teamA.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if(linearLayoutChild.getChildAt(j) instanceof  EditText){
                            EditText teamAElement = (EditText) linearLayoutChild.getChildAt(j);
                            teamAElement.setText(mLineUpA.get(i - 1), TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }

            LinearLayout teamB = (LinearLayout) findViewById(R.id.team_b);
            for( int i = 0; i < teamB.getChildCount(); i++ ) {
                if (teamB.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if(linearLayoutChild.getChildAt(j) instanceof  EditText) {
                            EditText teamBElement = (EditText) linearLayoutChild.getChildAt(j);
                            teamBElement.setText(mLineUpB.get(i - 1), TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        }
    }

    private final void focusOnView(){
        mScrollView.post(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, mFocusEdit.getBottom());
            }
        });
    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//
//
//        // Checks whether a hardware keyboard is available
//        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
//            Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
//        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
//            Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
//        }
//    }

}
