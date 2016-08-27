package com.jingtuo.android.app;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingtuo.android.widget.LinearItemDecoration;
import com.jingtuo.android.widget.RefreshRecyclerView;
import com.jingtuo.android.widget.RichRecyclerView;
import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RefreshRecyclerView recyclerView;
    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RefreshRecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setEnabled(false);
        recyclerView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                handler.sendEmptyMessageDelayed(4, 3000);
            }
        });
        recyclerView.getRichRecyclerView().setOnReloadListener(new RichRecyclerView.OnReloadListener() {
            @Override
            public void onReload() {
                recyclerView.getRichRecyclerView().setStatus(RichRecyclerView.Status.LOADING);
                int value = random.nextInt(3);
                handler.sendEmptyMessageDelayed(value, 3000);
            }
        });
        int value = random.nextInt(3);
        handler.sendEmptyMessageDelayed(value, 3000);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0 == msg.what) {//失败
                recyclerView.getRichRecyclerView().setStatus(RichRecyclerView.Status.LOAD_FAILURE);
                return ;
            }
            if (1 == msg.what) {//无数据
                recyclerView.getRichRecyclerView().setStatus(RichRecyclerView.Status.EMPTY);
                return;
            }
            if (4 == msg.what) {//刷新数据
                recyclerView.setRefreshing(false);
                recyclerView.getRichRecyclerView().getRecyclerAdapter().setData(getData());
                recyclerView.getRichRecyclerView().getRecyclerAdapter().notifyDataSetChanged();
                return;
            }
            recyclerView.setEnabled(true);
            recyclerView.getRichRecyclerView().setStatus(RichRecyclerView.Status.NORMAL);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
            linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.getRichRecyclerView().setLayoutManager(linearLayoutManager);
            recyclerView.getRichRecyclerView().addItemDecoration(new LinearItemDecoration(linearLayoutManager, ContextCompat.getDrawable(MainActivity.this, R.drawable.recycler_view_decoration)));
            RecyclerAdapter<String> adapter = new RecyclerAdapter<String>(MainActivity.this) {
                @Override
                public ViewHolder<String> onCreateViewHolder(ViewGroup parent, int viewType) {
                    return new ViewHolder<String>(getInflater().inflate(R.layout.recycler_view_item, parent, false)) {

                        @Override
                        public void setView(String data) {
                            TextView textView = (TextView) getView();
                            textView.setText(data);
                        }
                    };
                }
            };
            recyclerView.getRichRecyclerView().setRecyclerAdapter(adapter);
            adapter.setData(getData());
            adapter.notifyDataSetChanged();

        }
    };

    private ArrayList<String> getData() {
        ArrayList<String> data = new ArrayList<>();
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            data.add(random.nextInt(30) + "");
        }
        return data;
    }
}
