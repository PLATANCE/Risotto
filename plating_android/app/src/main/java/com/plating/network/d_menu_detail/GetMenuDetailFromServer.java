package com.plating.network.d_menu_detail;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.network.RequestURL;
import com.plating.object.SingleMenu;
import com.plating.object.CustomerReview;
import com.plating.pages.d_menu_detail.MenuDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by cheehoonha on 7/17/15.
 */
public class GetMenuDetailFromServer {
    public static String LOG_TAG = Constant.APP_NAME + "getMenuDetailFromServer";

    public static void getDataFromServer(final Context context, RequestQueue requestQueue, final int menuIdx) {
        Log.d("", "menu_idx : " + menuIdx);
        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                getRequestUrl(menuIdx),
                createJsonForPost(menuIdx),
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Debug.d(LOG_TAG, "getMenuDetailFromServer.menuidx: " + menuIdx);
                        Debug.d(LOG_TAG, "getMenuDetailFromServer.response: " + response.toString());
                        SingleMenu singleMenu = convertJsonToMenuDetail(response);
                        ((MenuDetailActivity) context).getMenuDetailFromServer_Callback(singleMenu);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Debug.d(LOG_TAG, "getMenuDetailFromServer.error: " + error.toString());
                        Toast.makeText(context, Constant.SERVER_CONNECTION_FAILURE, Toast.LENGTH_LONG).show();
                    }
                }
        );
        requestQueue.add(request);
    }

    public static String getRequestUrl(int menu_idx) {
        return RequestURL.SERVER__GET_MENU_DETAIL + "?menu_idx=" + menu_idx;
    }

    public static JSONObject createJsonForPost(int menuIdx) {
        JSONObject json = new JSONObject();
        try {
            json.put("menu_idx", menuIdx);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return json;
    }

    private static SingleMenu convertJsonToMenuDetail(JSONArray response) {
        Debug.d(LOG_TAG, "convertJsonToMenuDetail: Start");

        if (response != null || response.length() > 0) {
            JSONObject menuJsonObject = response.optJSONObject(0);

            SingleMenu singleMenu = new SingleMenu();

            singleMenu.idx = menuJsonObject.optInt("idx", -1);
            singleMenu.nameMenu = menuJsonObject.optString("name_menu", "N/A");
            singleMenu.nameMenu_eng = menuJsonObject.optString("name_menu_eng", "N/A");

            singleMenu.imageUrlMenu = menuJsonObject.optString("image_url_menu", "N/A");
            singleMenu.price = menuJsonObject.optInt("price", -1);
            singleMenu.altPrice = menuJsonObject.optInt("alt_price", -1);
            singleMenu.idxChef = menuJsonObject.optInt("idx_chef", -1);
            singleMenu.nameChef = menuJsonObject.optString("name_chef", "N/A");
            singleMenu.imageUrlChef = menuJsonObject.optString("image_url_chef", "N/A");
            singleMenu.imageUrlChef2 = menuJsonObject.optString("image_url_chef2", "N/A");
            singleMenu.career = menuJsonObject.optString("career", "N/A");
            singleMenu.careerSumm = menuJsonObject.optString("career_summ", "N/A");
            singleMenu.ingredients = menuJsonObject.optString("ingredients", "N/A");
            singleMenu.calories = menuJsonObject.optString("calories", "N/A");
            singleMenu.story = menuJsonObject.optString("story", "N/A");
            singleMenu.rating = menuJsonObject.optDouble("rating", 4.5);
            singleMenu.numOfReviews = menuJsonObject.optInt("review_count", 34);

            ArrayList<CustomerReview> customerReviewArrayList = new ArrayList<>();
            JSONArray reviewArray = menuJsonObject.optJSONArray("review");
            for (int i = 0; i < reviewArray.length(); i++) {
                CustomerReview customerReview = new CustomerReview();
                customerReview.idx = reviewArray.optJSONObject(i).optInt("idx", -1);
                customerReview.rating = reviewArray.optJSONObject(i).optDouble("rating", 4.5);
                customerReview.comment = reviewArray.optJSONObject(i).optString("comment", "");
                customerReview.time = reviewArray.optJSONObject(i).optString("rated_time", "");
                customerReview.mobile = reviewArray.optJSONObject(i).optString("mobile", "");
                customerReviewArrayList.add(customerReview);
            }
            singleMenu.reviewArrayList = customerReviewArrayList;

            return singleMenu;
        }

        // This should not be reached. In the case it is reached, return empty object
        return new SingleMenu();
    }
}
