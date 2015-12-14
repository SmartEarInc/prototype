package com.smartear.smartear.main.fragments;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.databinding.SmartMainButtonsBinding;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class TopPanelFragment extends Fragment {
    private static final String ASSISTANT_PACKAGENAME = "com.nuance.balerion";
    SmartMainButtonsBinding binding;

    public static TopPanelFragment newInstance() {
        Bundle args = new Bundle();
        TopPanelFragment fragment = new TopPanelFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = SmartMainButtonsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.launchAssistant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getActivity().getPackageManager();
                Intent assistantIntent = packageManager.getLaunchIntentForPackage(ASSISTANT_PACKAGENAME);
                try {
                    startActivity(assistantIntent);
                } catch (Exception ignore) {

                }
            }
        });
    }
}
