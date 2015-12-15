package com.smartear.smartear.main.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.databinding.SmartMainButtonsBinding;
import com.smartear.smartear.main.viewmodel.TopPanelModel;
import com.smartear.smartear.utils.MuteHelper;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class TopPanelFragment extends Fragment {
    private static final String ASSISTANT_PACKAGENAME = "com.nuance.balerion";
    SmartMainButtonsBinding binding;
    MuteHelper muteHelper = new MuteHelper(SmartEarApplication.getContext());
    public static final int BT_STREAM = AudioManager.STREAM_MUSIC;

    private SettingsContentObserver settingsContentObserver;
    private TopPanelModel model;

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
        model = new TopPanelModel();
        binding.setViewModel(model);
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
        binding.muteSound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (muteHelper.isMute()) {
                    muteHelper.unmuteSound();
                    model.isMute.set(false);
                } else {
                    muteHelper.muteSound();
                    model.isMute.set(true);
                }
            }
        });

        initSoundControl();
    }

    private void initSoundControl() {
        final AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        updateCurrentVolumeUI(am);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.soundSeekbar:
                        am.setStreamVolume(BT_STREAM, progress, 0);
                        break;
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        };

        binding.soundSeekbar.setOnSeekBarChangeListener(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        initVolumeObserver();
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getApplicationContext().getContentResolver().unregisterContentObserver(settingsContentObserver);
    }

    private void initVolumeObserver() {
        settingsContentObserver = new SettingsContentObserver(new Handler());
        getActivity().getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, settingsContentObserver);
    }

    private class SettingsContentObserver extends ContentObserver {
        public SettingsContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            updateCurrentVolumeUI((AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE));
        }
    }

    private void updateCurrentVolumeUI(AudioManager am) {
        updateCurrentVolumeUI(am, BT_STREAM);
        model.isMute.set(muteHelper.isMute());
    }

    private void updateCurrentVolumeUI(AudioManager am, int stream) {
        int maxVolume = am.getStreamMaxVolume(stream);
        switch (stream) {
            case BT_STREAM:
                model.soundMaxVolume.set(maxVolume);
                model.soundVolume.set(am.getStreamVolume(stream));
                break;
        }
    }
}
