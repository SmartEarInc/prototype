package com.smartear.smartear.main.widget;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.smartear.smartear.R;
import com.smartear.smartear.databinding.CategoryTitleBinding;
import com.smartear.smartear.main.viewmodel.CategoryTitleModel;

/**
 * Created: Belozerov
 * Company: APPGRANULA LLC
 * Date: 14.12.2015
 */
public class CategoryTitle extends FrameLayout {

    CategoryTitleModel model;
    CategoryTitleBinding binding;

    public CategoryTitle(Context context) {
        super(context);
        init();
    }

    public CategoryTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.category_title, this, true);
        model = new CategoryTitleModel();
        binding.setData(model);
    }

    public void setModel(CategoryTitleModel model) {
        this.model = model;
        binding.setData(model);
    }

    public void setOnBackClickListener(View.OnClickListener onBackClickListener) {
        binding.back.setOnClickListener(onBackClickListener);
    }
}
