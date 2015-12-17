package com.smartear.smartear.main.fragments;

import android.support.v4.app.Fragment;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public abstract class BaseSmartFragment extends Fragment {
    public static final String ADD_TO_BACK_STACK = "add_to_back_stack";

    public boolean onBackPressed() {
        return false;
    }

    public abstract String getFragmentTag();

    private boolean isBackStack() {
        return getArguments().getBoolean(ADD_TO_BACK_STACK);
    }
}
