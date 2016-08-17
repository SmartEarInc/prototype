package com.smartear.smartear.wechat.fragments;

import android.content.Context;
import android.speech.tts.TextToSpeech;

import com.smartear.smartear.fragment.BaseFragment;
import com.smartear.smartear.voice.BaseVoiceRecognizer;
import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.WeChatMainActivity;

import java.util.HashMap;

public abstract class WeChatBaseFragment extends BaseFragment {


    private static HashMap<RecognizedState, String> ttsMap = new HashMap<>();

    {
        ttsMap.put(RecognizedState.AUTH, "Good morning. You are authenticated.");
        ttsMap.put(RecognizedState.WEATHER, "The weather is seventy-eight degrees with 11mph. Partly cloudy.");
        ttsMap.put(RecognizedState.MEETING, "Your first meeting is with Dean Gardner at 9am at the Vault");
        ttsMap.put(RecognizedState.VOICE_MESSAGE, "Ok, ready");
        ttsMap.put(RecognizedState.MUSIC, "Ok, playing Lady Gaga");
        ttsMap.put(RecognizedState.PAUSE_MUSIC, "Ok, pause music");
        ttsMap.put(RecognizedState.RESUME_MUSIC, "Ok, playing Lady Gaga");
        ttsMap.put(RecognizedState.SENT, "Message sent");
        ttsMap.put(RecognizedState.CONFIRM_LOCATION, "Ok, getting Uber. Confirm your location at 250 Sutter Street");
        ttsMap.put(RecognizedState.CONFIRMED, "Your Uber driver Daisy Lee will be here in 5 minutes");
        ttsMap.put(RecognizedState.DIDI, "Getting Uber to the Vault of 415 Jackson in San Francisco");
    }

    protected WeChatMainActivity getWeChatActivity() {
        return (WeChatMainActivity) getActivity();
    }

    protected BaseVoiceRecognizer getVoiceRecognizer() {
        return getWeChatActivity().getVoiceRecognizer();
    }

    private static TextToSpeech tts;

    public static void sayText(Context context, final RecognizedState recognizedState) {
        if (ttsMap.get(recognizedState) != null) {
            tts = new TextToSpeech(context, new TextToSpeech.OnInitListener() {
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
