package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentBionicBinding;
import com.smartear.smartear.main.viewmodel.BionicModel;
import com.smartear.smartear.main.viewmodel.CategoryTitleModel;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class BionicFragment extends Fragment {
    public static final String TAG = "BionicFragment";
    FragmentBionicBinding binding;
    BionicModel model;

    public static BionicFragment newInstance() {
        Bundle args = new Bundle();
        BionicFragment fragment = new BionicFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentBionicBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new BionicModel();
        binding.setData(model);

        binding.left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.isLeftSelected.set(true);
            }
        });

        binding.right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                model.isLeftSelected.set(false);
            }
        });

        CategoryTitleModel presets = new CategoryTitleModel();
        presets.title.set(getString(R.string.presets));
        presets.action.set(getString(R.string.prescription));
        presets.value.set("Dr. Lang");
        model.presets.set(presets);
    }
}
