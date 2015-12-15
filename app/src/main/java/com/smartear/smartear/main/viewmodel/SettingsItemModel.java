package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableField;
import android.databinding.ObservableInt;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 15.12.2015
 */
public class SettingsItemModel {
    public ObservableInt id = new ObservableInt();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> value = new ObservableField<>();

    public SettingsItemModel(int id, String title, String value) {
        super();
        this.id.set(id);
        this.title.set(title);
        this.value.set(value);
    }

    public CharSequence getText() {
        if (TextUtils.isEmpty(value.get())) {
            return new SpannableString(title.get());
        }

        String text = title.get() + " : " + value.get();
        SpannableString spannableString = new SpannableString(text);
        int color = SmartEarApplication.getContext().getResources().getColor(R.color.colorAccent);
        spannableString.setSpan(new ForegroundColorSpan(color), text.indexOf(":") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}
