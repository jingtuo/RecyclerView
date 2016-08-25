package com.jingtuo.android.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by JingTuo on 16/8/25.
 */
public class RichRecyclerView extends RecyclerView {

    public enum Mode {
        loading, loadFailure, empty, normal
    }

    private Mode mode = Mode.normal;

    public RichRecyclerView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public RichRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public RichRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView(context, attrs, defStyle);
    }

    private void initView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        if (attrs!=null) {
            //TODO 自定义属性
        }
        
    }


}
