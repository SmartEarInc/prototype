package com.smartear.smartear.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
        categoryTitleModel.action.set(getString(R.string.raePlayer));
        categoryTitleModel.value.set(getString(R.string.runPlaylist));
        model.playerInfo.set(categoryTitleModel);
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
    }
}
