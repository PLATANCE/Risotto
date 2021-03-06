package com.plating.network;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.object.DailyMenu;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;

import org.json.JSONArray;

import java.util.ArrayList;

/**
 * Created by redjjol on 18/11/2015.
 */
public class GetMyPointFromServer {
//    public static String LOG_TAG = Constant.APP_NAME + "getMenuListFromServer";

    public static void GetMyPointFromServer(final Context context, RequestQueue requestQueue) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
//                        Debug.d(LOG_TAG, "getMenuListFromServer.response: " + response.toString());
                        ArrayList<DailyMenu> dailyMenuArrayList = convertJsonToDailyMenuArrayList(response);
                        ((DailyMenuListActivity) context).getMenuListFromServer_Callback(dailyMenuArrayList);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
//                        Debug.d(LOG_TAG, "getMenuListFromServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl() {
        return RequestURL.SERVER__DAILY_MENU;
    }

    private static ArrayList<DailyMenu> convertJsonToDailyMenuArrayList(JSONArray response) {
//        Debug.d(LOG_TAG, "convertJsonToDailyMenuArrayList: Start");
        ArrayList<DailyMenu> dailyMenuArrayList = new ArrayList<>();

        if (response != null || response.length() > 0) {
            for (int i = 0; i < response.length(); i++) {
                DailyMenu dailyMenu = new DailyMenu();
                dailyMenu.menu_d_idx = response.optJSONObject(i).optInt("idx", -1);

            }
        }

        return dailyMenuArrayList;
    }
}
