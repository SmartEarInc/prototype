package com.smartear.smartear.voice;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.app.Fragment;

import com.smartear.smartear.utils.commands.CommandHelper;
import com.smartear.smartear.wechat.WeChatMainActivity;

/**
 * Created: Belozerov Sergei
 * E-mail: belozerow@gmail.com
 * Company: APPGRANULA LLC
 * Date: 06/07/16
 */
public abstract class BaseVoiceRecognizer extends Fragment {

    @Override
    public void onResume() {
        super.onResume();
    }

    private boolean isBtMicrophoneOn() {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        return am.isBluetoothScoOn();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (getActivity() instanceof WeChatMainActivity) {
            WeChatMainActivity activity = (WeChatMainActivity) getActivity();
            if (activity.isStartRecordingOnResume()) {
                activity.setStartRecordingOnResume(false);
                startRecognize();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopRecording();
    }

    public abstract void stopRecording();

    public abstract void startRecording();

    public void startBtMicrophone() {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_NORMAL);
        am.setSpeakerphoneOn(false);
        am.setBluetoothScoOn(true);
        am.startBluetoothSco();
    }

    public void stopBtMicrophone() {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        am.setSpeakerphoneOn(true);
        am.stopBluetoothSco();
    }

    private Handler startRecognizeHandler = new Handler();
    private Runnable startRecognizeRunnable = new Runnable() {
        @Override
        public void run() {
            startRecognizeImmediately();
        }
    };

    public void startRecognize() {
        ((WeChatMainActivity) getActivity()).requestAudioFocus();
        if (!isBtMicrophoneOn()) {
            startBtMicrophone();
        }
        startRecognizeHandler.removeCallbacks(startRecognizeRunnable);
        startRecognizeHandler.postDelayed(startRecognizeRunnable, 1000);
    }

    public void startRecognizeImmediately() {
        startRecording();
    }

    protected CommandHelper getCommandHelper() {
        return ((WeChatMainActivity) getActivity()).getCommandHelper();
    }
}
