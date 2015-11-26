package com.smartear.smartear.utils;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 23.11.2015
 */
public class GoogleSpeechRecognizerHelper implements RecognitionListener {
    private static final String TAG = "SpeechRecognizerHelper";
    private SpeechRecognizer speechRecognizer;

    private Context context;

    public GoogleSpeechRecognizerHelper(Context context) {
        this.context = context;
    }

    private SpeechRecordingListener speechRecordingListener;

    public void setSpeechRecordingListener(SpeechRecordingListener speechRecordingListener) {
        this.speechRecordingListener = speechRecordingListener;
    }

    public void startListening() {
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
        }
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        speechRecognizer.setRecognitionListener(this);
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);
        speechRecognizer.startListening(intent);
    }

    public void stopListening() {
        speechRecognizer.stopListening();
    }

    public interface SpeechRecordingListener {
        void onResults(String text);
        void onError(int error);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        Log.i(TAG, "onReadyForSpeech");
        if(!isBtMicrophoneOn())
            startBtMicrophone();
    }

    @Override
    public void onBeginningOfSpeech() {
        Log.i(TAG, "onBeginningOfSpeech");
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        Log.i(TAG, "onRmsChanged");
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        Log.i(TAG, "onBufferReceived");

    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech");
    }

    @Override
    public void onError(int error) {
        Log.i(TAG, "onError");
        if(error == SpeechRecognizer.ERROR_NO_MATCH || speechRecordingListener == null)
            return;
        speechRecordingListener.onError(error);
    }

    @Override
    public void onResults(Bundle results) {
        if (speechRecordingListener == null)
            return;
        ArrayList<String> data = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
        if (data != null && !data.isEmpty()) {
            speechRecordingListener.onResults(data.get(0));
        }
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        Log.i(TAG, "onPartialResults");
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        Log.i(TAG, "onEvent");
    }

    private void startBtMicrophone() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        am.setMode(AudioManager.MODE_NORMAL);
        am.setSpeakerphoneOn(false);
        am.setBluetoothScoOn(true);
        am.startBluetoothSco();
    }

    private boolean isBtMicrophoneOn() {
        AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        return am.isBluetoothScoOn();
    }
}
