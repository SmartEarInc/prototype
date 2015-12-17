package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import java.io.Serializable;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public class NestedSettingsModel implements Serializable{
    public String settingsKey;
    public ObservableField<CategoryTitleModel> title = new ObservableField<>();
    public ObservableArrayList<NestedSettingsItemModel> items = new ObservableArrayList<>();
}
