package com.provehitoIA.ferno92.volleyscorekeeper.homepage;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
 * Created by lucas on 13/11/2016.
 */

public class GameListInfo extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    /** Content URI for the existing game */
    private Uri mCurrentGameUri;

    private static final int MATCH_LOADER = 0;
    TextView tNameA;
    TextView tNameB;
    TextView tResA;
    TextView tResB;
    CallbackManager mCallbackManager;
    ShareDialog mShareDialog;
    String[]totalResultsStringArray;
    String mLatitude = null;
    String mLongitude = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.provehitoIA.ferno92.volleyscorekeeper.R.layout.match_list_info);

        //get intent bla bla bla
        Intent intent = getIntent();
        mCurrentGameUri = intent.getData();

        tNameA = (TextView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_a_name);
        tNameB = (TextView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_b_name);
        tResA = (TextView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_a_score);
        tResB = (TextView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_b_score);


        getSupportLoaderManager().initLoader(MATCH_LOADER, null, this);
        ImageView shareTwitter = (ImageView) findViewById(R.id.twitter_share_button);
        shareTwitter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnTwitter();
            }
        });
        ImageView shareFB = (ImageView) findViewById(R.id.fb_share_button);
        shareFB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnFB();
            }
        });

        ImageView shareGPlus = (ImageView) findViewById(R.id.gplus_share_button);
        shareGPlus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareOnGoogle();
            }
        });

        Button gymPosition = (Button) findViewById(R.id.gym_position);
        gymPosition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Uri gmmIntentUri = Uri.parse("geo:37.7749,-122.4194?q=37.7749,-122.4194");
                Uri gmmIntentUri = Uri.parse("geo:" + mLatitude + "," + mLongitude + "?q=" + mLatitude + "," + mLongitude );
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null && mLatitude != null) {
                    startActivity(mapIntent);
                }
            }
        });
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
                MatchContract.MatchEntry.COLUMN_LOGO_B,
                MatchContract.MatchEntry.COLUMN_LATITUDE,
                MatchContract.MatchEntry.COLUMN_LONGITUDE
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
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
            int latitudeColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_LATITUDE);
            int longitudeColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_LONGITUDE);

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

            if(latitudeColumnIndex != -1){
                if(cursor.getString(latitudeColumnIndex) != null || cursor.getString(latitudeColumnIndex) != "") {
                    mLatitude = cursor.getString(latitudeColumnIndex);
                    mLongitude = cursor.getString(longitudeColumnIndex);
                }
            }

            if(mLatitude == null){
                Button gymPosition = (Button) findViewById(R.id.gym_position);
                gymPosition.setVisibility(View.GONE);
            }
            try {
                JSONArray resultsArray = new JSONArray(totalResultsString);
                totalResultsStringArray = new String[resultsArray.length()];
                LinearLayout setTitleList = (LinearLayout) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.set_title_list);
                LinearLayout setList = (LinearLayout) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.set_list);
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
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            View parentLayout = findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.game_list_info_layout_parent);
            parentLayout.setVisibility(View.VISIBLE);
            View emptyView = findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.empty_view);
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.provehitoIA.ferno92.volleyscorekeeper.R.menu.game_info_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.action_delete_current_game) {
            showDeleteConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteGame(){
        // Only perform the delete if this is an existing pet.
        if (mCurrentGameUri != null) {
            // Call the ContentResolver to delete the match at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentGameUri
            // content URI already identifies the match that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentGameUri, null, null);

            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(com.provehitoIA.ferno92.volleyscorekeeper.R.string.delete_game_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(com.provehitoIA.ferno92.volleyscorekeeper.R.string.delete_game_successful),
                        Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder alertLineUp = new AlertDialog.Builder(this);
        alertLineUp.setTitle("Delete");
        alertLineUp.setMessage("Remove this game from list?");

        alertLineUp.setCancelable(false);
        alertLineUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteGame();
            }
        });

        alertLineUp.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = alertLineUp.create();
        alert.show();
    }

    public void shareOnFB(){
        mShareDialog = new ShareDialog(this);
        // this part is optional
        mCallbackManager = CallbackManager.Factory.create();
        mShareDialog.registerCallback(mCallbackManager, new FacebookCallback<Sharer.Result>() {
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

            mShareDialog.show(linkContent);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(mCallbackManager != null){
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
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
        Intent shareIntent = new PlusShare.Builder(this)
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
            if(totalResultsStringArray[i].toString() != "null"){
                resultsString = resultsString.concat(totalResultsStringArray[i].toString() + " | ");
            }
        }
        TweetComposer.Builder builder = new TweetComposer.Builder(this)
                //max length string == 10 each team
                .text(tNameA.getText().toString() + " vs " + tNameB.getText().toString() + "\n"+
                        "Results: " + tResA.getText().toString() + " - " + tResB.getText().toString() +
                        "\n" + "Set list: " + resultsString + "\n #volley #VolleyScoreKeeper" + "\n\n" + "https://play.google.com/store/apps/details?id=com.provehitoIA.ferno92.volleyscorekeeper");
        builder.show();
    }
}
