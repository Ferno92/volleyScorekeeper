package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;

/**
 * Created by lucas on 06/12/2016.
 */

public class CurrentLineUpFragment extends Fragment {

    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();
    String mNameA;
    String mNameB;
    View mRootView;
    public CurrentLineUpFragment(){

    }

    public static CurrentLineUpFragment newInstance(String nameA, String nameB, ArrayList<String> lineUpA, ArrayList<String> lineUpB){
        CurrentLineUpFragment lineUpFragment = new CurrentLineUpFragment();
        Bundle args = new Bundle();
        args.putStringArrayList("lineUpA", lineUpA);
        args.putStringArrayList("lineUpB", lineUpB);
        args.putString("nameA", nameA);
        args.putString("nameB", nameB);
        lineUpFragment.setArguments(args);

        return lineUpFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState){

        mRootView = inflater.inflate(R.layout.current_line_up,
                container, false);
        getLineUp();
        return mRootView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void getLineUp() {
        mLineUpA = getArguments().getStringArrayList("lineUpA");
        mLineUpB = getArguments().getStringArrayList("lineUpB");
        mNameA = getArguments().getString("nameA");
        mNameB = getArguments().getString("nameB");
        TextView nameATextView = (TextView) mRootView.findViewById(R.id.lineup_name_a);
        TextView nameBTextView = (TextView) mRootView.findViewById(R.id.lineup_name_b);
        nameATextView.setText(mNameA);
        nameBTextView.setText(mNameB);

        setLineUpView();
    }

    private void setLineUpView() {
        TextView tp1a = (TextView) mRootView.findViewById(R.id.tp1a);
        tp1a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp2a = (TextView) mRootView.findViewById(R.id.tp2a);
        tp2a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp3a = (TextView) mRootView.findViewById(R.id.tp3a);
        tp3a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp4a = (TextView) mRootView.findViewById(R.id.tp4a);
        tp4a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp5a = (TextView) mRootView.findViewById(R.id.tp5a);
        tp5a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp6a = (TextView) mRootView.findViewById(R.id.tp6a);
        tp6a.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });

        tp1a.setText(mLineUpA.get(0));
        tp2a.setText(mLineUpA.get(1));
        tp3a.setText(mLineUpA.get(2));
        tp4a.setText(mLineUpA.get(3));
        tp5a.setText(mLineUpA.get(4));
        tp6a.setText(mLineUpA.get(5));

        TextView tp1b = (TextView) mRootView.findViewById(R.id.tp1b);
        tp1b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp2b = (TextView) mRootView.findViewById(R.id.tp2b);
        tp2b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp3b = (TextView) mRootView.findViewById(R.id.tp3b);
        tp3b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp4b = (TextView) mRootView.findViewById(R.id.tp4b);
        tp4b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp5b = (TextView) mRootView.findViewById(R.id.tp5b);
        tp5b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });
        TextView tp6b = (TextView) mRootView.findViewById(R.id.tp6b);
        tp6b.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                changePlayer(view);
                return true;
            }
        });

        tp1b.setText(mLineUpB.get(0));
        tp2b.setText(mLineUpB.get(1));
        tp3b.setText(mLineUpB.get(2));
        tp4b.setText(mLineUpB.get(3));
        tp5b.setText(mLineUpB.get(4));
        tp6b.setText(mLineUpB.get(5));

    }

    private void changePlayer(final View view) {
        final String playerNumber = ((TextView)view).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Substitute player");
        LayoutInflater inflater = (LayoutInflater)getContext().getSystemService (Context.LAYOUT_INFLATER_SERVICE);
        final View v = inflater.inflate(R.layout.change_player_dialog, null);
        builder.setView(v);
        final EditText editPlayer = (EditText) v.findViewById(R.id.edit_player_number);
        editPlayer.setText(playerNumber);
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                String newPlayerNumber = editPlayer.getText().toString();
                if(getResources().getResourceEntryName(view.getId()).toString().endsWith("a")){
                    int index = mLineUpA.indexOf(playerNumber);
                    mLineUpA.remove(index);
                    mLineUpA.add(index, newPlayerNumber);
                    ((TextView) view).setText(newPlayerNumber );
                }else{
                    int index = mLineUpB.indexOf(playerNumber);
                    mLineUpB.remove(index);
                    mLineUpB.add(index, newPlayerNumber);
                    ((TextView) view).setText(newPlayerNumber );
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });
        builder.show();
    }

    public ArrayList<String> getCurrentLineUpA(){
        return mLineUpA;
    }
    public ArrayList<String> getCurrentLineUpB(){
        return mLineUpB;
    }
}
