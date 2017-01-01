package com.provehitoIA.ferno92.volleyscorekeeper.homepage;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by lucas on 21/11/2016.
 */

public class GameListInfoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
    /** Content URI for the existing game */
    private Uri mCurrentGameUri;

    private static final int MATCH_LOADER = 0;
    TextView tNameA;
    TextView tNameB;
    TextView tResA;
    TextView tResB;
    View rootView;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String[] totalResultsStringArray;
    Context mApplicationContext;

    public GameListInfoFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){
        rootView = inflater.inflate(R.layout.match_list_info,
                container, false);
        mApplicationContext = MainActivity.getContextOfApplication();
        return rootView;
    }

    public void changeGameInfoData(Uri uri){
        mCurrentGameUri = uri;
        tNameA = (TextView) rootView.findViewById(R.id.team_a_name);
        tNameB = (TextView) rootView.findViewById(R.id.team_b_name);
        tResA = (TextView) rootView.findViewById(R.id.team_a_score);
        tResB = (TextView) rootView.findViewById(R.id.team_b_score);
        getLoaderManager().initLoader(MATCH_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Since the editor shows all pet attributes, define a projection that contains
        // all columns from the pet table
        String[] projection = {
                MatchContract.MatchEntry._ID,
                MatchContract.MatchEntry.COLUMN_NAME_A,
                MatchContract.MatchEntry.COLUMN_NAME_B,
                MatchContract.MatchEntry.COLUMN_RES_A,
                MatchContract.MatchEntry.COLUMN_RES_B,
                MatchContract.MatchEntry.COLUMN_TOTAL_RES,
                MatchContract.MatchEntry.COLUMN_LOGO_A,
                MatchContract.MatchEntry.COLUMN_LOGO_B
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(getActivity(),   // Parent activity context
                mCurrentGameUri,         // Query the content URI for the current pet
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
// Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (cursor.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameTeamAColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NAME_A);
            int nameTeamBColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NAME_B);
            int resAColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_RES_A);
            int resBColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_RES_B);
            int resultsColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_TOTAL_RES);

            // Extract out the value from the Cursor for the given column index
            String nameA = cursor.getString(nameTeamAColumnIndex);
            String nameB = cursor.getString(nameTeamBColumnIndex);
            int resA = cursor.getInt(resAColumnIndex);
            int resB = cursor.getInt(resBColumnIndex);
            String totalResultsString = cursor.getString(resultsColumnIndex);

            tNameA.setText(nameA);
            tNameB.setText(nameB);
            tResA.setText("" + resA);
            tResB.setText("" + resB);

            try {
                JSONArray resultsArray = new JSONArray(totalResultsString);
                totalResultsStringArray = new String[resultsArray.length()];
                LinearLayout setTitleList = (LinearLayout) rootView.findViewById(R.id.set_title_list);
                LinearLayout setList = (LinearLayout) rootView.findViewById(R.id.set_list);
                for(int i = 0; i < resultsArray.length(); i++){
                    totalResultsStringArray[i] = resultsArray.getString(i);

                    if (setList.getChildAt(i) instanceof TextView) {
                        TextView setTextView = (TextView) setList.getChildAt(i);
                        if(resultsArray.getString(i) == "null"){
                            setTextView.setVisibility(View.GONE);
                            TextView setTitleTextView = (TextView) setTitleList.getChildAt(i);
                            setTitleTextView.setVisibility(View.GONE);
                        }else{
                            setTextView.setText(resultsArray.getString(i));
                            setTextView.setVisibility(View.VISIBLE);
                            TextView setTitleTextView = (TextView) setTitleList.getChildAt(i);
                            setTitleTextView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            View parentLayout = rootView.findViewById(R.id.game_list_info_layout_parent);
            parentLayout.setVisibility(View.VISIBLE);
            View emptyView = rootView.findViewById(R.id.empty_view);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageView shareTwitter = (ImageView) getActivity().findViewById(R.id.twitter_share_button);
        shareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnTwitter();
            }
        });
        ImageView shareFB = (ImageView) getActivity().findViewById(R.id.fb_share_button);
        shareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnFB();
            }
        });

        ImageView shareGPlus = (ImageView) getActivity().findViewById(R.id.gplus_share_button);
        shareGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnGoogle();
            }
        });
    }

    public void shareOnFB(){
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
                Toast.makeText(getContext(), "Check your internet connection",
                        Toast.LENGTH_SHORT).show();
            }

        });
        if (ShareDialog.canShow(ShareLinkContent.class)) {
            String resultsString = "";
            for(int i = 0; i < totalResultsStringArray.length; i++){
                if(totalResultsStringArray[i].toString() != "null") {
                    resultsString = resultsString.concat(totalResultsStringArray[i].toString() + " | ");
                }
            }
            ShareLinkContent linkContent = new ShareLinkContent.Builder()
                    .setContentTitle(tNameA.getText().toString() + " vs " + tNameB.getText().toString())
                    .setContentDescription(
                            "Results: " + tResA.getText().toString() + " - " + tResB.getText().toString() +
                                    ", Set list: " + resultsString)
                    .setContentUrl(Uri.parse("https://maxcdn.icons8.com/Share/icon/Sports//volleyball1600.png"))
                    .build();

            shareDialog.show(linkContent);
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(callbackManager != null){
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void shareOnGoogle(){
        String resultsString = "";
        for(int i = 0; i < totalResultsStringArray.length; i++){
            if(totalResultsStringArray[i].toString() != "null"){
                resultsString = resultsString.concat(totalResultsStringArray[i].toString() + " | ");
            }
        }
        // Launch the Google+ share dialog with attribution to your app.
        Intent shareIntent = new PlusShare.Builder(mApplicationContext)
                .setType("text/plain")
                .setText(tNameA.getText().toString() + " vs " + tNameB.getText().toString() + "\n"+
                        "Results: " + "\n" + tResA.getText().toString() + " - " + tResB.getText().toString() +
                        "\n" + "Set list: " + "\n" + resultsString)
                .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.provehitoIA.ferno92.volleyscorekeeper"))
                .getIntent();

        startActivityForResult(shareIntent, 0);
    }

    public void shareOnTwitter(){
        String resultsString = "";
        for(int i = 0; i < totalResultsStringArray.length; i++){
            if(totalResultsStringArray[i]!= null){
                resultsString = resultsString.concat(totalResultsStringArray[i].toString() + " | ");
            }
        }
        Intent builder = new TweetComposer.Builder(mApplicationContext)
                //max length string == 10 each team
                .text(tNameA.getText().toString() + " vs " + tNameB.getText().toString() + "\n"+
                        "Results: " + tResA.getText().toString() + " - " + tResB.getText().toString() +
                        "\n" + "Set list: " + resultsString + "\n #volley #VolleyScoreKeeper" + "\n\n" +
                        "https://play.google.com/store/apps/details?id=com.provehitoIA.ferno92.volleyscorekeeper").createIntent();
        builder.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(builder);
    }
}
