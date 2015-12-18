package com.smartear.smartear.main.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentSettingsBinding;
import com.smartear.smartear.main.SmartMainActivity;
import com.smartear.smartear.main.viewmodel.CategoryTitleModel;
import com.smartear.smartear.main.viewmodel.NestedSettingsItemModel;
import com.smartear.smartear.main.viewmodel.NestedSettingsModel;
import com.smartear.smartear.main.viewmodel.SettingsItemModel;
import com.smartear.smartear.main.viewmodel.SettingsModel;
import com.smartear.smartear.main.widget.SettingsAdapter;
import com.smartear.smartear.main.widget.SimpleDividerItemDecoration;
import com.smartear.smartear.utils.SettingsUtils;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

import java.util.List;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class SettingsFragment extends Fragment {
    public static final String TAG = "SettingsFragment";
    FragmentSettingsBinding binding;
    SettingsModel model;
    private SettingsItemModel assistantModel;

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

        String assistantName = getAssistantName();

        assistantModel = new SettingsItemModel(R.id.settingsAssistant, getString(R.string.assistant), assistantName);
        model.settingsList.add(assistantModel);
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
                    case R.id.settingsAssistant:
                        ((SmartMainActivity) getActivity()).replaceFragment(NestedSettingsFragment.newInstance(getAssistantNestedModel()), true);
                        break;
                }
            }
        });
    }

    private NestedSettingsModel getAssistantNestedModel() {
        NestedSettingsModel nestedSettingsModel = new NestedSettingsModel();
        CategoryTitleModel titleModel = new CategoryTitleModel();
        titleModel.title.set(getString(R.string.assistant));
        titleModel.isBack.set(true);
        nestedSettingsModel.title.set(titleModel);
        nestedSettingsModel.settingsKey = SettingsUtils.KEY_ASSISTANT_PACKAGE_NAME;

        SharedPreferences sharedPreferences = SettingsUtils.getPrefs(getActivity());
        String packageName = sharedPreferences.getString(SettingsUtils.KEY_ASSISTANT_PACKAGE_NAME, null);

        PackageManager manager = getActivity().getPackageManager();
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VOICE_COMMAND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> infoList = manager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        int i = 0;
        for (ResolveInfo resolveInfo : infoList) {
            if (!getActivity().getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                nestedSettingsModel.items.add(new NestedSettingsItemModel(
                        (packageName == null && i == 0) || (packageName != null && packageName.equals(resolveInfo.activityInfo.packageName)),
                        resolveInfo.loadLabel(manager).toString(),
                        resolveInfo.activityInfo.packageName
                ));
            }
            i++;
        }
        return nestedSettingsModel;
    }

    private String getAssistantName() {
        PackageManager manager = getActivity().getPackageManager();
        SharedPreferences sharedPreferences = SettingsUtils.getPrefs(getActivity());
        String packageName = sharedPreferences.getString(SettingsUtils.KEY_ASSISTANT_PACKAGE_NAME, null);
        if (!TextUtils.isEmpty(packageName)) {
            try {
                ApplicationInfo ai = manager.getApplicationInfo(packageName, 0);
                if (ai != null) {
                    return manager.getApplicationLabel(ai).toString();
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VOICE_COMMAND);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        List<ResolveInfo> infoList = manager.queryIntentActivities(intent, PackageManager.GET_RESOLVED_FILTER);
        for (ResolveInfo resolveInfo : infoList) {
            if (!getActivity().getPackageName().equals(resolveInfo.activityInfo.packageName)) {
                return resolveInfo.loadLabel(manager).toString();
            }
        }
        return null;
    }
}
