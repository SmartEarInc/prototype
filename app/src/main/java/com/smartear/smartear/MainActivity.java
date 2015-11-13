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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
        window.addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        window.addFlags(WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON);
        setContentView(R.layout.activity_main);
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
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void registerMediaButtonReceiver() {
//        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//        receiverComponent = new ComponentName(this, MediaButtonReceiver.class);
        BTService.start(this);
//        if(VersionUtils.lollipopOrHigher()){
//            MediaSession mediaSession = new MediaSession(this, "MainActivity");
//            pendingIntent = PendingIntent.getBroadcast(this, REQUEST_MEDIA_BUTTON_CODE, new Intent(this, MediaButtonReceiver.class), 0);
//            mediaSession.setMediaButtonReceiver(pendingIntent);
//        } else {
//            audioManager.registerMediaButtonEventReceiver(receiverComponent);
//        }
//
//        IntentFilter intentFilter = new IntentFilter(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
//        intentFilter.addCategory(BluetoothHeadset.VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY + "." + BluetoothAssignedNumbers.SONY_ERICSSON);
//        registerReceiver(new MediaButtonReceiver(), intentFilter);
//
//        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
//        adapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
//            @Override
//            public void onServiceConnected(int profile, BluetoothProfile proxy) {
//                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) proxy;
//
//            }
//
//            @Override
//            public void onServiceDisconnected(int profile) {
//
//            }
//        }, BluetoothProfile.HEADSET);

    }

    private void requestPermissionIfNeeded() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        }
    }


}
