package com.jingtuo.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.jingtuo.android.widget.model.Status;


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

    private void initView(Context context, AttributeSet attrs) {
        setOrientation(HORIZONTAL);
    }

    public void addRefreshRecyclerView(RefreshRecyclerView refreshRecyclerView) {
        LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT);
        params.weight = 1;
        addView(refreshRecyclerView, params);
    }

    /**
     * @param index
     * @return
     */
    public RefreshRecyclerView getRefreshRecyclerView(int index) {
        int count = getChildCount();
        if (index < 0 || index >= count) {
            return null;
        }
        return (RefreshRecyclerView) getChildAt(index);
    }

    public void setStatus(int index, Status status) {
        RefreshRecyclerView refreshRecyclerView = getRefreshRecyclerView(index);
        if (refreshRecyclerView == null) {
            return;
        }
        if (Status.LOADING == status || Status.LOAD_FAILURE == status || Status.EMPTY == status) {
            refreshRecyclerView.setEnabled(false);//关闭下拉刷新
        } else if (Status.NORMAL == status) {
            refreshRecyclerView.setEnabled(true);
            if (refreshRecyclerView.isRefreshing()) {
                refreshRecyclerView.setRefreshing(false);
            }
        }
        if (View.VISIBLE != refreshRecyclerView.getVisibility()) {
            refreshRecyclerView.setVisibility(View.VISIBLE);
        }
        refreshRecyclerView.setStatus(status);
    }
}
