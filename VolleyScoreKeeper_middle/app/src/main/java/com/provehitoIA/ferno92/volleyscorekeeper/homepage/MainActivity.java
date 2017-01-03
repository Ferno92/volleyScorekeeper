package com.provehitoIA.ferno92.volleyscorekeeper.homepage;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.matchInfos.MatchInfo;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;

import io.fabric.sdk.android.Fabric;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, LoaderManager.LoaderCallbacks<Cursor> {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
//    private static final String TWITTER_KEY = "vzJ0zNlxVzzFycK72oOAlF1Du";
//    private static final String TWITTER_SECRET = "7Vrimwrk2kxDfjxNKZNof1TxjuGdekkwfl2231nLvI6tB8FrC8";
    private static final String TWITTER_KEY = "iiUSCjhR1Ez0ceVbKTdYTR4Vp";
    private static final String TWITTER_SECRET = "xma4awp4yTz2bCx43j8AuQtJ3Yy8nEMfIYWnPcD7Mq0CqZkhMz";

    private static final int MATCH_LOADER = 0;
    MatchCursorAdapter mCursorAdapter;
    public static Context contextOfApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int h = getDeviceHeight();
        int w = getDeviceWidth();
        if(h > 600 && w > 600){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            contextOfApplication = getApplicationContext();
        }
        //Twitter init
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));
        //Facebook init
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(getApplication());


        setContentView(com.provehitoIA.ferno92.volleyscorekeeper.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.fab);

        fab.setImageResource(com.provehitoIA.ferno92.volleyscorekeeper.R.mipmap.ic_volley_vs);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MatchInfo.class);
                startActivity(intent);
            }
        });

//        DrawerLayout drawer = (DrawerLayout) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, com.provehitoIA.ferno92.volleyscorekeeper.R.string.navigation_drawer_open, com.provehitoIA.ferno92.volleyscorekeeper.R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

//        NavigationView navigationView = (NavigationView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_view);
//        navigationView.setNavigationItemSelectedListener(this);

        ListView gameListView = (ListView) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.list);
        View emptyView = findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.empty_view);
        gameListView.setEmptyView(emptyView);

        //Setup an adapter to create a list item for each row of match data in the Cursor.
        // There is no match data yet (until the loader finishes) so pass in null for the Cursor.
        mCursorAdapter = new MatchCursorAdapter(this, null);
        gameListView.setAdapter(mCursorAdapter);

        // Click item listener
        gameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                view.setSelected(true);
                //Get height and width of device
                DisplayMetrics dMetrics = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
                float density = dMetrics.density;
                int w = Math.round(dMetrics.widthPixels / density);
                int h = Math.round(dMetrics.heightPixels / density);
                //Get/set uri
                Uri currentGameUri = ContentUris.withAppendedId(MatchContract.MatchEntry.CONTENT_URI, id);

                if(h < w && (h > 600 || w > 600) ) {
                    GameListInfoFragment gameListInfoFragment = (GameListInfoFragment) getSupportFragmentManager()
                            .findFragmentById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.match_fragment_info);
                    gameListInfoFragment.changeGameInfoData(currentGameUri);

                }else{
                    //Portrait (in all device) or landscape in mobile only
                    Intent intent = new Intent(MainActivity.this, GameListInfo.class);
                    intent.setData(currentGameUri);
                    startActivity(intent);
                }
            }
        });
        gameListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
                v.setPressed(true);
                Uri currentGameUri = ContentUris.withAppendedId(MatchContract.MatchEntry.CONTENT_URI, id);
                showDeleteItemConfirmationDialog(currentGameUri);
                return true;
            }
        });

        //Kick off the loader
        getSupportLoaderManager().initLoader(MATCH_LOADER, null, this);

    }
    private void insertMatch() {

        ContentValues values = new ContentValues();
        values.put(MatchContract.MatchEntry.COLUMN_NAME_A, "Team A");
        values.put(MatchContract.MatchEntry.COLUMN_NAME_B, "Team B");
        values.put(MatchContract.MatchEntry.COLUMN_RES_A, 3);
        values.put(MatchContract.MatchEntry.COLUMN_RES_B, 2);

        String fakeTotalResult[] = {"25 - 23", "25 - 18", "20 - 25", "22 - 25", "15 - 2"};
        JSONArray mJSONArray = new JSONArray(Arrays.asList(fakeTotalResult));
        String totalResultString = mJSONArray.toString();
        values.put(MatchContract.MatchEntry.COLUMN_TOTAL_RES, totalResultString);
        values.put(MatchContract.MatchEntry.COLUMN_LOGO_A, (byte[]) null);
        values.put(MatchContract.MatchEntry.COLUMN_LOGO_B, (byte[]) null);

        Uri newUri = getContentResolver().insert(MatchContract.MatchEntry.CONTENT_URI, values);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(com.provehitoIA.ferno92.volleyscorekeeper.R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.insert_dummy_data) {
            insertMatch();
            return true;
        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.delete_all_matches) {
            showDeleteConfirmationDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteConfirmationDialog(){
        AlertDialog.Builder alertLineUp = new AlertDialog.Builder(this);
        alertLineUp.setTitle("Delete");
        alertLineUp.setMessage("Remove all the games from list?");

        alertLineUp.setCancelable(false);
        alertLineUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteAllMatches();
            }
        });

        alertLineUp.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = alertLineUp.create();
        alert.show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_camera) {
            // Handle the camera action
        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_gallery) {

        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_slideshow) {

        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_manage) {

        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_share) {

        } else if (id == com.provehitoIA.ferno92.volleyscorekeeper.R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
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
        return new CursorLoader(this,
                MatchContract.MatchEntry.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    private void deleteAllMatches(){
        int rowsDeleted = getContentResolver().delete(MatchContract.MatchEntry.CONTENT_URI, null, null);
        Log.v("MainActivity", rowsDeleted + " rows deleted from match database");

        // Show a toast message depending on whether or not the delete was successful.
        if (rowsDeleted == 0) {
            // If no rows were deleted, then there was an error with the delete.
            Toast.makeText(this, getString(com.provehitoIA.ferno92.volleyscorekeeper.R.string.delete_all_game_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the delete was successful and we can display a toast.
            Toast.makeText(this, getString(com.provehitoIA.ferno92.volleyscorekeeper.R.string.delete_all_game_successful),
                    Toast.LENGTH_SHORT).show();
        }
        removeCurrentFragmentView();
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

    // a static variable to get a reference of our application context
    public static Context getContextOfApplication()
    {
        return contextOfApplication;
    }

    private void deleteGame(Uri currentGameUri){
        // Only perform the delete if this is an existing pet.
        if (currentGameUri != null) {
            // Call the ContentResolver to delete the match at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentGameUri
            // content URI already identifies the match that we want.
            int rowsDeleted = getContentResolver().delete(currentGameUri, null, null);

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
            removeCurrentFragmentView();
        }
    }
    private void showDeleteItemConfirmationDialog(final Uri currentGameUri){
        AlertDialog.Builder alertLineUp = new AlertDialog.Builder(this);
        alertLineUp.setTitle("Delete");
        alertLineUp.setMessage("Remove this game from list?");

        alertLineUp.setCancelable(false);
        alertLineUp.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteGame(currentGameUri);
            }
        });

        alertLineUp.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });
        AlertDialog alert = alertLineUp.create();
        alert.show();
    }

    public void removeCurrentFragmentView(){
        LinearLayout fragmentView = (LinearLayout) findViewById(R.id.game_list_info_layout_parent);
        if(fragmentView != null){
            fragmentView.setVisibility(View.GONE);
            RelativeLayout fragmentEmptyView = (RelativeLayout) findViewById(R.id.empty_view);
            fragmentEmptyView.setVisibility(View.VISIBLE);
        }
    }
}
