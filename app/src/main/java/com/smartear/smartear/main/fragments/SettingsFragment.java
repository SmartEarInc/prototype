package com.smartear.smartear.main.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentSettingsBinding;
import com.smartear.smartear.main.viewmodel.SettingsItemModel;
import com.smartear.smartear.main.viewmodel.SettingsModel;
import com.smartear.smartear.main.widget.SettingsAdapter;
import com.smartear.smartear.main.widget.SimpleDividerItemDecoration;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    FragmentSettingsBinding binding;
    SettingsModel model;

    public static SettingsFragment newInstance() {

        Bundle args = new Bundle();

        SettingsFragment fragment = new SettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        model = new SettingsModel();
        binding.setData(model);

        model.settingsList.add(new SettingsItemModel(R.id.settingsAssistant, getString(R.string.assistant), "Nuance"));
        model.settingsList.add(new SettingsItemModel(R.id.chat, getString(R.string.chat), "WeChat"));
        model.settingsList.add(new SettingsItemModel(R.id.music, getString(R.string.music), "Spotify"));
        model.settingsList.add(new SettingsItemModel(R.id.pairing, getString(R.string.pairing), null));
        model.settingsList.add(new SettingsItemModel(R.id.tutorial, getString(R.string.tutorial), null));

        SettingsAdapter settingsAdapter = new SettingsAdapter();
        settingsAdapter.setItems(model.settingsList);
        settingsAdapter.setHasStableIds(true);
        binding.settingsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.settingsRecyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        binding.settingsRecyclerView.setAdapter(settingsAdapter);

        settingsAdapter.setOnItemClickListener(new RecyclerViewAdapterBase.OnItemClickListener<SettingsItemModel>() {
            @Override
            public void onItemClick(int position, SettingsItemModel item) {
                switch (item.id.get()) {
                    case R.id.pairing:
                        Intent settingsIntent = new Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                        startActivity(settingsIntent);
                        break;
                }
            }
        });
    }
}
