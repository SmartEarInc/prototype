package com.smartear.smartear.fragment;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.Observable;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuance.nmdp.speechkit.Prompt;
import com.nuance.nmdp.speechkit.Recognition;
import com.nuance.nmdp.speechkit.Recognizer;
import com.nuance.nmdp.speechkit.SpeechError;
import com.nuance.nmdp.speechkit.SpeechKit;
import com.smartear.smartear.MainActivity;
import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.databinding.FragmentVoiceRecognizerBinding;
import com.smartear.smartear.speechkit.AppInfo;
import com.smartear.smartear.viewmodels.VoiceRecognizerModel;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 11.11.2015
 */
public class VoiceRecognizeFragment extends Fragment {
    private static final String TAG = "VoiceRecognizeFragment";
    FragmentVoiceRecognizerBinding binding;
    VoiceRecognizerModel data = new VoiceRecognizerModel();

    public static VoiceRecognizeFragment newInstance(boolean startRecognize) {
        Bundle args = new Bundle();
        args.putBoolean(MainActivity.EXTRA_START_RECOGNITION, startRecognize);
        VoiceRecognizeFragment fragment = new VoiceRecognizeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private Recognizer.Listener speechListener = new Recognizer.Listener() {
        @Override
        public void onRecordingBegin(Recognizer recognizer) {
            data.state.set(VoiceRecognizerModel.State.RECORDING);
        }

        @Override
        public void onRecordingDone(Recognizer recognizer) {
            data.state.set(VoiceRecognizerModel.State.PROCESSING);
        }

        @Override
        public void onResults(Recognizer recognizer, Recognition results) {
            int count = results.getResultCount();
            if (count > 0) {
                data.recordingResultText.set(results.getResult(0).getText());
            }
            data.state.set(VoiceRecognizerModel.State.COMPLETED);
            data.recordingButtonText.set(getString(R.string.startRecording));
        }

        @Override
        public void onError(Recognizer recognizer, SpeechError speechError) {
            if (getActivity() == null)
                return;
            data.state.set(VoiceRecognizerModel.State.ERROR);
            data.recordingResultText.set(speechError.getErrorCode() + "\n" + speechError.getErrorDetail() + "\n" + speechError.getSuggestion());
            data.recordingButtonText.set(getString(R.string.startRecording));
        }
    };
    private Handler speechHandler = new Handler();
    private Recognizer recognizer;
    private int REQUEST_AUDIO = 11;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentVoiceRecognizerBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.setData(data);
        data.recordingButtonText.set(getString(R.string.startRecording));
        data.state.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                switch (data.state.get()) {
                    case INITIALIZING:
                        startBtMicrophone();
                        break;
                    case ERROR:
                    case COMPLETED:
                    case NOT_RECORDING:
                        stopBtMicrophone();
                        break;
                }
            }
        });
        binding.recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRecognizeImmediately();
            }
        });

        requestRecordPermission();

        if (getArguments() != null && getArguments().getBoolean(MainActivity.EXTRA_START_RECOGNITION, false)) {
            startRecognize();
        }
    }

    private void requestRecordPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            requestPermissions(new String[]{android.Manifest.permission.RECORD_AUDIO}, REQUEST_AUDIO);
        }
    }

    private void stopRecording() {
        if (recognizer != null)
            recognizer.stopRecording();
        data.state.set(VoiceRecognizerModel.State.NOT_RECORDING);
    }

    private void startRecording() {
        recognizer = SmartEarApplication.getSpeechKit().createRecognizer(Recognizer.RecognizerType.Dictation, Recognizer.EndOfSpeechDetection.Long, "en_US", speechListener, speechHandler);
        data.recordingButtonText.set(getString(R.string.stopRecording));
        data.state.set(VoiceRecognizerModel.State.INITIALIZING);
        recognizer.start();
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

    @Override
    public void onDetach() {
        super.onDetach();
        if (recognizer != null) {
            recognizer.cancel();
        }
    }

    public void startRecognize() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startRecognizeImmediately();
            }
        }, 3000);
    }

    public void startRecognizeImmediately() {
        switch (data.state.get()) {
            case NOT_RECORDING:
            case ERROR:
            case COMPLETED:
                startRecording();
                break;
            default:
                stopRecording();
                break;
        }
    }
}
