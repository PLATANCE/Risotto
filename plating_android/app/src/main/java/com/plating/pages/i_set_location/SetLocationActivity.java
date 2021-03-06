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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.Debug;
import com.plating.application.PlatingActivity;
import com.plating.network.i_set_location.GetAvailableLocations;
import com.plating.object.AvailableLocation;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.sdk_tools.mix_panel.MixPanelProperty;

import org.w3c.dom.Text;

import java.net.URLEncoder;
import java.util.ArrayList;

public class SetLocationActivity extends PlatingActivity {
    String LOG_TAG = Constant.APP_NAME + "SetLocationActivity";

    private LinearLayout mSetLocationLinearLayout;
    private EditText mSetLocationEditText;
    private RecyclerView mRecyclerView;
    private AvailableLocationListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_set_location_activity);

        getAllViews();

        setUpRecyclerView();

        setOnClickListener();
    }

    public void getAllViews() {
        mSetLocationLinearLayout = (LinearLayout) findViewById(R.id.set_location_linear_layout);

        mSetLocationEditText = (EditText) findViewById(R.id.set_location_edit_text);
        Log.d(LOG_TAG, mSetLocationEditText.getBackground().toString());
        mRecyclerView = (RecyclerView) findViewById(R.id.available_location_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setUpRecyclerView() {
        mAdapter = new AvailableLocationListAdapter(this, new ArrayList<AvailableLocation>());
        mRecyclerView.setAdapter(mAdapter);
    }

    public void moveToAddressCoverActivity() {
        Intent intent = new Intent(this, AddressCoverActivity.class);
        startActivity(intent);
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
        mSetLocationLinearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getAvailableLocationsFromServer();
            }
        });
    }


    /***********************
     * * NETWORK OPERATION *
     ***********************/
    public void getAvailableLocationsFromServer() {
        Log.d("", "search : "+mSetLocationEditText.getText().toString());
        ArrayList<MixPanelProperty> mixPanelPropertyArrayList = new ArrayList<>();
        mixPanelPropertyArrayList.add(new MixPanelProperty("Keyword", mSetLocationEditText.getText().toString()));
        MixPanel.mixPanel_trackWithProperties("Search Address", mixPanelPropertyArrayList);

        String text = "";
        try {
            text = URLEncoder.encode(mSetLocationEditText.getText().toString(), "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (mSetLocationEditText.getText().toString().length() > 0) {
            GetAvailableLocations.getDataFromServer(this, mRequestQueue, text);
        }
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
        //AvailableLocationListAdapter adapter = new AvailableLocationListAdapter(this, availableLocationArrayList);
        mAdapter.updateData(availableLocationArrayList);
        mAdapter.notifyDataSetChanged();
    }

    public void AddressUpdateFinished() {
        finish();
    }
}
