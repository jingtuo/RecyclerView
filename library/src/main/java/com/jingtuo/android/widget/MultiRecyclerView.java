package com.jingtuo.android.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * 结合多个RefreshRecyclerView实现多级选择,如地区,职业
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

    }

}
