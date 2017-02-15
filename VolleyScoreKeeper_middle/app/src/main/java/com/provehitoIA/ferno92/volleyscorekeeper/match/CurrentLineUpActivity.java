package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.provehitoIA.ferno92.volleyscorekeeper.OnSwipeTouchListener;
import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 04/12/2016.
 */

public class CurrentLineUpActivity extends AppCompatActivity {
    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();
    String mTeamAName;
    String mTeamBName;
    Boolean isLineUpEmpty = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_line_up);
        mTeamAName = getIntent().getExtras().getString("teamA");
        mTeamBName = getIntent().getExtras().getString("teamB");
        // Get Intent LineUp
        if(getIntent().getExtras().getStringArrayList("lineUpA") != null){
            isLineUpEmpty = false;
            mLineUpA = getIntent().getExtras().getStringArrayList("lineUpA");
            mLineUpB = getIntent().getExtras().getStringArrayList("mLineUpB");
        }

        //Swipe back
        LinearLayout mainView = (LinearLayout) findViewById(R.id.edit_lineup_main);
        mainView.setOnTouchListener(new OnSwipeTouchListener(this) {
            @Override
            public void onSwipeRight() {
                // Whatever
                Toast.makeText(getApplicationContext(), "Swiping..",
                        Toast.LENGTH_SHORT).show();
                Intent slideactivity = new Intent(CurrentLineUpActivity.this, VolleyMatch.class);
                slideactivity.putExtra("teamA", "mTeamAName");
                slideactivity.putExtra("teamB", "mTeamBName");
                startActivity(slideactivity);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}
