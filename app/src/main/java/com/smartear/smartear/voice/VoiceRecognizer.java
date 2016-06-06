package com.smartear.smartear.voice;

import android.content.Context;
import android.media.AudioManager;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.smartear.smartear.utils.GoogleSpeechRecognizerHelper;
import com.smartear.smartear.utils.commands.CommandHelper;
import com.smartear.smartear.wechat.WeChatMainActivity;

public class VoiceRecognizer extends Fragment {
    GoogleSpeechRecognizerHelper googleSpeechRecognizerHelper;
    private int recognizeTryCount = 0;
    private Handler speechHandler = new Handler();
    private int REQUEST_AUDIO = 11;

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

    private void stopRecording() {
        if (googleSpeechRecognizerHelper != null) {
            googleSpeechRecognizerHelper.stopListening();
        }
    }

    private void startRecording() {
        googleSpeechRecognizerHelper = new GoogleSpeechRecognizerHelper(getActivity());
        googleSpeechRecognizerHelper.setSpeechRecordingListener(new GoogleSpeechRecognizerHelper.SpeechRecordingListener() {
            @Override
            public void onResults(String text) {
//                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
                getCommandHelper().parseCommand(text);
                stopBtMicrophone();
            }

            @Override
            public void onError(int error) {
                Toast.makeText(getActivity(), "Error: " + error, Toast.LENGTH_LONG).show();
            }
        });
        googleSpeechRecognizerHelper.startListening();
    }

    private void startBtMicrophone() {
        AudioManager am = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_NORMAL);
        am.setSpeakerphoneOn(false);
        am.setBluetoothScoOn(true);
        am.startBluetoothSco();
    }

    private void stopBtMicrophone() {
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

    public void


    startRecognize() {
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

    private CommandHelper getCommandHelper() {
        return ((WeChatMainActivity) getActivity()).getCommandHelper();
    }
}
