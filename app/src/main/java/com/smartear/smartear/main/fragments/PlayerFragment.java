package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentPlayerBinding;
import com.smartear.smartear.main.viewmodel.CategoryTitleModel;
import com.smartear.smartear.main.viewmodel.PlayerModel;

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
        CategoryTitleModel categoryTitleModel = new CategoryTitleModel();
        categoryTitleModel.title.set(getString(R.string.player));
        categoryTitleModel.action.set(getString(R.string.streaming));
        categoryTitleModel.value.set("Spotify");
        model.playerInfo.set(categoryTitleModel);

        binding.setData(model);
    }
}
