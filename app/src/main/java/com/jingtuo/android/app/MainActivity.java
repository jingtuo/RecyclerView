package com.jingtuo.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jingtuo.android.widget.LinearItemDecoration;
import com.jingtuo.android.widget.MultiRecyclerView;
import com.jingtuo.android.widget.RefreshRecyclerView;
import com.jingtuo.android.widget.RichRecyclerView;
import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.adapter.ViewHolder;
import com.jingtuo.android.widget.model.Item;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int WHAT_INIT = 1;
    private static final int WHAT_REFRESH = 2;

    private MultiRecyclerView multiRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiRecyclerView = (MultiRecyclerView) findViewById(R.id.multiRecyclerView);
        RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(0);
        refreshRecyclerView.setEnabled(false);
        RichRecyclerView richRecyclerView = refreshRecyclerView.getRichRecyclerView();
        richRecyclerView.setStatus(RichRecyclerView.Status.LOADING);
        MyListener listener = new MyListener(null, 0);
        refreshRecyclerView.setOnRefreshListener(listener);
        sendMessage(WHAT_INIT, 0, getData(0, null));
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (WHAT_INIT==msg.what) {
                int level = msg.arg1;
                ArrayList<Item> items = ( ArrayList<Item>)msg.obj;
                RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(level);
                refreshRecyclerView.setEnabled(true);
                RichRecyclerView richRecyclerView = refreshRecyclerView.getRichRecyclerView();
                richRecyclerView.setStatus(RichRecyclerView.Status.NORMAL);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this);
                linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                richRecyclerView.setLayoutManager(linearLayoutManager);
                richRecyclerView.addItemDecoration(new LinearItemDecoration(linearLayoutManager, ContextCompat.getDrawable(MainActivity.this, R.drawable.recycler_view_decoration)));
                MyAdapter myAdapter = new MyAdapter(MainActivity.this, level);
                richRecyclerView.setRecyclerAdapter(myAdapter);
                myAdapter.setData(items);
                myAdapter.notifyDataSetChanged();
                return ;
            }
            if (WHAT_REFRESH==msg.what) {
                int level = msg.arg1;
                ArrayList<Item> items = (ArrayList<Item>)msg.obj;
                RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(level);
                refreshRecyclerView.setRefreshing(false);
                RichRecyclerView richRecyclerView = refreshRecyclerView.getRichRecyclerView();
                richRecyclerView.getRecyclerAdapter().setData(items);
                richRecyclerView.getRecyclerAdapter().notifyDataSetChanged();
            }

        }
    };

    /**
     * 生成level级别的数据，item为父级节点的数据
     * @param level
     * @param item
     * @return
     */
    private ArrayList<Item> getData(int level, Item item) {
        ArrayList<Item> data = new ArrayList<>();
        Random random = new Random();
        Item child = null;
        for (int i = 0; i < 30; i++) {
            child = new Item();
            if (level==0||item==null) {
                child.setKey("" + i);
            } else {
                child.setKey(item.getKey() + "-" + i);
            }
            child.setValue(child.getKey() + ":" + random.nextInt(30));
            if (level == 0 || level == 1) {
                child.setHasSubItems(random.nextBoolean());
            } else {
                child.setHasSubItems(false);
            }
            data.add(child);
        }
        return data;
    }

    class MyListener implements  SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

        private Item item;

        private int level;

        /**
         *
         * @param item 用于指定父级节点,根据父节点查
         * @param level
         */
        public MyListener(Item item, int level) {
            this.item = item;
            this.level = level;
        }

        @Override
        public void onRefresh() {
            ArrayList<Item> items = getData(level, item);
            sendMessage(WHAT_REFRESH, level, items);
        }

        @Override
        public void onClick(View view) {
            if (item == null) {
                return;
            }
            if (!item.isHasSubItems()) {
                RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(level + 1);
                if (refreshRecyclerView!=null) {
                    refreshRecyclerView.setVisibility(View.GONE);
                }
                Toast.makeText(view.getContext(), item.getValue(), Toast.LENGTH_SHORT).show();
                return;
            }
            RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(level + 1);
            if (refreshRecyclerView==null) {
                refreshRecyclerView = new RefreshRecyclerView(MainActivity.this);
                multiRecyclerView.addRefreshRecyclerView(refreshRecyclerView);
                refreshRecyclerView.setEnabled(false);
                RichRecyclerView richRecyclerView = refreshRecyclerView.getRichRecyclerView();
                richRecyclerView.setStatus(RichRecyclerView.Status.LOADING);
                MyListener listener = new MyListener(item, level + 1);
                refreshRecyclerView.setOnRefreshListener(listener);
                ArrayList<Item> items =  getData(level+1, item);
                item.setSubItems(items);
                sendMessage(WHAT_INIT, level + 1, items);
                return ;
            }
            ArrayList<Item> items = item.getSubItems();
            RichRecyclerView richRecyclerView = refreshRecyclerView.getRichRecyclerView();
            if (items!=null&&items.size()>=1) {
                refreshRecyclerView.setVisibility(View.VISIBLE);
                richRecyclerView.getRecyclerAdapter().setData(items);
                richRecyclerView.getRecyclerAdapter().notifyDataSetChanged();
                return ;
            }
            richRecyclerView.setStatus(RichRecyclerView.Status.LOADING);
            sendMessage(WHAT_INIT, 0, getData(level + 1 , item));
        }
    }


    class MyAdapter extends RecyclerAdapter<Item> {

        private int level;

        public MyAdapter(Context context, int level) {
            super(context);
            this.level = level;
        }

        @Override
        public ViewHolder<Item> onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder<Item>(getInflater().inflate(R.layout.recycler_view_item, parent, false)) {
                @Override
                public void setView(Item data) {
                    TextView textView = (TextView) getView();
                    textView.setText(data.getValue());
                    textView.setOnClickListener(new MyListener(data, level));
                }
            };
        }
    }

    private void sendMessage(int what, int level, ArrayList<Item> items) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = level;
        msg.obj = items;
        handler.sendMessageDelayed(msg, 3000);
    }
}
