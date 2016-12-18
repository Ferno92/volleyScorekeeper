package com.provehitoIA.ferno92.volleyscorekeeper.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract.MatchEntry;

/**
 * Created by l.fernandez on 10/11/2016.
 */

public class MatchDbHelper extends SQLiteOpenHelper {
    // Name of the database file
    private static final String DATABASE_NAME = "championship.db";

    // Database version
    private static final int DATABASE_VERSION = 1;

    public MatchDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_MATCHES_TABLE = "CREATE TABLE " + MatchEntry.TABLE_NAME + "("
                + MatchEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MatchEntry.COLUMN_NAME_A + " TEXT, "
                + MatchEntry.COLUMN_NAME_B + " TEXT, "
                + MatchEntry.COLUMN_RES_A + " INTEGER, "
                + MatchEntry.COLUMN_RES_B + " INTEGER, "
                + MatchEntry.COLUMN_TOTAL_RES + " TEXT )";

        db.execSQL(SQL_CREATE_MATCHES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
