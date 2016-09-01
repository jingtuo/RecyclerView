package com.jingtuo.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.model.Status;

/**
 * 结合{@link LinearLayout}和{@link RecyclerView} 组成一个支持多种状态{@link Status}的RichRecyclerView组件
 * Created by JingTuo on 16/8/25.
 */
public class RichRecyclerView extends FrameLayout {

    private Status status;

    private int loadingLayoutId;

    private int loadFailureLayoutId;

    private int emptyLayoutId;

    private int decorationId;

    private View loadingView;

    private View loadFailureView;

    private View emptyView;

    private RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;

    private OnReloadListener onReloadListener;

    public RichRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RichRecyclerView);
        int value = array.getInt(R.styleable.RichRecyclerView_status, -1);
        status = getStatus(value);
        loadingLayoutId = array.getResourceId(R.styleable.RichRecyclerView_loadingLayout, R.layout.loading_default);
        loadFailureLayoutId = array.getResourceId(R.styleable.RichRecyclerView_loadFailureLayout, R.layout.load_failure_default);
        emptyLayoutId = array.getResourceId(R.styleable.RichRecyclerView_emptyLayout, R.layout.empty_default);
        decorationId = array.getResourceId(R.styleable.RichRecyclerView_decoration, R.drawable.decoration_default);
        array.recycle();
        recyclerView = new RecyclerView(context, attrs);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof LinearLayoutManager) {
            if (decorationId != -1) {
                recyclerView.addItemDecoration(new LinearItemDecoration((LinearLayoutManager) layoutManager, ContextCompat.getDrawable(context, decorationId)));
            }
        }
        setView(status);
    }

    private Status getStatus(int layoutMode) {
        if (Status.LOADING.ordinal() == layoutMode) {
            return Status.LOADING;
        }
        if (Status.LOAD_FAILURE.ordinal() == layoutMode) {
            return Status.LOAD_FAILURE;
        }
        if (Status.EMPTY.ordinal() == layoutMode) {
            return Status.EMPTY;
        }
        return Status.NORMAL;
    }

    /**
     * @param status 参数不允许为null
     */
    public void setStatus(@NonNull Status status) {
        this.status = status;
        setView(status);
    }

    /**
     *
     * @param status
     */
    private void setView(Status status) {
        if (Status.LOADING == status) {
            if (loadingView == null) {
                loadingView = View.inflate(getContext(), loadingLayoutId, null);
                addChildView(loadingView, R.id.loading);
            }
            showView(R.id.loading);
        } else if (Status.LOAD_FAILURE == status) {
            if (loadFailureView == null) {
                loadFailureView = View.inflate(getContext(), loadFailureLayoutId, null);
                loadFailureView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onReloadListener != null) {
                            onReloadListener.onReload();
                        }
                    }
                });
                addChildView(loadFailureView, R.id.load_failure);
            }
            showView(R.id.load_failure);
        } else if (Status.EMPTY == status) {
            if (emptyView == null) {
                emptyView = View.inflate(getContext(), emptyLayoutId, null);
                emptyView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (onReloadListener != null) {
                            onReloadListener.onReload();
                        }
                    }
                });
                addChildView(emptyView, R.id.empty);
            }
            showView(R.id.empty);
        } else if (Status.NORMAL==status) {
            View view = findViewById(R.id.recycler_view);
            if (view == null) {
                addChildView(recyclerView, R.id.recycler_view);
            }
            showView(R.id.recycler_view);
        }
    }

    /**
     * 将View添加到当前组件中,并设置view的id
     *
     * @param view
     * @param id
     */
    private void addChildView(View view, int id) {
        view.setId(id);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    /**
     * 显示指定id的view
     *
     * @param id
     */
    private void showView(int id) {
        int count = getChildCount();
        View view;
        for (int i = 0; i < count; i++) {
            view = getChildAt(i);
            if (view.getId() == id) {
                view.setVisibility(View.VISIBLE);
            } else {
                view.setVisibility(View.GONE);
            }
        }
    }

    public interface OnReloadListener {
        void onReload();
    }

    public void setOnReloadListener(OnReloadListener onReloadListener) {
        this.onReloadListener = onReloadListener;
    }

    /**
     * 只有状态是{@link Status#NORMAL},调用此方法才作用
     *
     * @param recyclerAdapter
     */
    public void setRecyclerAdapter(RecyclerAdapter recyclerAdapter) {
        this.recyclerAdapter = recyclerAdapter;
        recyclerView.setAdapter(recyclerAdapter);
    }

    public RecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }

    public View getLoadFailureView() {
        return loadFailureView;
    }

    public View getLoadingView() {
        return loadingView;
    }

    public View getEmptyView() {
        return emptyView;
    }

    public void setLoadingLayoutId(int loadingLayoutId) {
        this.loadingLayoutId = loadingLayoutId;
    }

    public void setLoadFailureLayoutId(int loadFailureLayoutId) {
        this.loadFailureLayoutId = loadFailureLayoutId;
    }

    public void setEmptyLayoutId(int emptyLayoutId) {
        this.emptyLayoutId = emptyLayoutId;
    }

    public int getLoadingLayoutId() {
        return loadingLayoutId;
    }

    public int getLoadFailureLayoutId() {
        return loadFailureLayoutId;
    }

    public int getEmptyLayoutId() {
        return emptyLayoutId;
    }

    public Status getStatus() {
        return status;
    }
}
