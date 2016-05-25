package com.smartear.smartear.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.smartear.smartear.BaseActivity;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public abstract class BaseFragment extends Fragment {

    public static final String ADD_TO_BACK_STACK = "add_to_back_stack";

    public String getFragmentTag() {
        return getClass().getCanonicalName();
    }

    public abstract String getTitle();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void replaceFragment(BaseFragment fragment, boolean addToBackStack) {
        getBaseActivity().replaceFragment(fragment, addToBackStack);
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    private boolean isBackStack() {
        return getArguments().getBoolean(ADD_TO_BACK_STACK);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getSupportActionBar() != null) {
            setTitle(getTitle());
            if (isBackStack()) {
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setHomeButtonEnabled(true);
            } else {
                getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                getSupportActionBar().setDisplayShowHomeEnabled(false);
                getSupportActionBar().setHomeButtonEnabled(false);
            }
        }
    }

    protected void setTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    protected ActionBar getSupportActionBar() {
        return ((AppCompatActivity) getActivity()).getSupportActionBar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getFragmentManager().popBackStack();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
