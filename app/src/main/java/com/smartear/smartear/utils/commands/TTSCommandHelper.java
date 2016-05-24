package com.smartear.smartear.utils.commands;

import android.content.Intent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;

import java.util.HashMap;
import java.util.Map;

public class TTSCommandHelper extends BaseCommandHelper {

    private static final HashMap<String, String> keyAnswer = new HashMap<>();
    private TextToSpeech tts;

    static {
        keyAnswer.put("WEATHER", "The weather is seventy-eight degrees with 15mph winds mostly cloudy.");
        keyAnswer.put("Good morning", "Good Morning Kinu! You have been authenticated.");
        keyAnswer.put("first meeting", "Your first meeting with Dean Garnder at 9am at The Vault");
    }

    public TTSCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        for (final Map.Entry<String, String> entry : keyAnswer.entrySet()) {
            if (text.toUpperCase().contains(entry.getKey().toUpperCase())) {
                tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
                    @Override
                    public void onInit(int i) {
                        tts.speak(entry.getValue(), TextToSpeech.QUEUE_FLUSH, null);
                    }
                });
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
