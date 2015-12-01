package com.smartear.smartear;

import android.os.Bundle;
import android.view.MenuItem;

import com.smartear.smartear.fragment.EqualizerFragment;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 30.11.2015
 */
public class EqualizerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equalizer);
        if (savedInstanceState == null) {
            replaceFragment(EqualizerFragment.newInstance(), true);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
