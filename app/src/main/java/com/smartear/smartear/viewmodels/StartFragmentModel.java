package com.smartear.smartear.viewmodels;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;

/**
 * Created by Belozerow on 10.11.2015.
 */
public class StartFragmentModel {
    public ObservableField<String> devicesText = new ObservableField<>();
    public ObservableInt volumeLevel = new ObservableInt();
    public ObservableInt maxVolumeLevel = new ObservableInt();
}
