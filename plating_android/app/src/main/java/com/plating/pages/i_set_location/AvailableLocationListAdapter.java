package com.plating.pages.i_set_location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.object.AvailableLocation;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.util.SVUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class AvailableLocationListAdapter extends RecyclerView.Adapter<AvailableLocationListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "AvailableLocationListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    public ArrayList<AvailableLocation> data = new ArrayList<AvailableLocation>();
    private static int TYPE_ITEM = 1;
    private static int TYPE_FOOTER = 2;

    public AvailableLocationListAdapter(Context context, ArrayList<AvailableLocation> availableLocationArrayList) {
        Log.d(LOG_TAG, "Start: AvailableLocationListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = availableLocationArrayList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");
        View view;
        if (viewType == TYPE_ITEM) {
            view = inflater.inflate(R.layout.b_available_location_list_row, viewGroup, false);
        } else {
            view = inflater.inflate(R.layout.b_address_delivery_coverage, viewGroup, false);
        }
        MenuViewHolder holder = new MenuViewHolder(view, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {

        if (getItemViewType(position) == TYPE_ITEM) {
            AvailableLocation availableLocation = data.get(position);

            viewHolder.locationName.setText(availableLocation.name);

            if (availableLocation.available) {
                viewHolder.locationName.setTextColor(mContext.getResources().getColor(R.color.grey_700));
            } else {
                viewHolder.locationName.setTextColor(mContext.getResources().getColor(R.color.grey_300));
            }
        }
    }

    @Override
    public int getItemCount() {
        return data.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    public void updateData(ArrayList<AvailableLocation> data) {
        this.data = data;
    }

    public boolean isPositionFooter(int position) {
        return position == data.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView locationName;
        RelativeLayout available_location_layout;
        TextView textview_delivery_coverage;

        public MenuViewHolder(View itemView, int viewType) {
            super(itemView);
            if (viewType == TYPE_ITEM) {
                available_location_layout = (RelativeLayout) itemView.findViewById(R.id.available_location_layout);
                available_location_layout.setOnClickListener(this);
                locationName = (TextView) itemView.findViewById(R.id.location_name);
            } else {
                textview_delivery_coverage = (TextView) itemView.findViewById(R.id.textview_delivery_coverage);
                textview_delivery_coverage.setOnClickListener(this);
            }

        }

        @Override
        public void onClick(View v) {
            if (v == available_location_layout) {
                final int position = getAdapterPosition();
                final AvailableLocation availableLocation = data.get(position);


                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                if (availableLocation.available) {
                    alert.setTitle("ADDRESS");
                    alert.setMessage("나머지 주소를 입력해주세요");
                } else {
                    alert.setTitle("배달이 불가능한 지역입니다.");
                    alert.setMessage("나머지 주소를 입력하고 확인을 누르면 배달 지역 확장시 알려드리겠습니다.");

                }

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(36, 0, 36, 0);

                // Set an EditText view to get user input
                final EditText input = new EditText(mContext);
                layout.addView(input, params);

                alert.setView(layout);

                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String address_d = input.getText().toString();
                        UpdateAddress(availableLocation.name, address_d, availableLocation.lat, availableLocation.lon, availableLocation.available);
                    }
                });

                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                    }
                });

                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();

            } else if (v == textview_delivery_coverage) {
                MixPanel.mixPanel_trackWithOutProperties("Click Show Delivery Coverage");
                ((SetLocationActivity) mContext).moveToAddressCoverActivity();
            }
        }

    }

    private void UpdateAddress(final String address, final String detail, final double lat, final double lon, final boolean isAvailable) {
        RequestQueue queue = Volley.newRequestQueue(mContext);
        StringRequest myReq = new StringRequest(Request.Method.POST,
                "http://api.plating.co.kr/update_info",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("update_phone", "result : " + response);

                        SVUtil.SetDeliveryAreaStatus(mContext, isAvailable);
//                        ((SetLocationActivity) mContext).startDailyMenuListActivity();
//                        ((SetLoca`tionActivity) mContext).finish();
                        ((SetLocationActivity) mContext).AddressUpdateFinished();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }) {

            protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("user_idx", SVUtil.GetUserIdx(mContext) + "");
                params.put("address", address);
                params.put("address_detail", detail);
                params.put("delivery_available", isAvailable ? "1" : "0");
                params.put("lat", lat + "");
                params.put("lon", lon + "");

                return params;
            }
        };
        queue.add(myReq);
    }
}
