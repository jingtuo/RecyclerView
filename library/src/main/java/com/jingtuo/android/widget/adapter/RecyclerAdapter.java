package com.jingtuo.android.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * 使用RecyclerView不能直接View.inflate()
 * <pre>
 *     onBindViewHolder中创建view时,如果希望单个元素宽度等于屏幕宽度,不能使用View.inflate(),需要使用inflater.inflate(layoutId, 任意view, false)
 *     当业务常见出现多个viewType，建议将不同viewType对应的数据整合为一个类，否则无法使用此类
 * </pre>
 * Created by 28173_000 on 2016/8/24.
 */
public abstract class RecyclerAdapter<T> extends RecyclerView.Adapter<ViewHolder<T>> {

    private ArrayList<T> data;

    private Context context;

    private LayoutInflater inflater;

    public RecyclerAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public final void onBindViewHolder(ViewHolder<T> holder, int position) {
        holder.setData(data.get(position));
    }

    @Override
    public final int getItemCount() {
        return data!=null?data.size():0;
    }

    public final ArrayList<T> getData() {
        return data;
    }

    public final void setData(ArrayList<T> data) {
        this.data = data;
    }

    public final LayoutInflater getInflater() {
        return inflater;
    }
}
