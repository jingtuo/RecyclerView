package com.jingtuo.android.app;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jingtuo.android.widget.MultiRecyclerView;
import com.jingtuo.android.widget.RefreshRecyclerView;
import com.jingtuo.android.widget.adapter.RecyclerAdapter;
import com.jingtuo.android.widget.adapter.ViewHolder;
import com.jingtuo.android.widget.model.Item;
import com.jingtuo.android.widget.model.Status;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int WHAT_INIT = 1;
    private static final int WHAT_REFRESH = 2;

    private MultiRecyclerView multiRecyclerView;

    private Item selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        multiRecyclerView = (MultiRecyclerView) findViewById(R.id.multiRecyclerView);
        RefreshRecyclerView refreshRecyclerView = createRefreshRecyclerView();
        refreshRecyclerView.setOnRefreshListener(new MyListener(0, null));
        multiRecyclerView.addRefreshRecyclerView(refreshRecyclerView);
        multiRecyclerView.setStatus(0, Status.LOADING);
        sendMessage(WHAT_INIT, 0, new MsgObj(null, getData(0, null)));
    }

    private Handler handler = new Handler() {
        @Override
        public synchronized void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (WHAT_INIT == msg.what) {
                int index = msg.arg1;
                MsgObj msgObj = (MsgObj) msg.obj;
                Item item = msgObj.getParent();
                ArrayList<Item> items = msgObj.getItems();
                RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(index);
                MyAdapter adapter = (MyAdapter) refreshRecyclerView.getRecyclerAdapter();
                if (adapter == null) {
                    adapter = new MyAdapter(MainActivity.this, index);
                    refreshRecyclerView.setRecyclerAdapter(adapter);
                }
                //查看前一个
                Item previousItem = getPreviousSelected(index);
                if (index==0 || (item != null && previousItem!=null&&item.getKey()!=null&&item.getKey().equals(previousItem.getKey()))) {//上一级选项未切换
                    adapter.setData(items);
                    adapter.setSelected(null);
                    adapter.notifyDataSetChanged();
                    multiRecyclerView.setStatus(index, Status.NORMAL);
                }
                return;
            }
            if (WHAT_REFRESH == msg.what) {
                int index = msg.arg1;
                MsgObj msgObj = (MsgObj) msg.obj;
                Item parent = msgObj.getParent();
                ArrayList<Item> items = msgObj.getItems();
                RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(index);
                RecyclerAdapter adapter = refreshRecyclerView.getRecyclerAdapter();
                if (adapter == null) {
                    adapter = new MyAdapter(MainActivity.this, index);
                    refreshRecyclerView.setRecyclerAdapter(adapter);
                }
                adapter.setData(items);
                adapter.notifyDataSetChanged();
                multiRecyclerView.setStatus(index, Status.NORMAL);
                //TODO 如果刷新完之后已选择数据不存在,需要关闭当前级别之后的数据
            }
        }
    };

    /**
     * 生成level级别的数据，item为父级节点的数据
     *
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
            if (level == 0 || item == null) {
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


    private void sendMessage(int what, int level, MsgObj msgObj) {
        Message msg = new Message();
        msg.what = what;
        msg.arg1 = level;
        msg.obj = msgObj;
        handler.sendMessageDelayed(msg, 3000);
    }

    private RefreshRecyclerView createRefreshRecyclerView() {
        RefreshRecyclerView refreshRecyclerView = (RefreshRecyclerView) View.inflate(this, R.layout.refresh_recycler_view, null);
        return refreshRecyclerView;
    }


    class MyAdapter extends RecyclerAdapter<Item> {

        private int index;
        private Item selected;

        public MyAdapter(Context context, int index) {
            super(context);
            this.index = index;
        }

        @Override
        public ViewHolder<Item> onCreateViewHolder(final ViewGroup parent, int viewType) {
            return new ViewHolder<Item>(getInflater().inflate(R.layout.recycler_view_item, parent, false)) {
                @Override
                public void setView(Item data) {
                    TextView textView = (TextView) getView();
                    textView.setText(data.getValue());
                    if (checkSelected(data)) {
                        textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorPrimary));
                    } else {
                        textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.colorAccent));
                    }
                    textView.setOnClickListener(new MyListener(index, data));
                }
            };


        }

        private boolean checkSelected(Item item) {
            return selected != null && item != null && selected.getKey() != null && selected.getKey().equals(item.getKey());
        }

        class MyListener implements View.OnClickListener {
            private int index;
            private Item item;

            public MyListener(int index, Item item) {
                this.index = index;
                this.item = item;
            }

            @Override
            public void onClick(View view) {
                if (checkSelected(item)) {
                    return;
                }
                selected = item;
                notifyDataSetChanged();
                handleClick(index, item);
            }
        }

        public Item getSelected() {
            return selected;
        }

        public void setSelected(Item selected) {
            this.selected = selected;
        }
    }

    private void handleClick(int index, Item item) {
        if (item == null) {
            return;
        }
        if (!item.isHasSubItems()) {//没有下一级数据
            RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(index + 1);
            if (refreshRecyclerView != null) {
                refreshRecyclerView.setVisibility(View.GONE);
            }
            MyAdapter adapter = null;
            Item selected = null;
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < index; i++) {
                refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(i);
                adapter = (MyAdapter) refreshRecyclerView.getRecyclerAdapter();
                selected = adapter.getSelected();
                buffer.append(selected != null ? selected.getValue() : "");
                buffer.append("\n");
            }
            buffer.append(item != null ? item.getValue() : "");
            Toast.makeText(this, buffer.toString(), Toast.LENGTH_SHORT).show();
            return;
        }
        RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(index + 1);
        if (refreshRecyclerView == null) {
            refreshRecyclerView = createRefreshRecyclerView();
            refreshRecyclerView.setOnRefreshListener(new MyListener(index + 1, item));
            multiRecyclerView.addRefreshRecyclerView(refreshRecyclerView);
        }
        refreshRecyclerView.setVisibility(View.VISIBLE);
        refreshRecyclerView.setStatus(Status.LOADING);
        ArrayList<Item> items = item.getSubItems();
        if (items != null && items.size() >= 1) {
            sendMessage(WHAT_INIT, index + 1, new MsgObj(item, items));
            return;
        }
        item.setSubItems(getData(index + 1, item));
        sendMessage(WHAT_INIT, index + 1, new MsgObj(item, item.getSubItems()));
    }

    class MyListener implements SwipeRefreshLayout.OnRefreshListener {

        private int index;
        private Item parent;

        public MyListener(int index, Item parent) {
            this.index = index;
            this.parent = parent;
        }


        @Override
        public void onRefresh() {
            ArrayList<Item> items = getData(index, parent);
            sendMessage(WHAT_REFRESH, index, new MsgObj(parent, items));
        }
    }

    class MsgObj {
        private Item parent;
        private ArrayList<Item> items;

        public MsgObj(Item parent, ArrayList<Item> items) {
            this.parent = parent;
            this.items = items;
        }

        public Item getParent() {
            return parent;
        }

        public ArrayList<Item> getItems() {
            return items;
        }
    }

    private Item getPreviousSelected(int index) {
        RefreshRecyclerView refreshRecyclerView = multiRecyclerView.getRefreshRecyclerView(index - 1);
        if (refreshRecyclerView == null) {
            return null;
        }
        MyAdapter adapter = (MyAdapter) refreshRecyclerView.getRecyclerAdapter();
        if (adapter == null) {
            return null;
        }
        Item item = adapter.getSelected();
        return item;
    }

}
