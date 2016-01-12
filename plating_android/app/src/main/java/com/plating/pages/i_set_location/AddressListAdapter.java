package com.plating.pages.i_set_location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.object.AddressListRow;
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

    private static final int TYPE_ITEM = 1;
    private static final int TYPE_FOOTER = 2;

    public AddressListAdapter(Context context, ArrayList<AddressListRow> data) {
        Log.d(LOG_TAG, "Start: AddressListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public AddressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");
        View view;
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.b_address_list_row, parent, false);
        } else {
            view = inflater.inflate(R.layout.b_address_list_add, parent, false);
        }
        AddressViewHolder holder = new AddressViewHolder(view, viewType);
        return holder;

    }


    @Override
    public void onBindViewHolder(AddressViewHolder holder, int position) {
        Log.d(LOG_TAG, "onBindViewHolder: Start");
        if (getItemViewType(position) == TYPE_ITEM) {
            AddressListRow addressListRow = data.get(position);

            // In_use 에 따른 이미지 변경
            if (addressListRow.getIn_use() == 0) {
                holder.imageView_use_check.setBackgroundResource(R.drawable.address_check_no);
            } else {
                holder.imageView_use_check.setBackgroundResource(R.drawable.address_check_yes);
            }

            // 배달 불가 지역이 경우, 배달 불가 지역 표시 및 텍스트 색깔 변경
            if (addressListRow.getDelivery_available() == 0) {
                holder.textView_reservation_type.setVisibility(View.VISIBLE);
                holder.textView_reservation_type.setText("배달이 불가능한 지역입니다.");
                holder.textView_addr1.setTextColor(mContext.getResources().getColor(R.color.grey_400));
                holder.textView_addr2.setTextColor(mContext.getResources().getColor(R.color.grey_400));
            } else {
                if (addressListRow.getReservation_type().equals("reservation_only")) {
                    holder.textView_reservation_type.setVisibility(View.VISIBLE);
                } else {
                    holder.textView_reservation_type.setVisibility(View.GONE);
                }
            }
            holder.textView_addr1.setText(addressListRow.getAddress());
            holder.textView_addr2.setText(addressListRow.getAddress_detail());
        }
    }

    @Override
    public int getItemViewType(int position) {
        //Log.d(LOG_TAG, "getItemViewType: Start");
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public boolean isPositionFooter(int position) {
        return position == data.size();
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    public void updateData(ArrayList<AddressListRow> data) {
        this.data = data;
    }


    class AddressViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public LinearLayout linear_address;
        public ImageView imageView_use_check;
        public TextView textView_addr1;
        public TextView textView_addr2;
        public TextView textView_reservation_type;
        public TextView textview_delete;
        private LinearLayout linear_add_address;

        public AddressViewHolder(View itemView, int viewType) {
            super(itemView);

            if (viewType == TYPE_ITEM) {
                linear_address = (LinearLayout) itemView.findViewById(R.id.linear_address);
                linear_address.setOnClickListener(this);
                imageView_use_check = (ImageView) itemView.findViewById(R.id.imageView_use_check);
                textView_addr1 = (TextView) itemView.findViewById(R.id.textView_addr1);
                textView_addr2 = (TextView) itemView.findViewById(R.id.textView_addr2);
                textView_reservation_type = (TextView) itemView.findViewById(R.id.textView_reservation_type);
                textview_delete = (TextView) itemView.findViewById(R.id.textview_delete);
                textview_delete.setOnClickListener(this);
            } else {
                linear_add_address = (LinearLayout) itemView.findViewById(R.id.linear_add_address);
                linear_add_address.setOnClickListener(this);
            }

        }


        @Override
        public void onClick(View v) {
            if (v == linear_address) {
                MixPanel.mixPanel_trackWithOutProperties("Change Address");
                ((AddressListActivity) mContext).updateAddress(data.get(getAdapterPosition()).getIdx(), "update");
            } else if (v == textview_delete) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("주소 삭제");
                alert.setMessage("주소를 삭제하시겠습니까?");
                alert.setCancelable(false);

                alert.setPositiveButton("삭제하기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        MixPanel.mixPanel_trackWithOutProperties("Delete Address");
                        ((AddressListActivity) mContext).updateAddress(data.get(getAdapterPosition()).getIdx(), "delete");
                        dialog.dismiss();
                    }
                });

                alert.setNegativeButton("닫기", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            } else if (v == linear_add_address) {
                MixPanel.mixPanel_trackWithOutProperties("Edit Address");

                Intent intent = new Intent(mContext, SetLocationActivity.class);
                mContext.startActivity(intent);
            }
        }
    }
}
