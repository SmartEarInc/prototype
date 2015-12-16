package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.databinding.ObservableLong;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class PlayerModel {
    public ObservableField<CategoryTitleModel> playerInfo = new ObservableField<>();
    public ObservableField<String> totalTime = new ObservableField<>();
    public ObservableField<String> currentTime = new ObservableField<>();
    public ObservableInt progress = new ObservableInt();
    public ObservableInt maxProgress = new ObservableInt();
}
