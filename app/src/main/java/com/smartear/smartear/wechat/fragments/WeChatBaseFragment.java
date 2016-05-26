package com.smartear.smartear.wechat.fragments;

import android.speech.tts.TextToSpeech;

import com.smartear.smartear.fragment.BaseFragment;
import com.smartear.smartear.voice.VoiceRecognizer;
import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.WeChatMainActivity;

import java.util.HashMap;

public abstract class WeChatBaseFragment extends BaseFragment {


    private HashMap<RecognizedState, String> ttsMap = new HashMap<>();

    {
        ttsMap.put(RecognizedState.AUTH, "Good morning. You are authenticated.");
        ttsMap.put(RecognizedState.WEATHER, "The weather is seventy-eight degrees with 11mph. Partly cloudy.");
        ttsMap.put(RecognizedState.MEETING, "Your first meeting is with Dean Gardner at 9am at the Vault");
        ttsMap.put(RecognizedState.VOICE_MESSAGE, "Ok, ready");
    }

    protected WeChatMainActivity getWeChatActivity() {
        return (WeChatMainActivity) getActivity();
    }

    protected VoiceRecognizer getVoiceRecognizer() {
        return getWeChatActivity().getVoiceRecognizer();
    }

    private TextToSpeech tts;

    protected void sayText(final RecognizedState recognizedState) {
        if (ttsMap.get(recognizedState) != null) {
            tts = new TextToSpeech(getActivity(), new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int i) {
                    tts.speak(ttsMap.get(recognizedState), TextToSpeech.QUEUE_FLUSH, new HashMap<String, String>());
                }
            });
        }
    }

    public void pauseMusic() {

    }

    public void resumeMusic() {

    }
}
