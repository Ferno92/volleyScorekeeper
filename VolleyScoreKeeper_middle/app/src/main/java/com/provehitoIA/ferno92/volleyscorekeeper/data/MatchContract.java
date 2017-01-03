package com.provehitoIA.ferno92.volleyscorekeeper.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by l.fernandez on 10/11/2016.
 */

public final class MatchContract {
    public static final String CONTENT_AUTHORITY = "com.provehitoIA.ferno92.volleyscorekeeper";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MATCHES = "matches";
    private MatchContract(){}

    public static final class MatchEntry implements BaseColumns{
        public static final  String CONTENT_LIST_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + "/" +
                CONTENT_AUTHORITY + "/" + PATH_MATCHES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MATCHES;

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MATCHES);
        public final static String TABLE_NAME = "matches";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME_A = "name_a";
        public final static String COLUMN_NAME_B = "name_b";
        public final static String COLUMN_RES_A = "result_a";
        public final static String COLUMN_RES_B = "result_b";
        public final static String COLUMN_TOTAL_RES = "total_results";
        public final static String COLUMN_LOGO_A = "logo_a";
        public final static String COLUMN_LOGO_B = "logo_b";
        public final static String COLUMN_LATITUDE = "latitude";
        public final static String COLUMN_LONGITUDE = "longitude";
    }
}
