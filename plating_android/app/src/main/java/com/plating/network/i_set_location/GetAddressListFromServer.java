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
import com.plating.pages.i_set_location.AddressListActivity;
import com.plating.util.SVUtil;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by home on 16. 1. 4..
 */
public class GetAddressListFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "GetAddressListFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(SVUtil.getUserIdx(context)),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
                        ArrayList<AddressListRow> addressListRowArrayList = convertJsonToAddressList(response);


                        ((AddressListActivity) context).getAddressListFromServer_Callback(addressListRowArrayList);
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
        return RequestURL.SERVER__GET_ADDRESS_LIST + "?user_idx=" + userIdx;
    }

    private static ArrayList<AddressListRow> convertJsonToAddressList(JSONArray response) {
        Debug.d(LOG_TAG, "convertJsonToAddressList: Start");
        ArrayList<AddressListRow> addressListRows = new ArrayList<>();

        if (response != null || response.length() > 0) {

            for (int i = 0; i < response.length(); i++) {
                AddressListRow addressListRow = new AddressListRow();

                addressListRow.setIdx(response.optJSONObject(i).optInt("idx", -1));
                addressListRow.setAddress(response.optJSONObject(i).optString("address", ""));
                addressListRow.setAddress_detail(response.optJSONObject(i).optString("address_detail", ""));
                addressListRow.setIn_use(response.optJSONObject(i).optInt("in_use", -1));
                addressListRow.setReservation_type(response.optJSONObject(i).optString("reservation_type", ""));
                addressListRow.setDelivery_available(response.optJSONObject(i).optInt("delivery_available", -1));

                addressListRows.add(addressListRow);
            }
        }

        return addressListRows;
    }
}
