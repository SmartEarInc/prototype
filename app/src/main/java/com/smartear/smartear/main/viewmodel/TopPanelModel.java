package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.view.View;

import com.smartear.smartear.SmartEarApplication;
import com.smartear.smartear.utils.MuteHelper;

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

}
