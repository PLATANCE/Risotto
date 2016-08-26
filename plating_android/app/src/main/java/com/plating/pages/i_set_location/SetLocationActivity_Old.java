package com.plating.pages.i_set_location;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.application.PlatingActivity;
import com.plating.network.VolleySingleton;
import com.plating.network.i_set_location.GetAvailableLocations;
import com.plating.object.AvailableLocation;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;

import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;

public class SetLocationActivity_Old extends PlatingActivity {
    String LOG_TAG = Constant.APP_NAME + "SetLocationActivity";

    private EditText mSetLocationEditText;
    private RecyclerView mRecyclerView;
    private ImageView mOpenMapImageView;

    private Context cx;

    private boolean signup, searched;
    private int screen;
    public RequestQueue mRequestQueue;
    public VolleySingleton mVolleySingleton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searched = false;
        signup = false;
        signup = getIntent().getBooleanExtra("signup", false);

        screen = getIntent().getIntExtra("screen", 0);

        setContentView(R.layout.b_set_location_activity);
        ImageButton backIB = (ImageButton) findViewById(R.id.tool_bar_backIB);
        if (signup || screen == Constant.screen_no_address) {

            backIB.setVisibility(View.GONE);
        } else {
            backIB.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }

        mVolleySingleton = VolleySingleton.getInstance();
        mRequestQueue = mVolleySingleton.getRequestQueue();

        cx = this;

        getAllViews();
        setOnClickListener();
    }

    public void getAllViews() {
        mSetLocationEditText = (EditText) findViewById(R.id.set_location_edit_text);
        mRecyclerView = (RecyclerView) findViewById(R.id.available_location_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setOnClickListener() {

        mSetLocationEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER) {
                    getAvailableLocationsFromServer();
                    return true;
                } else {
                    return false;
                }
            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!searched) {
            JSONObject props = new JSONObject();
            try {
                props.put("searched", "no");

            } catch (Exception e) {
                e.printStackTrace();
            }
//            MixPanel.getMixPanelInstance().track("(Screen) Address Search", props);
        }
    }


    /***********************
     * * NETWORK OPERATION *
     ***********************/
    public void getAvailableLocationsFromServer() {
        searched = true;
        Log.d("", "search : "+mSetLocationEditText.getText().toString());

        JSONObject props = new JSONObject();
        try {
            props.put("keyword", mSetLocationEditText.getText().toString());

        } catch (Exception e) {
            e.printStackTrace();
        }
//        MixPanel.getMixPanelInstance().track("(Screen) Address Search", props);


        String text = "";
        try {
            text = URLEncoder.encode(mSetLocationEditText.getText().toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        GetAvailableLocations.getDataFromServer(this, mRequestQueue, text);
    }

    public void getAvailableLocationsFromServer_Callback(ArrayList<AvailableLocation> availableLocationArrayList) {
        Debug.d(LOG_TAG, availableLocationArrayList.toString());
        Log.d("", "result : "+availableLocationArrayList.toString());

        if(availableLocationArrayList.size() > 0) {
            // Hide keyboard
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mSetLocationEditText.getWindowToken(), 0);

            mRecyclerView.setVisibility(View.VISIBLE);
            populateRecyclerView(availableLocationArrayList);
        }
    }

    public void populateRecyclerView(ArrayList<AvailableLocation> availableLocationArrayList) {
        // Set recyclerview, which is the listview in this case
        AvailableLocationListAdapter adapter = new AvailableLocationListAdapter(this, availableLocationArrayList);
        mRecyclerView.setAdapter(adapter);
    }
//
//
//    public void startDailyMenuListActivity() {
//        Intent intent = new Intent(mContext, DailyMenuListActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
//    }


    /*********************
     * * SYSTEM OPERATION *
     *********************/
//    @Override
//    public void onClickNavigateBack(View view) {
//        // No slide animation here.
//        finish();
//    }

    public void AddressUpdateFinished() {
        if (signup) {
            Intent intent;
            intent = new Intent(cx, DailyMenuListActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);

            //Apply splash exit (fade out) and main entry (fade in) animation transitions.
            overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
        } else {
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
