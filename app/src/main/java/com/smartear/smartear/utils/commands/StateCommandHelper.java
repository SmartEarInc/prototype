package com.smartear.smartear.utils.commands;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.bus.VoiceCommandEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

public class StateCommandHelper extends BaseCommandHelper {

    private static final HashMap<String, RecognizedState> keyAnswer = new HashMap<>();

    static {
        keyAnswer.put("WEATHER", RecognizedState.WEATHER);
        keyAnswer.put("Good morning", RecognizedState.AUTH);
        keyAnswer.put("first meeting", RecognizedState.MEETING);
        keyAnswer.put("meeting", RecognizedState.MEETING);
        keyAnswer.put("voice", RecognizedState.VOICE_MESSAGE);
        keyAnswer.put("play", RecognizedState.MUSIC);
        keyAnswer.put("didi", RecognizedState.DIDI);
        keyAnswer.put("get me a", RecognizedState.DIDI);
        keyAnswer.put("pause", RecognizedState.PAUSE_MUSIC);
        keyAnswer.put("boost", RecognizedState.PAUSE_MUSIC);
        keyAnswer.put("bose", RecognizedState.PAUSE_MUSIC);
        keyAnswer.put("folk", RecognizedState.PAUSE_MUSIC);
        keyAnswer.put("ghost", RecognizedState.PAUSE_MUSIC);
        keyAnswer.put("resume", RecognizedState.RESUME_MUSIC);
        keyAnswer.put("review", RecognizedState.RESUME_MUSIC);
    }

    public StateCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        for (final Map.Entry<String, RecognizedState> entry : keyAnswer.entrySet()) {
            if (text.toUpperCase().contains(entry.getKey().toUpperCase())) {
                EventBus.getDefault().post(new VoiceCommandEvent(entry.getValue()));
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
