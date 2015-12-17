package com.smartear.smartear.main.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.databinding.FragmentNestedSettingsBinding;
import com.smartear.smartear.databinding.ItemNestedSettingBinding;
import com.smartear.smartear.main.viewmodel.NestedSettingsItemModel;
import com.smartear.smartear.main.viewmodel.NestedSettingsModel;
import com.smartear.smartear.main.widget.SimpleDividerItemDecoration;
import com.smartear.smartear.utils.PrefsUtils;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public class NestedSettingsFragment extends BaseSmartFragment {
    private static final String ARG_NESTING_SETTINGS = "arg_nesting_settings";
    FragmentNestedSettingsBinding binding;
    NestedSettingsModel model;

    public static NestedSettingsFragment newInstance(NestedSettingsModel nesting) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_NESTING_SETTINGS, nesting);
        NestedSettingsFragment fragment = new NestedSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentNestedSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = (NestedSettingsModel) getArguments().getSerializable(ARG_NESTING_SETTINGS);
        binding.setData(model);

        final RecyclerViewAdapterBase<NestedSettingsItemModel, ItemNestedSettingBinding> adapterBase = new RecyclerViewAdapterBase<NestedSettingsItemModel, ItemNestedSettingBinding>() {
            @Override
            public ItemNestedSettingBinding inflateItem(LayoutInflater inflater, ViewGroup parent, int viewType) {
                return ItemNestedSettingBinding.inflate(inflater, parent, false);
            }
        };

        adapterBase.setItems(model.items);
        adapterBase.setOnItemClickListener(new RecyclerViewAdapterBase.OnItemClickListener<NestedSettingsItemModel>() {
            @Override
            public void onItemClick(int position, NestedSettingsItemModel item) {
                for (NestedSettingsItemModel nestedSettingsItemModel : adapterBase.getItems()) {
                    nestedSettingsItemModel.isChecked.set(false);
                }
                item.isChecked.set(true);
                SharedPreferences sharedPreferences = PrefsUtils.getPrefs(getActivity());
                sharedPreferences.edit().putInt(model.settingsKey, position).apply();
            }
        });
        binding.recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        binding.recyclerView.setAdapter(adapterBase);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        binding.title.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public String getFragmentTag() {
        return "NestedSettingsFragment";
    }
}
