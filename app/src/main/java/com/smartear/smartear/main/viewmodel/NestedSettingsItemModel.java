package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 17.12.2015
 */
public class NestedSettingsItemModel {
    public ObservableBoolean isChecked = new ObservableBoolean();
    public ObservableField<String> value = new ObservableField<>();

    public NestedSettingsItemModel(boolean isChecked, String value) {
        this.isChecked.set(isChecked);
        this.value.set(value);
    }
}
