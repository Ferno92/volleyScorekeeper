package com.provehitoIA.ferno92.volleyscorekeeper.matchInfos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

/**
 * Created by lucas on 27/11/2016.
 */

public class EditLineUpFragment extends Fragment {
    public EditLineUpFragment(){
    }
        @Override
        public View onCreateView(LayoutInflater inflater,
                                 ViewGroup container, Bundle savedInstanceState){
            View rootView = inflater.inflate(R.layout.edit_line_up,
                    container, false);

            return rootView;
        }

}
