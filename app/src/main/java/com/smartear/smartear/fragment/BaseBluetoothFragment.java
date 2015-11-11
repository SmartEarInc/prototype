package com.smartear.smartear.fragment;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothProfile;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.util.List;

/**
 * Created by Belozerow on 10.11.2015.
 */
public abstract class BaseBluetoothFragment extends BaseFragment {
    BroadcastReceiver bluetoothReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onDeviceFound(device);
            }

            if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);
                final int prevState = intent.getIntExtra(BluetoothDevice.EXTRA_PREVIOUS_BOND_STATE, BluetoothDevice.ERROR);
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (state == BluetoothDevice.BOND_BONDED && prevState == BluetoothDevice.BOND_BONDING) {
                    onDevicePaired(device);

                } else if (state == BluetoothDevice.BOND_NONE && prevState == BluetoothDevice.BOND_BONDED) {
                    onDeviceUnPaired(device);
                }
            }

            if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onDeviceConnected(device);
            }

            if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                final BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                onDeviceDisconnected(device);
            }
        }
    };

    protected abstract void onDeviceDisconnected(BluetoothDevice device);

    protected abstract void onDeviceConnected(BluetoothDevice device);

    private List<BluetoothDevice> connectedDevices;

    public abstract void onDeviceFound(BluetoothDevice device);

    public abstract void onDevicePaired(BluetoothDevice device);

    public abstract void onDeviceUnPaired(BluetoothDevice device);

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
        getActivity().registerReceiver(bluetoothReceiver, filter);

        updateConnectedDevices();
    }

    protected void updateConnectedDevices() {
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        BluetoothProfile.ServiceListener profileListener = new BluetoothProfile.ServiceListener() {
            @Override
            public void onServiceConnected(int profile, BluetoothProfile proxy) {
                connectedDevices = proxy.getConnectedDevices();
                updateConnectedDevices(connectedDevices);
            }

            @Override
            public void onServiceDisconnected(int profile) {

            }
        };
        bluetoothAdapter.getProfileProxy(getActivity(), profileListener, BluetoothProfile.HEADSET);
    }

    protected abstract void updateConnectedDevices(List<BluetoothDevice> connectedDevices);

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(bluetoothReceiver);
        BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        bluetoothAdapter.cancelDiscovery();
    }
}
