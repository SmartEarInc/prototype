package com.smartear.smartear.viewmodels;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by Belozerow on 10.11.2015.
 */
public class StartFragmentModel {
    public ObservableField<String> devicesText = new ObservableField<>();
    public ObservableInt musicVolumeLevel = new ObservableInt();
    public ObservableInt musicMaxVolumeLevel = new ObservableInt();

    public ObservableInt ringVolumeLevel = new ObservableInt();
    public ObservableInt ringMaxVolumeLevel = new ObservableInt();

    public ObservableBoolean isMute = new ObservableBoolean();
    public ObservableBoolean isMicMute = new ObservableBoolean();
}
