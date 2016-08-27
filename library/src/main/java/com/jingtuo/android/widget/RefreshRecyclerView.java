package com.jingtuo.android.widget;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by 28173_000 on 2016/8/27.
 */
public class RefreshRecyclerView extends SwipeRefreshLayout {

    private RichRecyclerView richRecyclerView;

    private LinearLayoutManager linearLayoutManager;

    public RefreshRecyclerView(Context context) {
        super(context);
        initView(context, null);
    }

    public RefreshRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        richRecyclerView = new RichRecyclerView(context, attrs);
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        richRecyclerView.setLayoutManager(linearLayoutManager);
        addView(richRecyclerView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public LinearLayoutManager getLinearLayoutManager() {
        return linearLayoutManager;
    }

    public RichRecyclerView getRichRecyclerView() {
        return richRecyclerView;
    }
}
