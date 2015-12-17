package com.smartear.smartear.main.viewmodel;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

import com.smartear.smartear.R;
import com.smartear.smartear.SmartEarApplication;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class CategoryTitleModel {
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> action = new ObservableField<>();
    public ObservableField<String> value = new ObservableField<>();
    public ObservableBoolean isBack = new ObservableBoolean();

    public CharSequence getActionText() {
        String action = this.action.get();
        String value = this.value.get();
        String text = action + " : " + value;
        SpannableString spannableString = new SpannableString(text);
        int color = SmartEarApplication.getContext().getResources().getColor(R.color.colorAccent);
        spannableString.setSpan(new ForegroundColorSpan(color), text.indexOf(":") + 1, text.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}
