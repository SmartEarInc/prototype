package com.smartear.smartear.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.ResultReceiver;
import android.support.annotation.Nullable;
import android.support.v4.media.session.MediaSessionCompat;
import android.view.KeyEvent;

import com.smartear.smartear.MainActivity;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 13.11.2015
 */
public class BTService extends Service {
    private static boolean isRunning = false;
    private String TAG = "BTService";
    private MediaSessionCompat mediaSession;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        initMediaSession();
        parseIntent(intent);
        isRunning = true;
        return START_STICKY;
    }

    private void parseIntent(Intent mediaButtonIntent) {
        if (mediaButtonIntent == null)
            return;
        KeyEvent keyEvent = mediaButtonIntent.getParcelableExtra(Intent.EXTRA_KEY_EVENT);
        if (keyEvent != null && keyEvent.getAction() == KeyEvent.ACTION_DOWN && (keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PAUSE || keyEvent.getKeyCode() == KeyEvent.KEYCODE_MEDIA_PLAY)) {
            unlockDevice();
            Intent intent = new Intent(BTService.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(MainActivity.EXTRA_START_RECOGNITION, true);
            startActivity(intent);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            mediaSession.release();
        } catch (Exception ignore) {

        }
        mediaSession = null;
        isRunning = false;
    }

    public static void start(Context context) {
        if (isRunning)
            return;
        context.startService(new Intent(context, BTService.class));
    }

    private void initMediaSession() {
        if (mediaSession != null)
            return;
        mediaSession = new MediaSessionCompat(this, getPackageName() + "." + TAG);
        mediaSession.setCallback(new MediaSessionCompat.Callback() {

            @Override
            public void onCommand(String command, Bundle args, ResultReceiver cb) {
                super.onCommand(command, args, cb);
            }

            @Override
            public void onCustomAction(String action, Bundle extras) {
                super.onCustomAction(action, extras);
            }


            @Override
            public boolean onMediaButtonEvent(Intent mediaButtonIntent) {
                super.onMediaButtonEvent(mediaButtonIntent);
                parseIntent(mediaButtonIntent);
                return true;
            }
        });
        mediaSession.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS | MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setActive(true);

    }

    private void unlockDevice() {
        PowerManager.WakeLock screenLock = ((PowerManager) getSystemService(POWER_SERVICE)).newWakeLock(
                PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
        screenLock.acquire();
        screenLock.release();
    }
}
