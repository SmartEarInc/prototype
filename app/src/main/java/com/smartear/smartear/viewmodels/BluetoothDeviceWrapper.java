package com.smartear.smartear.viewmodels;

import android.bluetooth.BluetoothDevice;
import android.databinding.ObservableField;
import android.text.TextUtils;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 11.11.2015
 */
public class BluetoothDeviceWrapper {
    public ObservableField<BluetoothDevice> device = new ObservableField<>();
    public ObservableField<Boolean> isConnected = new ObservableField<>();
    public String getName(){
        return TextUtils.isEmpty(device.get().getName())?device.get().toString():device.get().getName();
    }
}
