package com.smartear.smartear.utils;

import android.databinding.BindingAdapter;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 10.11.2015
 */
public class BindingAttributes {
    @BindingAdapter({"bind:imageLeftDrawable"})
    public static void setImageLeftDrawable(TextView view, Drawable drawableRes) {
        view.setCompoundDrawablesWithIntrinsicBounds(drawableRes, null, null, null);
    }
}
