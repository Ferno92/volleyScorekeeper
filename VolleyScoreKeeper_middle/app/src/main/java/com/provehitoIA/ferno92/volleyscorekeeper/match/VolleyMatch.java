package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.PlusShare;
import com.pixelcan.inkpageindicator.InkPageIndicator;
import com.provehitoIA.ferno92.volleyscorekeeper.OnSwipeTouchListener;
import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchDbHelper;
import com.provehitoIA.ferno92.volleyscorekeeper.homepage.MainActivity;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static android.support.v4.view.ViewPager.SCROLL_STATE_DRAGGING;
import static android.support.v4.view.ViewPager.SCROLL_STATE_IDLE;
import static android.support.v4.view.ViewPager.SCROLL_STATE_SETTLING;

/**
 * Created by lucas on 23/10/2016.
 */

public class VolleyMatch extends AppCompatActivity implements FragmentManager.OnBackStackChangedListener {

    int scoreTeamA = 0;
    int scoreTeamB = 0;
    int scoreSetTeamA = 0;
    int scoreSetTeamB = 0;
    boolean setEnded = false;
    boolean matchEnded = false;
    String teamAName;
    String teamBName;
    String totalResult[] = new String[5];
    int hasBall = -1;
    int hasBallPrevious = -1;
    int mCurrentFragment = -1;
    byte[] mImageA;
    byte[] mImageB;

    ArrayList<String> lineUpA = new ArrayList<String>();
    ArrayList<String> lineUpB = new ArrayList<String>();
    ArrayList<String> mSavedLineUpA = new ArrayList<String>();
    ArrayList<String> mSavedLineUpB = new ArrayList<String>();
    Fragment mainFragment;
    FragmentManager mFragmentManager;

    CallbackManager callbackManager;
    ShareDialog shareDialog;
    Boolean mOnEditLineUp = false;

    MatchPagerAdapter adapterViewPager;
    ViewPager vpPager;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.volley_match_container);
        setDataFromIntent();
        // Set orientation
        int h = getDeviceHeight();
        int w = getDeviceWidth();
        if (h > 600 && w > 600) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        //Set view
        mFragmentManager = getSupportFragmentManager();
        vpPager = (ViewPager) findViewById(R.id.vpPager);
        adapterViewPager = new MatchPagerAdapter(mFragmentManager, this, (lineUpA.size() == 6 && lineUpB.size() == 6));
        vpPager.setAdapter(adapterViewPager);
        // Attach the page change listener inside the activity
        vpPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            // This method will be invoked when a new page becomes selected.
            @Override
            public void onPageSelected(int position) {
                //                Toast.makeText(VolleyMatch.this,
                //                        "Selected page position: " + position, Toast.LENGTH_SHORT).show();
                mCurrentFragment = position;
                if(position == 0){
                    setTitle("Play Match");
                }else if(adapterViewPager.getIsEditingLineUp()){
                    setTitle("Edit Line Up");
                }else{
                    setTitle("Current Line Up");
                }
            }

            // This method will be invoked when the current page is scrolled
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                // Code goes here
            }

            // Called when the scroll state changes:
            // SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING
            @Override
            public void onPageScrollStateChanged(int state) {
                // Code goes here
                String message = state == SCROLL_STATE_DRAGGING ? "Start dragging" : (state == SCROLL_STATE_SETTLING ?
                        "Settling to final position" : (state == SCROLL_STATE_IDLE ? "Stopped dragging" : "wtf"));
//                Toast.makeText(VolleyMatch.this, message, Toast.LENGTH_SHORT).show();
                if (state == SCROLL_STATE_DRAGGING) {
                    if (adapterViewPager.getSecondFragment() instanceof CurrentLineUpFragment && mCurrentFragment == 0) {
                        CurrentLineUpFragment currentLineUpFragment = ((CurrentLineUpFragment) adapterViewPager.getSecondFragment());
                        currentLineUpFragment.setCurrentLineUp(lineUpA, lineUpB);
                        currentLineUpFragment.setLineUpView();
                    }
                }
                if (state == SCROLL_STATE_IDLE) {
                    if (adapterViewPager.getSecondFragment() instanceof EditLineUpFragment && mCurrentFragment == 0) {
                        EditLineUpFragment editLineUpFragment = ((EditLineUpFragment) adapterViewPager.getSecondFragment());
                        editLineUpFragment.getLineUp();
                        lineUpA = editLineUpFragment.getCurrentLineUpA();
                        lineUpB = editLineUpFragment.getCurrentLineUpB();
                        if (lineUpA.size() != 6 && lineUpB.size() != 6) {
                            lineUpA.clear();
                            lineUpB.clear();
                        } else {
//                            if(!editLineUpFragment.checkLineUpRepeated()) {
                                adapterViewPager.setIsEditingLineUp(false);
                                editLineUpFragment.getSecondPageListener().onSwitchToNextFragment();
//                            }
                        }
                    }
                }
            }
        });

        InkPageIndicator inkPageIndicator = (InkPageIndicator) findViewById(R.id.indicator);
        inkPageIndicator.setViewPager(vpPager);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void setDataFromIntent() {
        this.teamAName = getIntent().getExtras().getString("teamA");
        this.teamBName = getIntent().getExtras().getString("teamB");
        this.mImageA = getIntent().getExtras().getByteArray("logoA");
        this.mImageB = getIntent().getExtras().getByteArray("logoB");
        // Set lineup if not empty
        setLineUp();
    }

    private void setLineUp() {
        if (getIntent().getExtras().getStringArrayList("lineUpA") != null) {
            lineUpA = getIntent().getExtras().getStringArrayList("lineUpA");
            lineUpB = getIntent().getExtras().getStringArrayList("lineUpB");
            mSavedLineUpA = new ArrayList<>(lineUpA);
            mSavedLineUpB = new ArrayList<>(lineUpB);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public String getNameTeamA() {
        return this.teamAName;
    }

    public String getNameTeamB() {
        return this.teamBName;
    }

    public ArrayList<String> getLineUpA() {
        return this.lineUpA;
    }

    public ArrayList<String> getLineUpB() {
        return this.lineUpB;
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamA(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_a_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given score for Team A.
     */
    public void displayForTeamB(int score) {
        TextView scoreView = (TextView) findViewById(R.id.team_b_score);
        scoreView.setText(String.valueOf(score));
    }

    /**
     * Displays the given set score for Team A.
     */
    public void displaySetForTeamA(int score, boolean setOver) {
        TextView scoreSetViewA = (TextView) findViewById(R.id.team_a_set);
        scoreSetViewA.setText(String.valueOf(scoreSetTeamA));
        if (setOver) {
            TextView scoreWinner = (TextView) findViewById(R.id.team_a_score);
            scoreWinner.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));
        }
    }

    /**
     * Displays the given set score for Team A.
     */
    public void displaySetForTeamB(int score, boolean setOver) {
        TextView scoreSetViewB = (TextView) findViewById(R.id.team_b_set);
        scoreSetViewB.setText(String.valueOf(scoreSetTeamB));
        if (setOver) {
            TextView scoreWinner = (TextView) findViewById(R.id.team_b_score);
            scoreWinner.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));
        }
    }

    public void removeOneForTeam(View v) {
        int parentId = ((View) v.getParent()).getId();
        int team = getParentView(parentId);

        if (!setEnded) {
            if (team == 0 && scoreTeamA > 0) {
                //team A
                scoreTeamA = scoreTeamA - 1;
                displayForTeamA(scoreTeamA);
            } else if (team == 1 && scoreTeamB > 0) {
                //team B
                scoreTeamB = scoreTeamB - 1;
                displayForTeamB(scoreTeamB);
            }
        } else {
            if (scoreTeamA > scoreTeamB) {
                //Allow decrement only scoreTeamA
                if (team == 0) {
                    scoreTeamA = scoreTeamA - 1;
                    displayForTeamA(scoreTeamA);
                    scoreSetTeamA -= 1;
                    displaySetForTeamA(scoreSetTeamA, true);
                    resetStyle();
                    setEnded = false;
                }
            } else {
                //Allow decrement only scoreTeamB
                if (team == 1) {
                    scoreTeamB = scoreTeamB - 1;
                    displayForTeamB(scoreTeamB);
                    scoreSetTeamB -= 1;
                    displaySetForTeamB(scoreSetTeamB, true);
                    resetStyle();
                    setEnded = false;
                }
            }
        }
        if ((hasBall == hasBallPrevious && hasBallPrevious == team) || hasBall == -1 || hasBallPrevious == -1) {
            //Do nothing because team is already leading
        } else {
            if (lineUpA.size() == 6 && lineUpB.size() == 6) {
                if (team == 0) {
                    String itemToMove = (lineUpA.get(5));
                    lineUpA.remove(5);
                    lineUpA.add(0, itemToMove);
                    hasBall = 1;
                    hasBallPrevious = -1;
                } else if (team == 1) {
                    String itemToMove = (lineUpB.get(5));
                    lineUpB.remove(5);
                    lineUpB.add(0, itemToMove);
                    hasBall = 0;
                    hasBallPrevious = -1;
                }
            }
        }
    }

    public void addOneForTeam(View v) {
        int parentId = ((View) v.getParent()).getId();
        int team = getParentView(parentId);
        int maxSetPoints = scoreSetTeamA == 2 && scoreSetTeamB == 2 ? 15 : 25;


        if (!setEnded) {
            if (team == 0) {
                //team A
                scoreTeamA = scoreTeamA + 1;
                displayForTeamA(scoreTeamA);
                if (hasBall == 1) {
                    if (lineUpA.size() == 6 && lineUpB.size() == 6) {
                        String itemToMove = (lineUpA.get(0));
                        lineUpA.remove(0);
                        lineUpA.add(itemToMove);
                    }
                    hasBallPrevious = 1;
                } else {
                    hasBallPrevious = 0;
                }
                hasBall = 0;
            } else if (team == 1) {
                //team B
                scoreTeamB = scoreTeamB + 1;
                displayForTeamB(scoreTeamB);
                if (hasBall == 0) {
                    if (lineUpA.size() == 6 && lineUpB.size() == 6) {
                        String itemToMove = (lineUpB.get(0));
                        lineUpB.remove(0);
                        lineUpB.add(itemToMove);
                    }
                    hasBallPrevious = 0;
                } else {
                    hasBallPrevious = 1;
                }
                hasBall = 1;
            }

            if ((scoreTeamA > (scoreTeamB + 1) || scoreTeamB > (scoreTeamA + 1)) && (scoreTeamA > maxSetPoints - 1 || scoreTeamB > maxSetPoints - 1)) {
                setEnded = true;
                CharSequence setWinner = scoreTeamA > scoreTeamB ? teamAName : teamBName;

                totalResult[scoreSetTeamA + scoreSetTeamB] = scoreTeamA + " - " + scoreTeamB;
                showSetResult(scoreTeamA, scoreTeamB, scoreSetTeamA + scoreSetTeamB);

                if (scoreTeamA > scoreTeamB) {
                    scoreSetTeamA += 1;
                    displaySetForTeamA(scoreSetTeamA, true);
                } else {
                    scoreSetTeamB += 1;
                    displaySetForTeamB(scoreSetTeamB, true);
                }

                //Match ended
                if (scoreSetTeamA == 3 || scoreSetTeamB == 3) {
                    matchEnded = true;
                    // Set bold winner score
                    if (team == 0) {
                        TextView scoreSetViewA = (TextView) findViewById(R.id.team_a_set);
                        scoreSetViewA.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));
                    } else {
                        TextView scoreSetViewB = (TextView) findViewById(R.id.team_b_set);
                        scoreSetViewB.setTypeface(Typeface.create("sans-serif-light", Typeface.BOLD));
                    }

//                    TextView matchEndedString = (TextView) findViewById(R.id.match_ended_string);
//                    matchEndedString.setVisibility(View.VISIBLE);
//                    TextView matchWinnerString = (TextView) findViewById(R.id.match_winner_string);
//                    matchWinnerString.setVisibility(View.VISIBLE);
//                    matchWinnerString.setText("The winner is " + setWinner);

                    Button resetButton = (Button) findViewById(R.id.restart_set_button);
                    resetButton.setVisibility(View.INVISIBLE);
//                    Button resetGameButton = (Button) findViewById(R.id.restart_game_button);
//                    resetGameButton.setVisibility(View.VISIBLE);
                    Button saveGameButton = (Button) findViewById(R.id.save_game_button);
                    saveGameButton.setVisibility(View.VISIBLE);

                    LinearLayout shareLogos = (LinearLayout) findViewById(R.id.sharing_logos);
                    shareLogos.setVisibility(View.VISIBLE);

                    AlertDialog.Builder builder = new AlertDialog.Builder(this);

                    builder.setTitle("The match is over")
                            .setMessage("The winner is " + setWinner)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                // do something when the button is clicked
                                public void onClick(DialogInterface arg0, int arg1) {

                                }
                            });

                    AlertDialog dialog = builder.create();
                    dialog.show();

                }
            }
        }
    }

    private void showSetResult(int scoreTeamA, int scoreTeamB, int i) {
        LinearLayout setResults = (LinearLayout) findViewById(R.id.set_list);
        ((TextView) setResults.getChildAt(i)).setText(scoreTeamA + " - " + scoreTeamB);
    }

    public int getParentView(int id) {
        int team = -1;
        if (id == R.id.team_a) {
            team = 0;
        } else if (id == R.id.team_b) {
            team = 1;
        }
        return team;
    }

    public void resetStyle() {
        TextView scoreTeamA = (TextView) findViewById(R.id.team_a_score);
        scoreTeamA.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        TextView scoreTeamB = (TextView) findViewById(R.id.team_b_score);
        scoreTeamB.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        TextView scoreSetTeamA = (TextView) findViewById(R.id.team_a_set);
        scoreSetTeamA.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
        TextView scoreSetTeamB = (TextView) findViewById(R.id.team_b_set);
        scoreSetTeamB.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));

        TextView matchEndedString = (TextView) findViewById(R.id.match_ended_string);
        matchEndedString.setVisibility(View.GONE);
        TextView matchWinnerString = (TextView) findViewById(R.id.match_winner_string);
        matchWinnerString.setVisibility(View.GONE);

        Button resetButton = (Button) findViewById(R.id.restart_set_button);
        resetButton.setVisibility(View.VISIBLE);
        Button saveGameButton = (Button) findViewById(R.id.save_game_button);
        saveGameButton.setVisibility(View.GONE);

        LinearLayout shareLogos = (LinearLayout) findViewById(R.id.sharing_logos);
        shareLogos.setVisibility(View.GONE);

    }

    public void resetScore(View v) {
        if (!matchEnded) {
            TextView scoreViewA = (TextView) findViewById(R.id.team_a_score);
            scoreTeamA = 0;
            scoreViewA.setText(String.valueOf(scoreTeamA));
            TextView scoreViewB = (TextView) findViewById(R.id.team_b_score);
            scoreTeamB = 0;
            scoreViewB.setText(String.valueOf(scoreTeamB));

            setEnded = false;

            TextView scoreTeamA = (TextView) findViewById(R.id.team_a_score);
            scoreTeamA.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
            TextView scoreTeamB = (TextView) findViewById(R.id.team_b_score);
            scoreTeamB.setTypeface(Typeface.create("sans-serif-light", Typeface.NORMAL));
//            resetCheckbox();
            hasBallPrevious = -1;
            hasBall = -1;
            lineUpA = new ArrayList<>(mSavedLineUpA);
            lineUpB = new ArrayList<>(mSavedLineUpB);
        }
    }

    public void saveMatch(View v) {

        ContentValues values = new ContentValues();
        values.put(MatchContract.MatchEntry.COLUMN_NAME_A, teamAName);
        values.put(MatchContract.MatchEntry.COLUMN_NAME_B, teamBName);
        values.put(MatchContract.MatchEntry.COLUMN_RES_A, scoreSetTeamA);
        values.put(MatchContract.MatchEntry.COLUMN_RES_B, scoreSetTeamB);
        values.put(MatchContract.MatchEntry.COLUMN_TOTAL_RES, "25 - 23, 25 - 18, 20 - 25, 22 - 25, 15 - 2");
        values.put(MatchContract.MatchEntry.COLUMN_LOGO_A, mImageA);
        values.put(MatchContract.MatchEntry.COLUMN_LOGO_B, mImageB);

        JSONArray mJSONArray = new JSONArray(Arrays.asList(totalResult));
        String totalResultString = mJSONArray.toString();
        values.put(MatchContract.MatchEntry.COLUMN_TOTAL_RES, totalResultString);

        Uri newUri = getContentResolver().insert(MatchContract.MatchEntry.CONTENT_URI, values);
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.insert_game_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.insert_game_successful),
                    Toast.LENGTH_SHORT).show();
        }
        backToHomePage();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt("scoreTeamA", scoreTeamA);
        outState.putInt("scoreTeamB", scoreTeamB);
        outState.putInt("scoreSetTeamA", scoreSetTeamA);
        outState.putInt("scoreSetTeamB", scoreSetTeamB);
        outState.putBoolean("setEnded", setEnded);
        outState.putBoolean("matchEnded", matchEnded);
        outState.putStringArray("totalResult", totalResult);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        setEnded = savedInstanceState.getBoolean("setEnded");
        matchEnded = savedInstanceState.getBoolean("matchEnded");

        scoreTeamA = savedInstanceState.getInt("scoreTeamA");
        displayForTeamA(scoreTeamA);
        scoreTeamB = savedInstanceState.getInt("scoreTeamB");
        displayForTeamB(scoreTeamB);
        scoreSetTeamA = savedInstanceState.getInt("scoreSetTeamA");
        scoreSetTeamB = savedInstanceState.getInt("scoreSetTeamB");
        if (scoreTeamA > scoreTeamB && setEnded) {
            displaySetForTeamA(scoreSetTeamA, true);
            displaySetForTeamB(scoreSetTeamB, false);
        } else if (scoreTeamA < scoreTeamB && setEnded) {
            displaySetForTeamA(scoreSetTeamA, false);
            displaySetForTeamB(scoreSetTeamB, true);
        } else {
            displaySetForTeamA(scoreSetTeamA, false);
            displaySetForTeamB(scoreSetTeamB, false);
        }
        totalResult = savedInstanceState.getStringArray("totalResult");
        //TODO ended game

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // do something useful
                exitByBackKey();
                return (true);
        }

        return (super.onOptionsItemSelected(item));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                // do something here
                exitByBackKey();
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {
        if (vpPager.getCurrentItem() == 0) {

            new AlertDialog.Builder(this)
                    .setMessage("Do you really want to exit game?")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                            backToHomePage();
                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {

                        // do something when the button is clicked
                        public void onClick(DialogInterface arg0, int arg1) {
                        }
                    })
                    .show();
        } else {
            vpPager.setCurrentItem(0);
        }
    }

    public void backToHomePage() {
        Intent intent = new Intent(VolleyMatch.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setFlags(FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void shareOnFB(View view) {
        shareDialog = new ShareDialog(this);
        // this part is optional
        callbackManager = CallbackManager.Factory.create();
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }

        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {

            String resultsString = getResultString();
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(teamAName.toString() + " vs " + teamBName.toString())
                    .setContentDescription(
                            "Results: " + scoreSetTeamA + " - " + scoreSetTeamB +
                                    ", Set list: " + resultsString)
                    .setContentUrl(Uri.parse("https://maxcdn.icons8.com/Share/icon/Sports//volleyball1600.png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (callbackManager != null) {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void shareOnGoogle(View view) {

        String resultsString = getResultString();
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(this)
                .setType("text/plain")
                .setText(teamAName.toString() + " vs " + teamBName.toString() + "\n" +
                        "Results: " + "\n" + scoreSetTeamA + " - " + scoreSetTeamB +
                        "\n" + "Set list: " + "\n" + resultsString)
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.provehitoIA.ferno92.volleyscorekeeper"))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    public void shareOnTwitter(View view) {
        String resultsString = getResultString();

        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                //max length string == 10 each team
                .text(teamAName.toString() + " vs " + teamBName.toString() + "\n" +
                        "Results: " + scoreSetTeamA + " - " + scoreSetTeamB +
                        "\n" + "Set list: " + resultsString + "\n #volley #VolleyScoreKeeper" + "\n\n" + "https://play.google.com/store/apps/details?id=com.provehitoIA.ferno92.volleyscorekeeper");
        builder.show();
    }

    public String getResultString() {
        String resultsString = "";
        for (int i = 0; i < totalResult.length; i++) {
            if (totalResult[i] != null) {
                resultsString = resultsString.concat(totalResult[i].toString() + " | ");
            }
        }
        return resultsString;
    }

    public int getDeviceHeight() {
        //Get height of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.heightPixels / density);
    }

    public int getDeviceWidth() {
        //Get width of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.widthPixels / density);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("VolleyMatch Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
        setView();
    }

    private void setView() {

        if (this.adapterViewPager.getFirstFragment() != null) {

        }
//        Fragment currentFragment = mFragmentManager.findFragmentByTag("matchMainFragment");
//        if (currentFragment != null && !mOnEditLineUp) {
//            displayNameTeamA();
//            displayNameTeamB();
//            displayForTeamA(scoreTeamA);
//            displayForTeamB(scoreTeamB);
//            if (scoreTeamA > scoreTeamB && setEnded) {
//                displaySetForTeamA(scoreSetTeamA, true);
//                displaySetForTeamB(scoreSetTeamB, false);
//            }else if (scoreTeamA < scoreTeamB && setEnded) {
//                displaySetForTeamA(scoreSetTeamA, false);
//                displaySetForTeamB(scoreSetTeamB, true);
//            } else {
//                displaySetForTeamA(scoreSetTeamA, false);
//                displaySetForTeamB(scoreSetTeamB, false);
//            }
//            refillSetResults();
//            if(matchEnded){
//                Button resetButton = (Button) findViewById(R.id.restart_set_button);
//                resetButton.setVisibility(View.INVISIBLE);
//                Button saveGameButton = (Button) findViewById(R.id.save_game_button);
//                saveGameButton.setVisibility(View.VISIBLE);
//
//                LinearLayout shareLogos = (LinearLayout) findViewById(R.id.sharing_logos);
//                shareLogos.setVisibility(View.VISIBLE);
//
//            }
//            RelativeLayout mainView = (RelativeLayout) findViewById(R.id.volley_match_main_layout);
//            mainView.setOnTouchListener(new OnSwipeTouchListener(this) {
//                @Override
//                public void onSwipeLeft() {
//                    mOnEditLineUp = true;
//                    // Whatever
//                    Toast.makeText(getApplicationContext(), "Swiping to the line-up page",
//                            Toast.LENGTH_SHORT).show();
//
//                    android.support.v4.app.FragmentTransaction ft = mFragmentManager.beginTransaction();
//                    ft.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
//
//                    mainFragment = newInstance();
//                    ft.replace(R.id.volley_match_fragment, mainFragment, "detailLineUpFragment");
//                    ft.addToBackStack(null);
//                    // Start the animated transition.
//                    ft.commit();
//                }
//            });
//        } else {
//            //Swipe back
//            View imageCourt;
//            View mainView = findViewById(R.id.edit_lineup_main);
//            if (mainView == null) {
//                mainView = findViewById(R.id.current_lineup_fragment);
//            } else {
//                imageCourt = findViewById(R.id.court_with_number);
//                imageCourt.setOnTouchListener(new OnSwipeTouchListener(this) {
//                    @Override
//                    public void onSwipeRight() {
//                        swipeBack();
//                    }
//                });
//            }
//            mainView.setOnTouchListener(new OnSwipeTouchListener(this) {
//                @Override
//                public void onSwipeRight() {
//                    swipeBack();
//                }
//            });
//
//        }


    }

    private void refillSetResults() {
        for (int i = 0; i < totalResult.length; i++) {
            if (totalResult[i] != null) {
                LinearLayout setResults = (LinearLayout) findViewById(R.id.set_list);
                ((TextView) setResults.getChildAt(i)).setText(totalResult[i]);
            }
        }
    }

    private void swipeBack() {
        mOnEditLineUp = false;

    }

    private void modifyLineUp() {
        if (mainFragment instanceof CurrentLineUpFragment) {
            lineUpA = ((CurrentLineUpFragment) mainFragment).getCurrentLineUpA();
            lineUpB = ((CurrentLineUpFragment) mainFragment).getCurrentLineUpB();
            if (scoreTeamA == scoreTeamB && scoreTeamA == 0) {
                mSavedLineUpA = new ArrayList<>(lineUpA);
                mSavedLineUpB = new ArrayList<>(lineUpB);
            }
        } else {
            ((EditLineUpFragment) mainFragment).getLineUp();
            lineUpA = ((EditLineUpFragment) mainFragment).getCurrentLineUpA();
            lineUpB = ((EditLineUpFragment) mainFragment).getCurrentLineUpB();
            if (lineUpA.size() < 6 || lineUpB.size() < 6) {
                lineUpA.clear();
                lineUpB.clear();
            } else {
                mSavedLineUpA = new ArrayList<>(lineUpA);
                mSavedLineUpB = new ArrayList<>(lineUpB);
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public Fragment newInstance() {
        Fragment lineUpFragment = null;
        Bundle args = new Bundle();
        //Create layout with the 2 views
        if (lineUpA.size() == 6 && lineUpB.size() == 6) {
            lineUpFragment = new CurrentLineUpFragment();
            args.putStringArrayList("lineUpA", lineUpA);
            args.putStringArrayList("lineUpB", lineUpB);
        } else {
            lineUpFragment = new EditLineUpFragment();
        }
        args.putString("nameA", teamAName);
        args.putString("nameB", teamBName);
        lineUpFragment.setArguments(args);

        return lineUpFragment;
    }

    @Override
    public void onBackStackChanged() {

        setView();
    }
}

