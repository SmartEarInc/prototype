package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.databinding.FragmentBottomEqBinding;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class EqFragment extends Fragment {
    FragmentBottomEqBinding binding;

    public static EqFragment newInstance() {

        Bundle args = new Bundle();

        EqFragment fragment = new EqFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBottomEqBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }
}
