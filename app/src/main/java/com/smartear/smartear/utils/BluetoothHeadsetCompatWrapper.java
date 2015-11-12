package com.smartear.smartear.utils;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothHeadset;

import java.lang.reflect.Method;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 12.11.2015
 */
public class BluetoothHeadsetCompatWrapper {
    private static final Class<?> CLASS_BluetoothHeadset = BluetoothHeadset.class;
    private static final Method METHOD_startScoUsingVirtualVoiceCall = CompatUtils.getMethod(
            CLASS_BluetoothHeadset, "startScoUsingVirtualVoiceCall", BluetoothDevice.class);
    private static final Method METHOD_stopScoUsingVirtualVoiceCall = CompatUtils.getMethod(
            CLASS_BluetoothHeadset, "stopScoUsingVirtualVoiceCall", BluetoothDevice.class);

    private final BluetoothHeadset mHeadset;

    public BluetoothHeadsetCompatWrapper(BluetoothHeadset headset) {
        mHeadset = headset;
    }

    public boolean startScoUsingVirtualVoiceCall(BluetoothDevice device) {
        return (Boolean) CompatUtils.invoke(mHeadset, false, METHOD_startScoUsingVirtualVoiceCall,
                device);
    }

    public boolean stopScoUsingVirtualVoiceCall(BluetoothDevice device) {
        return (Boolean) CompatUtils.invoke(mHeadset, false, METHOD_stopScoUsingVirtualVoiceCall,
                device);
    }
}
