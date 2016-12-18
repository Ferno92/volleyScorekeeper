package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.provehitoIA.ferno92.volleyscorekeeper.OnSwipeTouchListener;
import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 04/12/2016.
 */

public class CurrentLineUpActivity extends AppCompatActivity {
    ArrayList<String> lineUpA = new ArrayList<String>();
    ArrayList<String> lineUpB = new ArrayList<String>();
    String teamAName;
    String teamBName;
    Boolean isLineUpEmpty = true;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_line_up);
        teamAName = getIntent().getExtras().getString("teamA");
        teamBName = getIntent().getExtras().getString("teamB");
        // Get Intent LineUp
        if(getIntent().getExtras().getStringArrayList("lineUpA") != null){
            isLineUpEmpty = false;
            lineUpA = getIntent().getExtras().getStringArrayList("lineUpA");
            lineUpB = getIntent().getExtras().getStringArrayList("lineUpB");
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
                slideactivity.putExtra("teamA", "teamAName");
                slideactivity.putExtra("teamB", "teamBName");
                startActivity(slideactivity);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
    }
}
