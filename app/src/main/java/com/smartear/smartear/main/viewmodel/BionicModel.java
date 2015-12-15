package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableInt;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class BionicModel {
    public ObservableField<CategoryTitleModel> title = new ObservableField<>();
    public ObservableBoolean isLeftSelected = new ObservableBoolean(true);
    public ObservableInt maxMicValue = new ObservableInt(100);
    public ObservableInt micValue = new ObservableInt();
    public ObservableField<CategoryTitleModel> presets = new ObservableField<>();

    public BionicModel() {
        super();
        CategoryTitleModel categoryTitleModel = new CategoryTitleModel();
        categoryTitleModel.title.set(SmartEarApplication.getContext().getString(R.string.bionicHearing));
        title.set(categoryTitleModel);
    }
}
