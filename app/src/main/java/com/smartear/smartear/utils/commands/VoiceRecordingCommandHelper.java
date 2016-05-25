package com.smartear.smartear.utils.commands;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.UtteranceProgressListener;
import android.support.v7.app.AppCompatActivity;

import com.smartear.smartear.utils.FileUtils;
import com.smartear.smartear.wechat.WeChatMainActivity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class VoiceRecordingCommandHelper extends BaseCommandHelper {
    private static final String[] keyWords = {
            "voicechat",
            "voice"
    };
    private static final long SECOND = 1000;
    @SuppressLint("SimpleDateFormat")
    private DateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private MediaRecorder recorder;
    private Handler recordingHandler = new Handler();
    private Runnable recordingRunnable = new Runnable() {
        @Override
        public void run() {
            stopRecording();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((WeChatMainActivity) activity).getMessageHelper().sendFile(Uri.fromFile(new File(filePath)));
                }
            }, 500);
        }
    };
    private Handler startRecordingHandelr = new Handler();
    private Runnable startRecordingRunnable = new Runnable() {
        @Override
        public void run() {
            startRecording();
        }
    };


    private String filePath;

    public VoiceRecordingCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    TextToSpeech tts;

    @Override
    public boolean parseCommand(String text) {
        boolean found = false;
        for (String keyWord : keyWords) {
            if (text.toUpperCase().contains(keyWord.toUpperCase())) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        tts = new TextToSpeech(activity, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setOnUtteranceProgressListener(new UtteranceProgressListener() {
                    @Override
                    public void onStart(String s) {

                    }

                    @Override
                    public void onDone(String s) {
                        startRecordingHandelr.postDelayed(startRecordingRunnable, 300);
                    }

                    @Override
                    public void onError(String s) {

                    }
                });
                HashMap<String, String> params = new HashMap<>();
                params.put(TextToSpeech.Engine.KEY_PARAM_STREAM, String.valueOf(AudioManager.STREAM_ALARM));
                params.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "SOME MESSAGE");
                tts.speak("Ok, Ready", TextToSpeech.QUEUE_FLUSH, params);
            }
        });


        return true;
    }

    public void startRecording() {
        stopRecording();
        Date creationDate = new Date();
        filePath = FileUtils.newFilePath(fileNameFormat.format(creationDate) + ".mp3");
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        recorder.setOutputFile(filePath);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        try {
            recorder.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        recorder.start();

        recordingHandler.postDelayed(recordingRunnable, SECOND * 5);

    }

    public void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (RuntimeException ignore) {

            }
            recorder = null;

        }

    }

    @Override
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        return false;
    }
}
