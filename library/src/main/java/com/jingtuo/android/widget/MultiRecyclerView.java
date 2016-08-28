package com.jingtuo.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

/**
 * 结合多个RefreshRecyclerView实现多级选择,如地区,职业;
 * 默认至少有一级数据;
 * Created by 28173_000 on 2016/8/27.
 */
public class MultiRecyclerView extends LinearLayout {

    public MultiRecyclerView(Context context) {
        super(context);
        initView(context, null);
    }

    public MultiRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, null);
    }

    private void initView (Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
        addRefreshRecyclerView(new RefreshRecyclerView(context));
    }

    public void addRefreshRecyclerView (RefreshRecyclerView refreshRecyclerView) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        addView(refreshRecyclerView, params);
    }

    /**
     *
     * @param index
     * @return
     */
    public RefreshRecyclerView getRefreshRecyclerView(int index) {
        int count = getChildCount();
        if (index<0||index>=count) {
            return  null;
        }
        return (RefreshRecyclerView) getChildAt(index);
    }
}
