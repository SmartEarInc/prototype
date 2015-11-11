package com.smartear.smartear.viewmodels;

import android.databinding.ObservableField;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 11.11.2015
 */
public class VoiceRecognizerModel {
    public ObservableField<String> recordingButtonText = new ObservableField<>();
    public ObservableField<String> recordingResultText = new ObservableField<>();
    public ObservableField<Boolean> isLoading = new ObservableField<>();
    public ObservableField<State> state = new ObservableField<>();

    public VoiceRecognizerModel() {
        state.set(State.NOT_RECORDING);
        recordingButtonText.set("");
        recordingResultText.set("");
        isLoading.set(false);
    }

    public enum State {
        NOT_RECORDING, INITIALIZING, PROCESSING, RECORDING, COMPLETED, ERROR
    }
}
