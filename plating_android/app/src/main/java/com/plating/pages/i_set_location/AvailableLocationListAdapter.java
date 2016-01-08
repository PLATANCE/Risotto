package com.plating.pages.i_set_location;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
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

    public AvailableLocationListAdapter(Context context, ArrayList<AvailableLocation> availableLocationArrayList) {
        Log.d(LOG_TAG, "Start: AvailableLocationListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = availableLocationArrayList;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");

        View view = inflater.inflate(R.layout.b_available_location_list_row, viewGroup, false);
        MenuViewHolder holder = new MenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final MenuViewHolder viewHolder, int position) {

        AvailableLocation availableLocation = data.get(position);

        viewHolder.locationName.setText(availableLocation.name);

        if (availableLocation.available) {
            viewHolder.locationName.setTextColor(mContext.getResources().getColor(R.color.grey_700));
        } else {
            viewHolder.locationName.setTextColor(mContext.getResources().getColor(R.color.grey_300));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView locationName;
        InputMethodManager imm;

        public MenuViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            locationName = (TextView) itemView.findViewById(R.id.location_name);
        }

        @Override
        public void onClick(View v) {
            final int position = getAdapterPosition();
            final AvailableLocation availableLocation = data.get(position);

            if (availableLocation.available) {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);

                alert.setTitle("ADDRESS");
                alert.setMessage("나머지 주소를 입력해주세요");

                LinearLayout layout = new LinearLayout(mContext);
                layout.setOrientation(LinearLayout.VERTICAL);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
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

                alert.setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                            }
                        });

                AlertDialog dialog = alert.create();
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
                dialog.show();
            } else {
                AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
                alert.setTitle("배달 불가 지역");
                alert.setMessage("현재 배달 가능한 지역이 아닙니다.");

                alert.setPositiveButton("다시선택", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.setNegativeButton("확인",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                UpdateAddress(availableLocation.name, "", availableLocation.lat, availableLocation.lon, availableLocation.available);
                            }
                        });

                alert.show();
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
