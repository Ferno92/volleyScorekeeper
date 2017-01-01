package com.provehitoIA.ferno92.volleyscorekeeper.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract.MatchEntry.TABLE_NAME;

/**
 * Created by lucas on 12/11/2016.
 */

public class MatchProvider extends ContentProvider {
    private MatchDbHelper mDbHelper;
    public static final String LOG_TAG = MatchProvider.class.getSimpleName();

    private static final int GAMES = 100;
    private static final int GAME_ID = 101;
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static{
        sUriMatcher.addURI(MatchContract.CONTENT_AUTHORITY, MatchContract.PATH_MATCHES, GAMES);
        sUriMatcher.addURI(MatchContract.CONTENT_AUTHORITY, MatchContract.PATH_MATCHES + "/#", GAME_ID);
    }

    @Override
    public boolean onCreate() {
        mDbHelper = new MatchDbHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        Cursor cursor;

        int match = sUriMatcher.match(uri);
        switch (match){
            case GAMES:
                cursor = database.query(TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            case GAME_ID:
                selection = MatchContract.MatchEntry._ID + "=?";
                selectionArgs = new String[]{ String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(TABLE_NAME, projection, selection,
                        selectionArgs, null, null, sortOrder);
                break;
            default:
                throw  new IllegalArgumentException("Cannot query unknown URI " + uri);
        }
        //if the data at this URI changes, then we know we need to update the cursor.
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return MatchContract.MatchEntry.CONTENT_LIST_TYPE;
            case GAME_ID:
                return MatchContract.MatchEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues){
        final int match = sUriMatcher.match(uri);
        switch (match){
            case GAMES:
                return insertMatch(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    private Uri insertMatch(Uri uri, ContentValues contentValues){
        // TODO: check data type?
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        long id = database.insert(TABLE_NAME, null, contentValues);

        Cursor dbCursor = database.query(TABLE_NAME, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        if(id == -1){
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;
        }
        //Notify all listeners that the data has changed for the match content URI
        getContext().getContentResolver().notifyChange(uri, null);
        return ContentUris.withAppendedId(uri, id);
    }
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Track the number of rows that were deleted
        int rowsDeleted;


        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case GAME_ID:
                // Delete a single row given by the ID in the URI
                selection = MatchContract.MatchEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                rowsDeleted = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case GAMES:
                return updateMatch(uri, contentValues, selection, selectionArgs);
            case GAME_ID:
                // For the PET_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = MatchContract.MatchEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return updateMatch(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    private int updateMatch(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        // TODO: Update the selected game in the match database table with the given ContentValues
        SQLiteDatabase database = mDbHelper.getWritableDatabase();
        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        // Return the number of rows updated
        return rowsUpdated;
    }
}
