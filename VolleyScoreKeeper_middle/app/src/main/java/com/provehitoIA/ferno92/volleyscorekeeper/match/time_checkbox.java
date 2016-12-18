package com.provehitoIA.ferno92.volleyscorekeeper.match;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.CheckBox;

import com.provehitoIA.ferno92.volleyscorekeeper.R;

/**
 * Created by lucas on 29/10/2016.
 */

public class time_checkbox extends CheckBox {



    public time_checkbox(Context context, AttributeSet attrs) {
        super(context, attrs);
        //setButtonDrawable(new StateListDrawable());
    }
    @Override
    public void setChecked(boolean t){
        if(t)
        {
            this.setBackgroundResource(R.drawable.time_selected);
        }
        else
        {
            this.setBackgroundResource(R.drawable.time_unselected);
        }
        super.setChecked(t);
    }
}