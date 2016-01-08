package com.plating.network.i_set_location;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.object.AddressListRow;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.util.SVUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by home on 16. 1. 5..
 */
public class GetAddressInUseFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetAddressInUseFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.GetUserIdx(context)),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString() + " length :" + response.length());
                        AddressListRow addressListRow = convertJsonToAddressListRow(response);

                        ((DailyMenuListActivity) context).getAddressInUseFromServer_Callback(addressListRow);

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "getDataFromServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl(int userIdx) {
        return RequestURL.SERVER__GET_ADDRESS_INUSE + "?user_idx=" + userIdx;
    }

    public static AddressListRow convertJsonToAddressListRow(JSONArray response) {
        if (response != null && response.length() > 0) {
            JSONObject addressJsonObject = response.optJSONObject(0);

            AddressListRow addressListRow = new AddressListRow();

            addressListRow.setIdx(addressJsonObject.optInt("idx", -1));
            addressListRow.setAddress(addressJsonObject.optString("address", ""));
            addressListRow.setAddress_detail(addressJsonObject.optString("address_detail", ""));
            addressListRow.setIn_use(addressJsonObject.optInt("in_use", -1));
            addressListRow.setReservation_type(addressJsonObject.optString("reservation_type", ""));

            return addressListRow;
        }
        return new AddressListRow();
    }
}
