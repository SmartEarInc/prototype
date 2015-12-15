package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class SettingsModel {
    public ObservableField<CategoryTitleModel> title = new ObservableField<>();
    public ObservableArrayList<SettingsItemModel> settingsList = new ObservableArrayList<>();

    public SettingsModel() {
        super();
        CategoryTitleModel model = new CategoryTitleModel();
        model.title.set(SmartEarApplication.getContext().getString(R.string.settings));
        title.set(model);
    }
}
