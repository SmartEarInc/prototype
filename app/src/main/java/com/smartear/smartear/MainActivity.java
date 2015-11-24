package com.smartear.smartear;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;

import com.smartear.smartear.fragment.StartFragment;
import com.smartear.smartear.fragment.VoiceRecognizeFragment;
import com.smartear.smartear.services.BTService;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_MEDIA_BUTTON_CODE = 11;
    private static final int REQUEST_PERMISSION = 12;
    public static String EXTRA_START_RECOGNITION = "EXTRA_START_RECOGNITION";
    private AudioManager audioManager;
    private ComponentName receiverComponent;
    private PendingIntent pendingIntent;
    private boolean audioFocusGranted = false;
    private AudioManager.OnAudioFocusChangeListener audioFocusListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            switch (focusChange) {
                case AudioManager.AUDIOFOCUS_LOSS:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                    audioFocusGranted = false;
                    audioManager.abandonAudioFocus(audioFocusListener);
                    break;
                case AudioManager.AUDIOFOCUS_GAIN:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(AUDIO_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        boolean startRecognize = parseIntent(getIntent());
        if (savedInstanceState == null) {
            replaceFragment(StartFragment.newInstance(startRecognize), false);
        }
        requestPermissionIfNeeded();
        registerMediaButtonReceiver();

    }

    private boolean parseIntent(Intent intent) {
        if (intent != null && intent.hasExtra(EXTRA_START_RECOGNITION)) {
            if (getLastFragment() == null)
                return true;
            if (!(getLastFragment() instanceof StartFragment)) {
                getSupportFragmentManager().popBackStack();
                return true;
            }
            ((VoiceRecognizeFragment) getLastFragment()
                    .getChildFragmentManager()
                    .findFragmentById(R.id.voiceRecognizerContainer))
                    .startRecognize();
            return true;
        }
        return false;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        parseIntent(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestFocus();
    }

    private void requestFocus() {
        if (!audioFocusGranted) {
            int result = audioManager.requestAudioFocus(audioFocusListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED) {
                audioFocusGranted = true;
            }
            BTService.restart(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void registerMediaButtonReceiver() {
        BTService.start(this);
    }

    private void requestPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_PERMISSION);
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PERMISSION);
        }
    }


}
