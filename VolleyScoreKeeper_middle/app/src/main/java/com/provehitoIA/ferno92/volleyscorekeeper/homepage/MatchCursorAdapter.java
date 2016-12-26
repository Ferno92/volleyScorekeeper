package com.provehitoIA.ferno92.volleyscorekeeper.homepage;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.provehitoIA.ferno92.volleyscorekeeper.R;
import com.provehitoIA.ferno92.volleyscorekeeper.data.MatchContract;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by lucas on 13/11/2016.
 */

public class MatchCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link MatchCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public MatchCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }

    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(com.provehitoIA.ferno92.volleyscorekeeper.R.layout.game_list_item, parent, false);
    }

    /**
     * This method binds the match data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current pet can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTeamA = (TextView) view.findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_a_name);
        TextView nameTeamB = (TextView) view.findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_b_name);
        TextView scoreTeamA = (TextView) view.findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_a_score);
        TextView scoreTeamB = (TextView) view.findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.team_b_score);
        ImageView logoA = (ImageView) view.findViewById(R.id.team_a_logo);
        ImageView logoB = (ImageView) view.findViewById(R.id.team_b_logo);

        int nameAColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NAME_A);
        int nameBColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_NAME_B);
        int scoreAColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_RES_A);
        int scoreBColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_RES_B);
        int resultsColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_TOTAL_RES);
        int logoAColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_IMAGE_A);
        int logoBColumnIndex = cursor.getColumnIndex(MatchContract.MatchEntry.COLUMN_IMAGE_B);

        String nameTeamAString = cursor.getString(nameAColumnIndex);
        String nameTeamBString = cursor.getString(nameBColumnIndex);
        int scoreTeamAInt = cursor.getInt(scoreAColumnIndex);
        int scoreTeamBInt = cursor.getInt(scoreBColumnIndex);
        String totalResultsString = cursor.getString(resultsColumnIndex);
        byte[] logoABytes = cursor.getBlob(logoAColumnIndex);
        byte[] logoBBytes = cursor.getBlob(logoBColumnIndex);

        nameTeamA.setText(nameTeamAString);
        nameTeamB.setText(nameTeamBString);
        scoreTeamA.setText("" + scoreTeamAInt);
        scoreTeamB.setText("" + scoreTeamBInt);
        if(logoABytes.length != 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(logoABytes, 0, logoABytes.length);
            logoA.setImageBitmap(bitmap);
        }
        if(logoBBytes.length != 0){
            Bitmap bitmap = BitmapFactory.decodeByteArray(logoBBytes, 0, logoBBytes.length);
            logoB.setImageBitmap(bitmap);
        }

        // Bind sets data
//        try {
//            JSONArray resultsArray = new JSONArray(totalResultsString);
//            String[] totalResultsStringArray = new String[resultsArray.length()];
//            LinearLayout setTitleList = (LinearLayout) view.findViewById(R.id.set_title_list);
//            LinearLayout setList = (LinearLayout) view.findViewById(com.provehitoIA.ferno92.volleyscorekeeper.R.id.set_list);
//            for(int i = 0; i < resultsArray.length(); i++){
//                totalResultsStringArray[i] = resultsArray.getString(i);
//
//                    if (setList.getChildAt(i) instanceof TextView) {
//                        TextView setTextView = (TextView) setList.getChildAt(i);
//                        if(resultsArray.getString(i) == "null"){
//                            setTextView.setVisibility(View.GONE);
//                            setTitleList.getChildAt(i).setVisibility(View.GONE);
//                        }else{
//                            setTextView.setText(resultsArray.getString(i));
//                            setTextView.setVisibility(View.VISIBLE);
//                            setTitleList.getChildAt(i).setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }
}
