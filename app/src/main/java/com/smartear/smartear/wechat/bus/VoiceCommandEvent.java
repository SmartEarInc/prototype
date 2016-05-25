package com.smartear.smartear.wechat.bus;

import com.smartear.smartear.wechat.RecognizedState;

public class VoiceCommandEvent {
    public RecognizedState recognizedState;

    public VoiceCommandEvent(RecognizedState recognizedState) {
        this.recognizedState = recognizedState;
    }
}
