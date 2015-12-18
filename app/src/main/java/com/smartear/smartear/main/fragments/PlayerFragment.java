package com.smartear.smartear.main.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentPlayerBinding;
import com.smartear.smartear.main.SmartMainActivity;
import com.smartear.smartear.main.viewmodel.CategoryTitleModel;
import com.smartear.smartear.main.viewmodel.NestedSettingsItemModel;
import com.smartear.smartear.main.viewmodel.NestedSettingsModel;
import com.smartear.smartear.main.viewmodel.PlayerModel;
import com.smartear.smartear.utils.SettingsUtils;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class PlayerFragment extends Fragment {
    public static final String TAG = "PlayerFragment";
    FragmentPlayerBinding binding;
    PlayerModel model = new PlayerModel();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlayerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public static PlayerFragment newInstance() {
        Bundle args = new Bundle();
        PlayerFragment fragment = new PlayerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        model.currentTime.set("3.55");
        model.totalTime.set("13.20");
        model.progress.set(4);
        model.maxProgress.set(13);
        binding.setData(model);
        binding.playerProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        binding.goToSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NestedSettingsModel model = new NestedSettingsModel();
                CategoryTitleModel titleModel = new CategoryTitleModel();
                titleModel.title.set(getString(R.string.players));
                titleModel.isBack.set(true);
                model.title.set(titleModel);
                model.settingsKey = SettingsUtils.KEY_PLAYER_MODE;

                SharedPreferences preferences = SettingsUtils.getPrefs(getActivity());
                String mode = preferences.getString(SettingsUtils.KEY_PLAYER_MODE, SettingsUtils.MODE_STREAMING);

                model.items.add(new NestedSettingsItemModel(mode.equals(SettingsUtils.MODE_STREAMING), getString(R.string.streaming) + " : " + "Spotify",SettingsUtils.MODE_STREAMING));
                model.items.add(new NestedSettingsItemModel(mode.equals(SettingsUtils.MODE_RAE), getString(R.string.raePlayer) + " : " + getString(R.string.runPlaylist),SettingsUtils.MODE_RAE));
                ((SmartMainActivity) getActivity()).replaceFragment(NestedSettingsFragment.newInstance(model), true);
            }
        });
    }

    private void updateUI() {
        SharedPreferences preferences = SettingsUtils.getPrefs(getActivity());
        String mode = preferences.getString(SettingsUtils.KEY_PLAYER_MODE, SettingsUtils.MODE_STREAMING);
        boolean isRae = mode.equals(SettingsUtils.MODE_RAE);
        CategoryTitleModel categoryTitleModel = new CategoryTitleModel();
        categoryTitleModel.title.set(getString(R.string.player));
        if (isRae) {
            categoryTitleModel.action.set(getString(R.string.raePlayer));
            categoryTitleModel.value.set(getString(R.string.runPlaylist));
            binding.playerPart.setVisibility(View.VISIBLE);
        } else {
            categoryTitleModel.action.set(getString(R.string.streaming));
            categoryTitleModel.value.set("Spotify");
            binding.playerPart.setVisibility(View.GONE);
        }
        model.playerInfo.set(categoryTitleModel);
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }
}
