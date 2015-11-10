package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothAdapter;
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

import java.util.ArrayList;
import java.util.Set;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class StartFragment extends BaseFragment {
    private static final String TAG = "StartFragment";
    FragmentStartBinding binding;
    public static final int STREAM_BLUETOOTH_SCO = 6;
    private SettingsContentObserver settingsContentObserver;

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
        binding.pairDevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new BluetoothDevicesFragment(), true);
            }
        });
        initSoundControl();

    }

    private void initPairedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        Set<BluetoothDevice> deviceSet = bluetoothAdapter.getBondedDevices();
        ArrayList<String> names = new ArrayList<>();
        for (BluetoothDevice device : deviceSet) {
            names.add(device.getName());
        }
        String text = TextUtils.join(", ", names);
        if (TextUtils.isEmpty(text)) {
            binding.pairedDevices.setText(getString(R.string.thereIsNoDevicesYet));
        } else {
            binding.pairedDevices.setText(text);
        }
    }

    private void initSoundControl() {
        final AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = am.getStreamMaxVolume(STREAM_BLUETOOTH_SCO);
        binding.soundSeekBar.setMax(maxVolume);
        binding.soundSeekBar.setProgress(am.getStreamVolume(STREAM_BLUETOOTH_SCO));
        binding.soundSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                am.setStreamVolume(STREAM_BLUETOOTH_SCO, progress, 0);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.mute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                am.setStreamVolume(STREAM_BLUETOOTH_SCO, 0, 0);
            }
        });

    }

    private void updateCurrentVolumeUI(AudioManager am) {
        binding.soundSeekBar.setProgress(am.getStreamVolume(STREAM_BLUETOOTH_SCO));
    }

    private void initVolumeObserver() {
        settingsContentObserver = new SettingsContentObserver(new Handler());
        getActivity().getApplicationContext().getContentResolver().registerContentObserver(Settings.System.CONTENT_URI, true, settingsContentObserver);
    }

    @Override
    public void onResume() {
        super.onResume();
        initVolumeObserver();
        initPairedDevices();
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
