package com.plating.pages.o_order_history;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.object.OrderHistoryListRow;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class OrderHistoryListAdapter extends RecyclerView.Adapter<OrderHistoryListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "OrderHistoryListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<OrderHistoryListRow> data = new ArrayList<>();

    public OrderHistoryListAdapter(Context context, ArrayList<OrderHistoryListRow> data) {
        Log.d(LOG_TAG, "Start: OrderHistoryListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.n_a_order_history_list_row, viewGroup, false);
        MenuViewHolder holder = new MenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {
        OrderHistoryListRow orderHistoryListRow = data.get(position);

        viewHolder.orderNumber.setText("# " + (data.size() - position));
        viewHolder.orderHistoryDate.setText(orderHistoryListRow.date);
        viewHolder.orderHistoryAddress.setText(orderHistoryListRow.address);
        viewHolder.orderHistoryOrderTime.setText(orderHistoryListRow.orderTime);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<OrderHistoryListRow> data) {
        this.data = data;
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView orderNumber;
        TextView orderHistoryDate;
        TextView orderHistoryAddress;
        TextView orderHistoryOrderTime;

        public MenuViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            orderNumber = (TextView) itemView.findViewById(R.id.order_number);
            orderHistoryDate = (TextView) itemView.findViewById(R.id.order_history_date);
            orderHistoryAddress = (TextView) itemView.findViewById(R.id.order_history_address);
            orderHistoryOrderTime = (TextView) itemView.findViewById(R.id.order_history_order_time);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();

            ((OrderHistoryListActivity) mContext).startOrderHistoryActivity(data.get(position).idx);
        }
    }
}
