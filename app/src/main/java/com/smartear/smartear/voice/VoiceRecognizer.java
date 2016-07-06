package com.smartear.smartear.voice;

import android.widget.Toast;

import com.smartear.smartear.utils.GoogleSpeechRecognizerHelper;

public class VoiceRecognizer extends BaseVoiceRecognizer {
    GoogleSpeechRecognizerHelper googleSpeechRecognizerHelper;

    @Override
    public void stopRecording() {
        if (googleSpeechRecognizerHelper != null) {
            googleSpeechRecognizerHelper.stopListening();
        }
    }

    @Override
    public void startRecording() {
        googleSpeechRecognizerHelper = new GoogleSpeechRecognizerHelper(getActivity());
        googleSpeechRecognizerHelper.setSpeechRecordingListener(new GoogleSpeechRecognizerHelper.SpeechRecordingListener() {
            @Override
            public void onResults(String text) {
                Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
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
}
