package com.jingtuo.android.widget.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 28173_000 on 2016/8/24.
 */
public abstract class ViewHolder<T> extends RecyclerView.ViewHolder {

    private View view;

    private T data;

    public ViewHolder(View itemView) {
        super(itemView);
        view = itemView;
    }

    public final T getData() {
        return data;
    }

    public final void setData(T data) {
        this.data = data;
        setView(data);
    }

    public final View getView() {
        return view;
    }

    public abstract void setView(T data);

}
