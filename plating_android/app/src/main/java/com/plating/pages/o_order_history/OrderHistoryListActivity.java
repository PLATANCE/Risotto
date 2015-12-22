package com.plating.pages.o_order_history;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.object.OrderHistoryListRow;
import com.plating.network.o_order_history.GetOrderHistoryListFromServer;
import com.plating.util.PreCachingLayoutManager;

import java.util.ArrayList;

public class OrderHistoryListActivity extends PlatingActivity {

    private RecyclerView mRecyclerView;
    private OrderHistoryListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.n_a_order_history_list_activity);

        getAllViews();

        setUpRecyclerView();

        getOrderHistoryListFromServer();
    }

    public void getAllViews() {
        // Recycler view for list of restaurants
        mRecyclerView = (RecyclerView) findViewById(R.id.order_history_list_recycler_view);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(this));
    }

    public void setUpRecyclerView() {
        mAdapter = new OrderHistoryListAdapter(this, new ArrayList<OrderHistoryListRow>());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void getOrderHistoryListFromServer() {
        GetOrderHistoryListFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getOrderHistoryListFromServer_Callback(ArrayList<OrderHistoryListRow> orderHistoryListRowArrayList) {
        Log.d(LOG_TAG, "getOrderHistoryListFromServer_Callback: size = " + orderHistoryListRowArrayList.size());
        mAdapter.updateData(orderHistoryListRowArrayList);
        mAdapter.notifyDataSetChanged();
    }

    public void startOrderHistoryActivity(int idx) {
        Intent intent = new Intent(this, OrderHistoryActivity.class);
        intent.putExtra(Constant.ORDER_HISTORY_LIST_ACTIVITY__ORDER_HISTORY_ID, idx);
        startActivity(intent);
        overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
    }
}
