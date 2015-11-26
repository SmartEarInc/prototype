package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Belozerow on 10.11.2015.
 */
public abstract class BaseBluetoothFragment extends BaseFragment {
    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            final BluetoothDevice device = intent.hasExtra(BluetoothDevice.EXTRA_DEVICE) ? (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE) : null;
            switch (action) {
                case BluetoothDevice.ACTION_FOUND:
                    onDeviceFound(device);
                    break;
                case BluetoothDevice.ACTION_BOND_STATE_CHANGED:
                    final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                    final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                    if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                        onDevicePaired(device);

                    } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                        onDeviceUnPaired(device);
                    }
                    break;
                case BluetoothDevice.ACTION_ACL_CONNECTED:
                    onDeviceConnected(device);
                    break;
                case BluetoothDevice.ACTION_ACL_DISCONNECTED:
                    onDeviceDisconnected(device);
                    break;
                case BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED:
                    if (isBluetoothEnabled()) {
                        onBluetoothConnected();
                    }
                    break;
            }
        }
    };
    protected BluetoothHeadset bluetoothHeadset;

    protected abstract void onDeviceDisconnected(BluetoothDevice device);

    protected abstract void onDeviceConnected(BluetoothDevice device);

    protected abstract void onBluetoothConnected();

    private List<BluetoothDevice> connectedDevices = new ArrayList<>();

    public List<BluetoothDevice> getConnectedDevices() {
        return connectedDevices;
    }

    public abstract void onDeviceFound(BluetoothDevice device);

    public abstract void onDevicePaired(BluetoothDevice device);

    public abstract void onDeviceUnPaired(BluetoothDevice device);

    private void checkBluetoothEnabled() {
        if (!isBluetoothEnabled()) {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothAdapter.enable();
        }
    }

    protected boolean isBluetoothEnabled() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        return bluetoothAdapter.isEnabled();
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
        getActivity().registerReceiver(bluetoothReceiver, filter);
        checkBluetoothEnabled();
        updateConnectedDevices();
    }

    private Handler requestConnectedHandler = new Handler();
    private Runnable requestConnectedRunnable = new Runnable() {
        @Override
        public void run() {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
                @Override
                public void onServiceConnected(int profile, BluetoothProfile proxy) {
//                bluetoothHeadset = (BluetoothHeadset) proxy;
                    List<BluetoothDevice> devices = proxy.getConnectedDevices();
                    for (BluetoothDevice device : devices) {
                        if (!connectedDevices.contains(device)) {
                            connectedDevices.add(device);
                        }
                    }
                    updateConnectedDeviceHandler.removeCallbacks(updateConnectedDeviceRunnable);
                    updateConnectedDeviceHandler.postDelayed(updateConnectedDeviceRunnable, 100);
                }

                @Override
                public void onServiceDisconnected(int profile) {

                }
            };
            connectedDevices.clear();
            bluetoothAdapter.getProfileProxy(getActivity(), profileListener, BluetoothProfile.HEADSET);
            bluetoothAdapter.getProfileProxy(getActivity(), profileListener, BluetoothProfile.A2DP);
        }
    };

    protected void updateConnectedDevices() {
        requestConnectedHandler.removeCallbacks(requestConnectedRunnable);
        requestConnectedHandler.postDelayed(requestConnectedRunnable, 500);
    }

    private Runnable updateConnectedDeviceRunnable = new Runnable() {
        @Override
        public void run() {
            updateConnectedDevices(connectedDevices);
        }
    };
    private Handler updateConnectedDeviceHandler = new Handler();

    protected abstract void updateConnectedDevices(List<BluetoothDevice> connectedDevices);

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(bluetoothReceiver);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
    }
}
