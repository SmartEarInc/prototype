package com.smartear.smartear.utils.commands;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.utils.FileUtils;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class VoiceRecordingCommandHelper extends BaseCommandHelper {
    private static final String[] keyWords = {
            "WE CHAT",
            "WECHAT"
    };
    private static final long SECOND = 1000;
    @SuppressLint("SimpleDateFormat")
    private DateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    Handler uiHandler = new Handler();
    private MediaRecorder recorder;
    private MediaPlayer mediaPlayer;
    private Handler recordingHandler = new Handler();
    private Runnable recordingRunnable = new Runnable() {
        @Override
        public void run() {
            stopRecording();
            startPlaying();
        }
    };
    private Handler startRecordingHandelr = new Handler();
    private Runnable startRecordingRunnable = new Runnable() {
        @Override
        public void run() {
            startRecording();
        }
    };

    private void startPlaying() {

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                mediaPlayer = MediaPlayer.create(activity, Uri.parse(filePath));
                mediaPlayer.start();
            }
        }, 500);
    }

    private String filePath;

    public VoiceRecordingCommandHelper(AppCompatActivity activity) {
        super(activity);
    }

    @Override
    public boolean parseCommand(String text) {
        boolean found = false;
        for (String keyWord : keyWords) {
            if (text.toUpperCase().contains(keyWord)) {
                found = true;
                break;
            }
        }
        if (!found) {
            return false;
        }

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(SmartEarApplication.getContext(), notification);
        r.play();
        startRecordingHandelr.postDelayed(startRecordingRunnable, 300);

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
