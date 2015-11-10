package com.smartear.smartear.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Belozerov on 11.06.2015.
 */
public class ViewWrapper<T, V extends View & ViewWrapper.Binder<T>> extends RecyclerView.ViewHolder {
    private V view;

    public ViewWrapper(V itemView) {
        super(itemView);
        view = itemView;
    }

    public V getView() {
        return view;
    }

    public interface Binder<T> {
        void bind(T data);
    }
}
