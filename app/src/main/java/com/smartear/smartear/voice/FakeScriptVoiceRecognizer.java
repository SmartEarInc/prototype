package com.smartear.smartear.voice;

import android.media.MediaPlayer;
import android.os.Handler;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.bus.VoiceCommandEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class FakeScriptVoiceRecognizer extends BaseVoiceRecognizer {
    private int currentPosition = 0;
    private final ArrayList<RecognitionStep> stateList = new ArrayList<>();

    {
        stateList.add(new RecognitionStep(3500, RecognizedState.AUTH));
        stateList.add(new RecognitionStep(4000, RecognizedState.WEATHER));
        stateList.add(new RecognitionStep(4500, RecognizedState.MEETING));
        stateList.add(new RecognitionStep(3500, RecognizedState.VOICE_MESSAGE));
        stateList.add(new RecognitionStep(3000, RecognizedState.MUSIC));
        stateList.add(new RecognitionStep(3500, RecognizedState.PAUSE_MUSIC));
        stateList.add(new RecognitionStep(5000, RecognizedState.DIDI));
        stateList.add(new RecognitionStep(3500, RecognizedState.RESUME_MUSIC));
    }

    @Override
    public void stopRecording() {

    }

    @Override
    public void startRecording() {
        if (currentPosition >= stateList.size())
            return;
        final RecognitionStep step = stateList.get(currentPosition);
        MediaPlayer mediaPlayer = MediaPlayer.create(SmartEarApplication.getContext(), R.raw.beep);
        mediaPlayer.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                EventBus.getDefault().post(new VoiceCommandEvent(step.getState()));
            }
        }, step.getDelay());

        currentPosition++;
    }

    public static class RecognitionStep {
        private long delay;
        private RecognizedState state;

        public RecognitionStep(long delay, RecognizedState state) {
            this.delay = delay;
            this.state = state;
        }

        public long getDelay() {
            return delay;
        }

        public void setDelay(long delay) {
            this.delay = delay;
        }

        public RecognizedState getState() {
            return state;
        }

        public void setState(RecognizedState state) {
            this.state = state;
        }
    }
}
