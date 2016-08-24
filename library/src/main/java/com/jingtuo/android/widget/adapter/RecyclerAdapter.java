package com.jingtuo.android.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * Created by 28173_000 on 2016/8/24.
 */
public class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private ArrayList<T> data;

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder<T> holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public int getItemCount() {
        return data!=null?data.size():0;
    }

    public ArrayList<T> getData() {
        return data;
    }

    public void setData(ArrayList<T> data) {
        this.data = data;
    }
}
