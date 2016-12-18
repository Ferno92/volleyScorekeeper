package com.provehitoIA.ferno92.volleyscorekeeper.matchInfos;

import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.GameListInfo;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.GameListInfoFragment;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.MainActivity;
import com.provehitoIA.ferno92.volleyscorekeeper.match.VolleyMatch;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;

/**
 * Created by lucas on 31/10/2016.
 */

public class MatchInfo extends AppCompatActivity {
    ArrayList<String> lineUpA = new ArrayList<String>();
    ArrayList<String> lineUpB = new ArrayList<String>();
    EditText teamAEditText;
    EditText teamBEditText;
    ImageView missingAName;
    ImageView missingBName;

    String teamAName;
    String teamBName;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.team_infos);
        if(getIntent().getExtras() != null){
            setTeamName();
            lineUpA = getIntent().getExtras().getStringArrayList("lineUpA");
            lineUpB = getIntent().getExtras().getStringArrayList("lineUpB");
            if (lineUpA.size() == 6 && lineUpB.size() == 6) {
                // show different court image maybe with some effects
                ImageView emptyCourt = (ImageView) findViewById(R.id.empty_court);
                emptyCourt.setImageResource(R.drawable.volleyball_court_done);
            }
        }
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if(h > 600 && w > 600) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

            LinearLayout editLineupButtonWrapper = (LinearLayout) findViewById(R.id.lineup_button_wrapper);
            ImageView emptyCourtImage = (ImageView) findViewById(R.id.empty_court);
            emptyCourtImage.setVisibility(View.GONE);
            editLineupButtonWrapper.setVisibility(View.GONE);

            ImageView courtWithNumberImage = (ImageView) findViewById(R.id.court_with_number_info);
            courtWithNumberImage.setVisibility(View.VISIBLE);
            ImageView courtWithNumber = (ImageView) findViewById(R.id.court_with_number);
            courtWithNumber.setVisibility(View.GONE);
            Button editedLineupButton = (Button) findViewById(R.id.lineup_edited_button);
            editedLineupButton.setVisibility(View.GONE);

        }else{
            //Portrait (in all device) or landscape in mobile only
        }

    }

    public void editLineUp(View v){
        saveTeamsName();
        if(teamAEditText.getText().toString().isEmpty() == false && teamBEditText.getText().toString().isEmpty() == false){
            Intent intent = new Intent(MatchInfo.this, EditLineUp.class);
            intent.putExtra("teamA", teamAEditText.getText().toString());
            intent.putExtra("teamB", teamBEditText.getText().toString());
            intent.putExtra("lineUpA", lineUpA);
            intent.putExtra("lineUpB", lineUpB);
            startActivity(intent);
        }else{
            showErrors();
        }
    }
    public void startNewMatch(View v){
        saveTeamsName();

        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if(h < w && (h > 600 || w > 600) ) {
            getLineUp();
        }

        if(teamAEditText.getText().toString().isEmpty() == false && teamBEditText.getText().toString().isEmpty() == false){

            // Alert if no lineUp
            if (lineUpA.size() < 6 || lineUpB.size() < 6) {
                AlertDialog.Builder alertLineUp = new AlertDialog.Builder(this);
                alertLineUp.setTitle("Line Up");
                alertLineUp.setMessage("You haven't set the team's line-up. " +
                        "You won't be able to track your team's positions until you set them. " +
                        "Do you still want to continue and start the match?");

                alertLineUp.setCancelable(false);
                alertLineUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MatchInfo.this, VolleyMatch.class);
                        intent.putExtra("teamA", teamAEditText.getText().toString());
                        intent.putExtra("teamB", teamBEditText.getText().toString());
                        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });

                alertLineUp.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                AlertDialog alert = alertLineUp.create();
                alert.show();
            }else{
                Intent intent = new Intent(MatchInfo.this, VolleyMatch.class);
                intent.putExtra("teamA", teamAEditText.getText().toString());
                intent.putExtra("teamB", teamBEditText.getText().toString());
                intent.putExtra("lineUpA", lineUpA);
                intent.putExtra("lineUpB", lineUpB);
                intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }

        }else{
            //Show error
            showErrors();
        }
    }

    public void saveTeamsName(){
        teamAEditText = (EditText) findViewById(R.id.edit_team_a);
        teamBEditText = (EditText) findViewById(R.id.edit_team_b);
    }

    public void showErrors(){
        missingAName = (ImageView) findViewById(R.id.missing_a_name);
        missingBName = (ImageView) findViewById(R.id.missing_b_name);
        missingAName.setVisibility(View.INVISIBLE);
        missingBName.setVisibility(View.INVISIBLE);

        if(teamAEditText.getText().toString().isEmpty()){
            missingAName.setVisibility(View.VISIBLE);
        }
        if(teamBEditText.getText().toString().isEmpty()){
            missingBName.setVisibility(View.VISIBLE);
        }
        Context context = getApplicationContext();
        CharSequence text = "Missing team name";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    public void setTeamName(){
        teamAName = getIntent().getExtras().getString("teamA");
        teamBName = getIntent().getExtras().getString("teamB");

        EditText teamANameEdit = (EditText) findViewById(R.id.edit_team_a);
        teamANameEdit.setText(teamAName, TextView.BufferType.EDITABLE);
        EditText teamBNameEdit = (EditText) findViewById(R.id.edit_team_b);
        teamBNameEdit.setText(teamBName, TextView.BufferType.EDITABLE);
    }

    public int getDeviceHeight(){
        //Get height of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.heightPixels / density);
    }
    public int getDeviceWidth(){
        //Get width of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.widthPixels / density);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if(h > w && (h > 600 || w > 600)) {
            getLineUp();
        }
        outState.putStringArrayList("lineUpA", lineUpA);
        outState.putStringArrayList("lineUpB", lineUpB);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        lineUpA = savedInstanceState.getStringArrayList("lineUpA");
        lineUpB = savedInstanceState.getStringArrayList("lineUpB");
        int w = getDeviceWidth();
        int h = getDeviceHeight();

        if(h < w && (h > 600 || w > 600)) {
            setPreviousLineUp();
        }

    }
    public void setPreviousLineUp(){

        if(lineUpA.size() != 0 || lineUpB.size() != 0){
            LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
            for( int i = 0; i < teamA.getChildCount(); i++ ) {
                if (teamA.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if(linearLayoutChild.getChildAt(j) instanceof  EditText){
                            EditText teamAElement = (EditText) linearLayoutChild.getChildAt(j);
                            teamAElement.setText(lineUpA.get(i - 1), TextView.BufferType.EDITABLE);
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
                            teamBElement.setText(lineUpB.get(i - 1), TextView.BufferType.EDITABLE);
                        }
                    }
                }
            }
        }
    }

    public void getLineUp(){
        lineUpA.clear();
        lineUpB.clear();
        LinearLayout teamA = (LinearLayout) findViewById(R.id.team_a);
        if(teamA != null) {
            for (int i = 0; i < teamA.getChildCount(); i++) {
                if (teamA.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                                lineUpA.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                            }
                        }
                    }
                }
            }

            LinearLayout teamB = (LinearLayout) findViewById(R.id.team_b);
            for (int i = 0; i < teamB.getChildCount(); i++) {
                if (teamB.getChildAt(i) instanceof LinearLayout) {
                    LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                    for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                        if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                            if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                                lineUpB.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                            }
                        }
                    }
                }
            }
        }
    }
}
