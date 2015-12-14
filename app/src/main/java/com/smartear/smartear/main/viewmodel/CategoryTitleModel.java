package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableField;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class CategoryTitleModel {
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> action = new ObservableField<>();
    public ObservableField<String> value = new ObservableField<>();
    public String getActionText(){
        return null;
    }
}
