package com.smartear.smartear.widget;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.smartear.smartear.BR;

import java.util.List;

/**
 * Created by Belozerov on 11.06.2015.
 */

public abstract class RecyclerViewAdapterBase<T, V extends ViewDataBinding> extends RecyclerView.Adapter<RecyclerViewAdapterBase.ParseViewHolder> {
    private List<T> items;
    private OnItemClickListener onItemClickListener;

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public T getItem(int position) {
        return items.get(position);
    }

    public List<T> getItems() {
        return items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public void addItem(T item){
        this.items.add(item);
        notifyDataSetChanged();
    }

    @Override
    public ParseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        V binding = inflateItem(LayoutInflater.from(parent.getContext()), parent, viewType);
        return new ParseViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(RecyclerViewAdapterBase.ParseViewHolder holder, final int position) {
        holder.viewDataBinding.setVariable(BR.item, getItem(position));
        holder.viewDataBinding.getRoot().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemClick(getItem(position));
            }
        });
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public abstract V inflateItem(LayoutInflater inflater, ViewGroup parent, int viewType);

    public interface OnItemClickListener<T> {
        void onItemClick(T item);
    }

    public class ParseViewHolder extends RecyclerView.ViewHolder {
        public V viewDataBinding;

        public ParseViewHolder(View itemView) {
            super(itemView);
            viewDataBinding = DataBindingUtil.bind(itemView);
        }
    }
}
