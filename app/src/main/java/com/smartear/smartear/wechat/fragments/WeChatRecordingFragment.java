package com.smartear.smartear.wechat.fragments;

import android.annotation.SuppressLint;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.smartear.smartear.R;
import com.smartear.smartear.utils.FileUtils;
import com.smartear.smartear.wechat.RecognizedState;
import com.smartear.smartear.wechat.WeChatMainActivity;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class WeChatRecordingFragment extends WeChatBaseFragment {
    private static final long SECOND = 1000;
    @SuppressLint("SimpleDateFormat")
    private DateFormat fileNameFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
    private MediaRecorder recorder;
    private Handler recordingHandler = new Handler();
    private Runnable recordingRunnable = new Runnable() {
        @Override
        public void run() {
            stopRecording();
            statusText.setText(R.string.sent);
            statusIcon.setImageResource(R.drawable.sent);
            seekBarContainer.setVisibility(View.GONE);
            sayText(getContext(), RecognizedState.SENT);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((WeChatMainActivity) getActivity()).getMessageHelper().sendFile(Uri.fromFile(new File(filePath)));
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
    private SeekBar recordingProgress;
    private Timer timer;
    private ImageView statusIcon;
    private TextView statusText;
    private View seekBarContainer;
    private View retrievingView;
    private View recordingView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.wechat_recording_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recordingProgress = (SeekBar) view.findViewById(R.id.recordingProgress);
        recordingProgress.setMax((int) (RECORDING_TIME / SECOND));
        seekBarContainer = view.findViewById(R.id.seekbarContainer);
        statusIcon = (ImageView) view.findViewById(R.id.statusIcon);
        statusText = (TextView) view.findViewById(R.id.statusText);
        recordingView = view.findViewById(R.id.recording);
        retrievingView = view.findViewById(R.id.retrieving);
        sayText(getWeChatActivity(),RecognizedState.VOICE_MESSAGE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recordingView.setVisibility(View.VISIBLE);
                retrievingView.setVisibility(View.GONE);
                startRecording();
            }
        }, 5000);
    }

    @Override
    public String getTitle() {
        return null;
    }

    private static final long RECORDING_TIME = SECOND * 7;

    public void startRecording() {
        getVoiceRecognizer().startBtMicrophone();
        stopRecording();
        Date creationDate = new Date();
        filePath = FileUtils.newFilePath(fileNameFormat.format(creationDate) + ".mp3");
        try {
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
        } catch (Exception e){
            e.printStackTrace();
        }

        recordingHandler.postDelayed(recordingRunnable, RECORDING_TIME);

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                recordingProgress.setProgress(++progress);
                if (progress >= recordingProgress.getMax()) {
                    timer.cancel();
                }
            }
        }, 0, SECOND);
    }

    private int progress = 0;

    public void stopRecording() {
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.release();
            } catch (RuntimeException ignore) {

            }
            recorder = null;
            try {
                timer.cancel();
            } catch (Exception e) {

            }
        }
        getVoiceRecognizer().stopBtMicrophone();

    }
}
