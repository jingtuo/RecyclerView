package com.jingtuo.android.app;

import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jingtuo.android.widget.LinearItemDecoration;
import com.jingtuo.android.widget.RichRecyclerView;
import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.adapter.ViewHolder;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RichRecyclerView recyclerView;

    private Random random = new Random();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RichRecyclerView) findViewById(R.id.richRecyclerView);
        recyclerView.setOnReloadListener(new RichRecyclerView.OnReloadListener() {
            @Override
            public void onReload() {
                recyclerView.setStatus(RichRecyclerView.Status.LOADING);
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
            if (0==msg.what) {//失败
                recyclerView.setStatus(RichRecyclerView.Status.LOAD_FAILURE);
            } else if (1==msg.what) {//无数据
                recyclerView.setStatus(RichRecyclerView.Status.EMPTY);
            } else {
                recyclerView.setStatus(RichRecyclerView.Status.NORMAL);
                LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
                layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.addItemDecoration(new LinearItemDecoration(layoutManager, ContextCompat.getDrawable(MainActivity.this, R.drawable.recycler_view_decoration)));
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
                recyclerView.setRecyclerAdapter(adapter);
                ArrayList<String> data = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    data.add((i + 1) + "");
                }
                adapter.setData(data);
                adapter.notifyDataSetChanged();
            }
        }
    };
}
