package com.smartear.smartear.viewmodels;

import android.bluetooth.BluetoothDevice;
import android.databinding.ObservableField;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 11.11.2015
 */
public class BluetoothDeviceWrapper {
    public ObservableField<BluetoothDevice> device = new ObservableField<>();
    public ObservableField<Boolean> isConnected = new ObservableField<>();
}
