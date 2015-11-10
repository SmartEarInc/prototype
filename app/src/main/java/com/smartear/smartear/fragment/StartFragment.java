package com.smartear.smartear.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentStartBinding;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class StartFragment extends BaseFragment {
    private static final String TAG = "StartFragment";
    FragmentStartBinding binding;

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return getString(R.string.app_name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.pairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BluetoothDevicesFragment(), true);
            }
        });
    }
}
