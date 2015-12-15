package com.smartear.smartear.main.widget;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.smartear.smartear.databinding.ItemSettingsBinding;
import com.smartear.smartear.main.viewmodel.SettingsItemModel;
import com.smartear.smartear.widget.RecyclerViewAdapterBase;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class SettingsAdapter extends RecyclerViewAdapterBase<SettingsItemModel, ItemSettingsBinding> {
    @Override
    public ItemSettingsBinding inflateItem(LayoutInflater inflater, ViewGroup parent, int viewType) {
        return ItemSettingsBinding.inflate(inflater, parent, false);
    }
}
