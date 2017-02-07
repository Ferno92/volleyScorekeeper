package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.V;
import static com.provehitoIA.ferno92.volleyscorekeeper.R.id.textView;

/**
 * Created by lucas on 08/12/2016.
 */

public class EditLineUpFragment extends Fragment implements SecondPageFragmentListener {
    ArrayList<String> mLineUpA = new ArrayList<String>();
    ArrayList<String> mLineUpB = new ArrayList<String>();

    ArrayList<String> mLineUpAToCheck = new ArrayList<String>(Arrays.asList("-1", "-1", "-1", "-1", "-1", "-1"));
    ArrayList<String> mLineUpBToCheck = new ArrayList<String>(Arrays.asList("-1", "-1", "-1", "-1", "-1", "-1"));
    String mNameA;
    String mNameB;
    View mRootView;
    static SecondPageFragmentListener secondPageListener;

    public EditLineUpFragment() {

    }

    public static EditLineUpFragment newInstance(String nameA, String nameB, SecondPageFragmentListener listener) {

        secondPageListener = listener;
        EditLineUpFragment lineUpFragment = new EditLineUpFragment();
        Bundle args = new Bundle();
        args.putString("nameA", nameA);
        args.putString("nameB", nameB);
        lineUpFragment.setArguments(args);

        return lineUpFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        int h = getDeviceHeight();
        int w = getDeviceWidth();
        if (h > 600 && w > 600) {
            mRootView = inflater.inflate(R.layout.edit_line_up_horizontal,
                    container, false);
        } else {
            mRootView = inflater.inflate(R.layout.edit_line_up,
                    container, false);
        }
        Button lineUpEditedButton = (Button) mRootView.findViewById(R.id.lineup_edited_button);
        lineUpEditedButton.setVisibility(View.GONE);

        mNameA = getArguments().getString("nameA");
        mNameB = getArguments().getString("nameB");
        TextView nameATextView = (TextView) mRootView.findViewById(R.id.line_up_titleA);
        TextView nameBTextView = (TextView) mRootView.findViewById(R.id.line_up_titleB);
        nameATextView.setText(mNameA);
        nameBTextView.setText(mNameB);
        setLineUpRepeatedControls();
        return mRootView;
    }

    public int getDeviceHeight() {
        //Get height of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.heightPixels / density);
    }

    public int getDeviceWidth() {
        //Get width of device
        DisplayMetrics dMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);
        float density = dMetrics.density;
        return Math.round(dMetrics.widthPixels / density);
    }

    private void setLineUpRepeatedControls() {
        LinearLayout teamA = (LinearLayout) mRootView.findViewById(R.id.team_a);
        for (int i = 0; i < teamA.getChildCount(); i++) {
            final int index = i;
            if (teamA.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                        final EditText editTextView = (EditText) linearLayoutChild.getChildAt(j);

                        editTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    boolean repeated = false;
                                    for (int k = 0; k < mLineUpAToCheck.size(); k++) {
                                        if (mLineUpAToCheck.get(k).equals(editTextView.getText().toString())) {
                                            repeated = true;
                                            // Show alert
                                            ViewGroup row = (ViewGroup) editTextView.getParent();
                                            for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                                                View view = row.getChildAt(itemPos);
                                                if (view instanceof ImageView) {
                                                    view.setVisibility(View.VISIBLE);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (!repeated) {
                                        mLineUpAToCheck.add(index, editTextView.getText().toString());
                                        ViewGroup row = (ViewGroup) editTextView.getParent();
                                        for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                                            View view = row.getChildAt(itemPos);
                                            if (view instanceof ImageView) {
                                                view.setVisibility(View.INVISIBLE);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
        LinearLayout teamB = (LinearLayout) mRootView.findViewById(R.id.team_b);
        for (int i = 0; i < teamB.getChildCount(); i++) {
            final int index = i;
            if (teamB.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                        final EditText editTextView = (EditText) linearLayoutChild.getChildAt(j);

                        editTextView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View v, boolean hasFocus) {
                                if (!hasFocus) {
                                    boolean repeated = false;
                                    for (int k = 0; k < mLineUpBToCheck.size(); k++) {
                                        if (mLineUpBToCheck.get(k).equals(editTextView.getText().toString())) {
                                            repeated = true;
                                            // Show alert
                                            ViewGroup row = (ViewGroup) editTextView.getParent();
                                            for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                                                View view = row.getChildAt(itemPos);
                                                if (view instanceof ImageView) {
                                                    view.setVisibility(View.VISIBLE);
                                                    break;
                                                }
                                            }
                                        }
                                    }
                                    if (!repeated) {
                                        mLineUpBToCheck.add(index, editTextView.getText().toString());
                                        ViewGroup row = (ViewGroup) editTextView.getParent();
                                        for (int itemPos = 0; itemPos < row.getChildCount(); itemPos++) {
                                            View view = row.getChildAt(itemPos);
                                            if (view instanceof ImageView) {
                                                view.setVisibility(View.INVISIBLE);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                }
            }
        }
    }

    public boolean checkLineUpRepeated() {
        boolean isRepeated = false;
        for (int i = 0; i < mLineUpAToCheck.size(); i++) {
            if (mLineUpAToCheck.get(i).equals("-1")) {
                isRepeated = true;
            }
        }
        if (!isRepeated) {
            for (int i = 0; i < mLineUpBToCheck.size(); i++) {
                if (mLineUpBToCheck.get(i).equals("-1")) {
                    isRepeated = true;
                }
            }
        }
        return isRepeated;
    }

    public void getLineUp() {
        mLineUpA = new ArrayList<String>();
        mLineUpB = new ArrayList<String>();
        LinearLayout teamA = (LinearLayout) mRootView.findViewById(R.id.team_a);
        for (int i = 0; i < teamA.getChildCount(); i++) {
            if (teamA.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamA.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                        if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                            mLineUpA.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                        }
                    }
                }
            }
        }

        LinearLayout teamB = (LinearLayout) mRootView.findViewById(R.id.team_b);
        for (int i = 0; i < teamB.getChildCount(); i++) {
            if (teamB.getChildAt(i) instanceof LinearLayout) {
                LinearLayout linearLayoutChild = (LinearLayout) teamB.getChildAt(i);
                for (int j = 0; j < linearLayoutChild.getChildCount(); j++) {
                    if (linearLayoutChild.getChildAt(j) instanceof EditText) {
                        if (((EditText) linearLayoutChild.getChildAt(j)).getText().toString().isEmpty() == false) {
                            mLineUpB.add(((EditText) linearLayoutChild.getChildAt(j)).getText().toString());
                        }
                    }
                }
            }
        }

    }

    public ArrayList<String> getCurrentLineUpA() {
        return mLineUpA;
    }

    public ArrayList<String> getCurrentLineUpB() {
        return mLineUpB;
    }

    public SecondPageFragmentListener getSecondPageListener() {
        return secondPageListener;
    }

    @Override
    public void onSwitchToNextFragment() {

    }
}