package com.jingtuo.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.model.Status;

/**
 * Created by 28173_000 on 2016/8/27.
 */
public class RefreshRecyclerView extends SwipeRefreshLayout {

    private RichRecyclerView richRecyclerView;

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        richRecyclerView = new RichRecyclerView(context, attrs);
        addView(richRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public RichRecyclerView getRichRecyclerView() {
        return richRecyclerView;
    }

    public void setStatus(Status status) {
        richRecyclerView.setStatus(status);
    }

    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter) {
        richRecyclerView.setRecyclerAdapter(recyclerAdapter);
    }

    public RecyclerAdapter getRecyclerAdapter() {
        return richRecyclerView.getRecyclerAdapter();
    }

    public Status getStatus() {
        return richRecyclerView.getStatus();
    }

}
