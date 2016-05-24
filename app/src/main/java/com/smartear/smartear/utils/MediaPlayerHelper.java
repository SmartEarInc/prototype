package com.smartear.smartear.utils;

import android.app.Application;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;

import com.smartear.smartear.SmartEarApplication;

public class MediaPlayerHelper {
    private MediaPlayer mediaPlayer;
    Handler uiHandler = new Handler();

    public void startPlaying(final String uri) {

        uiHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer != null) {
                    mediaPlayer.release();
                }

                mediaPlayer = MediaPlayer.create(SmartEarApplication.getContext(), Uri.parse(uri));
                mediaPlayer.start();
            }
        }, 100);
    }
}
