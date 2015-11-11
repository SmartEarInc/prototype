package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.FragmentStartBinding;
import com.smartear.smartear.utils.MuteHelper;
import com.smartear.smartear.viewmodels.StartFragmentModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class StartFragment extends BaseBluetoothFragment {
    private static final String TAG = "StartFragment";
    FragmentStartBinding binding;

    private SettingsContentObserver settingsContentObserver;
    private StartFragmentModel startFragmentModel = new StartFragmentModel();
    private MuteHelper muteHelper;

    @Override
    public String getFragmentTag() {
        return TAG;
    }

    @Override
    public String getTitle() {
        return getString(R.string.app_name);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentStartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setViewModel(startFragmentModel);
        binding.pairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BluetoothDevicesFragment(), true);
            }
        });
        muteHelper = new MuteHelper(getActivity());
        initSoundControl();
        startFragmentModel.isMute.set(muteHelper.isMute());
    }


    private void initSoundControl() {
        final AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        updateCurrentVolumeUI(am);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                switch (seekBar.getId()) {
                    case R.id.musicSeekbar:
                        am.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                        break;
                    case R.id.ringSeekbar:
                        am.setStreamVolume(AudioManager.STREAM_RING, progress, 0);
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

        binding.musicSeekbar.setOnSeekBarChangeListener(listener);
        binding.ringSeekbar.setOnSeekBarChangeListener(listener);

        binding.mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (muteHelper.isMute()) {
                    muteHelper.unmuteSound();
                } else {
                    muteHelper.muteSound();
                }
            }
        });

    }

    private void updateCurrentVolumeUI(AudioManager am) {
        updateCurrentVolumeUI(am, AudioManager.STREAM_MUSIC);
        updateCurrentVolumeUI(am, AudioManager.STREAM_RING);
        startFragmentModel.isMute.set(muteHelper.isMute());
    }

    private void updateCurrentVolumeUI(AudioManager am, int stream) {
        int maxVolume = am.getStreamMaxVolume(stream);
        switch (stream){
            case AudioManager.STREAM_MUSIC:
                startFragmentModel.musicMaxVolumeLevel.set(maxVolume);
                startFragmentModel.musicVolumeLevel.set(am.getStreamVolume(stream));
                break;
            case AudioManager.STREAM_RING:
                startFragmentModel.ringMaxVolumeLevel.set(maxVolume);
                startFragmentModel.ringVolumeLevel.set(am.getStreamVolume(stream));
                break;
        }

    }

    private void initVolumeObserver() {
        settingsContentObserver = new SettingsContentObserver(new Handler());
        getActivity().getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, settingsContentObserver);
    }

    @Override
    protected void onDeviceDisconnected(BluetoothDevice device) {
        updateConnectedDevices();
    }

    @Override
    protected void onDeviceConnected(BluetoothDevice device) {
        updateConnectedDevices();
    }

    @Override
    public void onDeviceFound(BluetoothDevice device) {
    }

    @Override
    public void onDevicePaired(BluetoothDevice device) {
        updateConnectedDevices();
    }

    @Override
    public void onDeviceUnPaired(BluetoothDevice device) {
        updateConnectedDevices();
    }

    @Override
    public void onResume() {
        super.onResume();
        initVolumeObserver();
    }

    @Override
    protected void updateConnectedDevices(List<BluetoothDevice> connectedDevices) {
        ArrayList<String> names = new ArrayList<>();
        for (BluetoothDevice device : connectedDevices) {
            names.add(!TextUtils.isEmpty(device.getName()) ? device.getName() : device.toString());
        }
        String text = TextUtils.join(", ", names);
        if (TextUtils.isEmpty(text)) {
            text = getString(R.string.thereIsNoDevicesYet);
        }
        startFragmentModel.devicesText.set(text);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().getApplicationContext().getContentResolver().unregisterContentObserver(settingsContentObserver);
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

}
