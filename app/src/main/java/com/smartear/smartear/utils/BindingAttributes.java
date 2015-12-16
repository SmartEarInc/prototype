package com.smartear.smartear.utils;

import android.databinding.BindingAdapter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.TextView;

import com.smartear.smartear.main.viewmodel.CategoryTitleModel;
import com.smartear.smartear.main.widget.CategoryTitle;

import java.util.HashMap;

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

    @BindingAdapter({"bind:categoryModel"})
    public static void setCategoryModel(CategoryTitle categoryTitle, CategoryTitleModel model) {
        categoryTitle.setModel(model);
    }

    private static HashMap<String, Typeface> typefaceHashMap = new HashMap<>();


    @BindingAdapter("bind:customFont")
    public static void setCustomFont(TextView textView, String font) {
        Typeface tf = typefaceHashMap.get(font);
        if (tf == null) {
            tf = Typeface.createFromAsset(textView.getContext().getAssets(), "fonts/" + font);
            typefaceHashMap.put(font, tf);
        }
        textView.setTypeface(tf);
    }
}
