package com.plating.pages.i_set_location;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.object.AddressListRow;
import com.plating.object.CouponListRow;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;

import java.util.ArrayList;

/**
 * Created by home on 16. 1. 4..
 */
public class AddressListAdapter extends RecyclerView.Adapter<AddressListAdapter.AddressViewHolder> {
    public String LOG_TAG = Constant.APP_NAME + "AddressListAdapter";

    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<AddressListRow> data = new ArrayList<>();

    public AddressListAdapter(Context context, ArrayList<AddressListRow> data) {
        Log.d(LOG_TAG, "Start: AddressListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.b_address_list_row, parent, false);
        AddressViewHolder holder = new AddressViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        AddressListRow addressListRow = data.get(position);
        holder.textView_addr1.setText(addressListRow.getAddress());
        holder.textView_addr2.setText(addressListRow.getAddress_detail());
        if (addressListRow.getIn_use() == 0) {
            holder.imageView_use_check.setImageResource(R.drawable.address_check_no);
        } else {
            holder.imageView_use_check.setImageResource(R.drawable.address_check_yes);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<AddressListRow> data) {
        this.data = data;
    }

    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView imageView_use_check;
        public TextView textView_addr1;
        public TextView textView_addr2;
        public TextView textView_delete;

        public AddressViewHolder(View itemView) {
            super(itemView);

            imageView_use_check = (ImageView) itemView.findViewById(R.id.imageView_use_check);
            imageView_use_check.setOnClickListener(this);
            textView_addr1 = (TextView) itemView.findViewById(R.id.textView_addr1);
            textView_addr2 = (TextView) itemView.findViewById(R.id.textView_addr2);
            textView_delete = (TextView) itemView.findViewById(R.id.textView_delete);
            textView_delete.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (v == imageView_use_check) {
                Log.d(LOG_TAG, "imageView_use_check Checked");
            } else if (v == textView_delete) {
                Log.d(LOG_TAG, "textView_delete Checked");
            }
        }
    }
}
