package com.jingtuo.android.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.jingtuo.android.widget.adapter.RecyclerAdapter;

/**
 * Created by JingTuo on 16/8/25.
 */
public class RichRecyclerView extends LinearLayout {

    public enum Status {
        LOADING, LOAD_FAILURE, EMPTY, NORMAL
    }

    private Status status = Status.NORMAL;

    private int loadingLayoutId;

    private int loadFailureLayoutId;

    private int emptyLayoutId;

    private View loadingView;

    private View loadFailureView;

    private View emptyView;

    private RecyclerView recyclerView;

    private RecyclerAdapter recyclerAdapter;

    private OnReloadListener onReloadListener;

    public RichRecyclerView(Context context) {
        super(context);
        initView(context, null);
    }

    public RichRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    private void initView(Context context, @Nullable AttributeSet attrs) {
        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER);
        if (attrs != null) {
            final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RichRecyclerView);
            int value = array.getInt(R.styleable.RichRecyclerView_status, -1);
            this.status = getStatus(value);
            loadingLayoutId = array.getResourceId(R.styleable.RichRecyclerView_loadingLayout, R.layout.loading_default);
            loadFailureLayoutId = array.getResourceId(R.styleable.RichRecyclerView_loadFailureLayout, R.layout.load_failure_default);
            emptyLayoutId = array.getResourceId(R.styleable.RichRecyclerView_emptyLayout, R.layout.empty_default);
            array.recycle();
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

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        if (status == null) {
            status = Status.NORMAL;
        }
        this.status = status;
        setView(status);
    }

    /**
     * 当状态在{@link Status#LOADING},{@link Status#LOAD_FAILURE},{@link Status#EMPTY}之间进行切换,当前组件会包含三个状态对应的组件;
     * 当状态为{@link Status#NORMAL}时,当前组件只一个组件-RecyclerView
     *
     * @param status
     */
    public void setView(Status status) {
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
        } else {
            if (recyclerView == null) {
                loadingView = null;
                loadFailureView = null;
                emptyView = null;
                removeAllViews();
                recyclerView = new RecyclerView(getContext());
                addChildView(recyclerView, R.id.recycler_view);
            }
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
     * 设置布局方式,只有状态是{@link Status#NORMAL},调用此方法才有作用
     *
     * @param layoutManager
     */
    public void setLayoutManager(LinearLayoutManager layoutManager) {
        if (Status.NORMAL == status && recyclerView != null) {
            recyclerView.setLayoutManager(layoutManager);
        }
    }

    /**
     * 添加分割线,只有状态是{@link Status#NORMAL},调用此方法才有作用
     *
     * @param itemDecoration
     */
    public void addItemDecoration(RecyclerView.ItemDecoration itemDecoration) {
        if (Status.NORMAL == status && recyclerView != null) {
            recyclerView.addItemDecoration(itemDecoration);
        }
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
        if (Status.NORMAL == status && recyclerView != null) {
            this.recyclerAdapter = recyclerAdapter;
            recyclerView.setAdapter(recyclerAdapter);
        }
    }

    public RecyclerAdapter getRecyclerAdapter() {
        return recyclerAdapter;
    }
}
