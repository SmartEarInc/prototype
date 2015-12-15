package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class TopPanelModel {
    public ObservableBoolean isMute = new ObservableBoolean();
    public ObservableBoolean isNoise = new ObservableBoolean();
    public ObservableInt soundVolume = new ObservableInt();
    public ObservableInt soundMaxVolume = new ObservableInt();
    public ObservableBoolean isPlayerActive = new ObservableBoolean();
    public ObservableBoolean isSmartEarSettingsActive = new ObservableBoolean();
    public ObservableBoolean isGeneralSettingsActive = new ObservableBoolean();
}
