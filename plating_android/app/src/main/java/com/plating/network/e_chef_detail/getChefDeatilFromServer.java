package com.plating.network.e_chef_detail;

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

import org.json.JSONArray;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class getChefDeatilFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "getMenuListFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue) {
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Debug.d(LOG_TAG, "getDataFromServer.response: " + response.toString());
//                        ArrayList<DailyMenu> dailyMenuArrayList = convertJsonToDailyMenuArrayList(response);
//                        ((DailyMenuListActivity) context).getMenuListFromServer_Callback(dailyMenuArrayList);
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

    public static String getRequestUrl() {
        return RequestURL.SERVER__DAILY_MENU;
    }
/*
    private static ArrayList<DailyMenu> convertJsonToDailyMenuArrayList(JSONArray response) {
        Log.d(LOG_TAG, "convertJsonToDailyMenuArrayList: Start");
        ArrayList<DailyMenu> dailyMenuArrayList = new ArrayList<DailyMenu>();

        if (response != null || response.length() > 0) {
            // Get full menu
            for (int i = 0; i < response.length(); i++) {
                int idx = response.optJSONObject(i).optInt("idx", -1);
                String nameMenu = response.optJSONObject(i).optString("name_menu", "N/A");
                String imageUrlMenu = response.optJSONObject(i).optString("image_url_menu", "N/A");
                int price = response.optJSONObject(i).optInt("price", -1);
                int idxChef = response.optJSONObject(i).optInt("idx_chef", -1);
                String nameChef = response.optJSONObject(i).optString("name_chef", "N/A");
                String imageUrlChef = response.optJSONObject(i).optString("image_url_chef", "N/A");
                String imageUrlChef2 = response.optJSONObject(i).optString("image_url_chef2", "N/A");
                String career = response.optJSONObject(i).optString("career", "N/A");
                String careerSumm = response.optJSONObject(i).optString("career_summ", "N/A");
                String ingredients = response.optJSONObject(i).optString("ingredients", "N/A");
                String calories = response.optJSONObject(i).optString("calories", "N/A");
                String story = response.optJSONObject(i).optString("story", "N/A");

                DailyMenu dailyMenu = new DailyMenu(idx, nameMenu, imageUrlMenu, price, idxChef, nameChef, imageUrlChef, imageUrlChef2, career,
                        careerSumm, ingredients, calories, story);

                dailyMenuArrayList.add(dailyMenu);
            }
        }
        return dailyMenuArrayList;
    }*/
}
