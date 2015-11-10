package com.smartear.smartear;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.smartear.smartear.fragment.StartFragment;

public class MainActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            replaceFragment(new StartFragment(), false);
        }
    }
}
