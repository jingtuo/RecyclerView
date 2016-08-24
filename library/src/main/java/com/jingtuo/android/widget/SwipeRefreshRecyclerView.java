package com.jingtuo.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.jingtuo.android.widget.adapter.RecyclerAdapter;

/**
 * Created by 28173_000 on 2016/8/24.
 */
public class SwipeRefreshRecyclerView extends SwipeRefreshLayout {

    private RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;

    public SwipeRefreshRecyclerView(Context context) {
        super(context);
        initView(context, null);
    }

    public SwipeRefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        if (attrs!=null) {
            //TODO 自定义属性
//            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.);
        }
        recyclerView = new RecyclerView(context);
    }

    public void setAdapter(RecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
        recyclerView.setAdapter(recyclerAdapter);
    }
}
