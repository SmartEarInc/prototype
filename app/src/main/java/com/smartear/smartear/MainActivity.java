package com.smartear.smartear;

import android.Manifest;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAssignedNumbers;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;

import com.smartear.smartear.fragment.StartFragment;
import com.smartear.smartear.receivers.MediaButtonReceiver;
import com.smartear.smartear.utils.VersionUtils;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_MEDIA_BUTTON_CODE = 11;
    private static final int REQUEST_PERMISSION = 12;
    private AudioManager audioManager;
    private ComponentName receiverComponent;
    private PendingIntent pendingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (savedInstanceState == null) {
            replaceFragment(new StartFragment(), false);
        }

        registerMediaButtonReceiver();
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
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        receiverComponent = new ComponentName(this, MediaButtonReceiver.class);
        if(VersionUtils.lollipopOrHigher()){
            MediaSession mediaSession = new MediaSession(this, "MainActivity");
            pendingIntent = PendingIntent.getBroadcast(this, REQUEST_MEDIA_BUTTON_CODE, new Intent(this, MediaButtonReceiver.class), 0);
            mediaSession.setMediaButtonReceiver(pendingIntent);
        } else {
            audioManager.registerMediaButtonEventReceiver(receiverComponent);
        }

        IntentFilter intentFilter = new IntentFilter(BluetoothHeadset.ACTION_VENDOR_SPECIFIC_HEADSET_EVENT);
        intentFilter.addCategory(BluetoothHeadset.VENDOR_SPECIFIC_HEADSET_EVENT_COMPANY_ID_CATEGORY + "." + BluetoothAssignedNumbers.SONY_ERICSSON);
        registerReceiver(new MediaButtonReceiver(), intentFilter);

        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        adapter.getProfileProxy(this, new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                BluetoothHeadset bluetoothHeadset = (BluetoothHeadset) proxy;

            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        }, BluetoothProfile.HEADSET);

        requestPermissionIfNeeded();
    }

    private void requestPermissionIfNeeded() {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_PERMISSION);
        }
    }


}
