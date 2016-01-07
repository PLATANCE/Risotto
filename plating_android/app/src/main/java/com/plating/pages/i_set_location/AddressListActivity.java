package com.plating.pages.i_set_location;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.i_set_location.GetAddressListFromServer;
import com.plating.network.i_set_location.SetAddressToServer;
import com.plating.object.AddressListRow;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.util.PreCachingLayoutManager;

import java.util.ArrayList;

/**
 * Created by home on 16. 1. 4..
 */
public class AddressListActivity extends PlatingActivity {
    private RecyclerView mRecyclerView;
    private AddressListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_address_list_activity);


        getAllView();
        setUpRecyclerView();
        getAddressListFromServer();
    }

    public void getAllView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.address_list_recycler_view);
        mRecyclerView.setLayoutManager(new PreCachingLayoutManager(this));
    }

    public void setUpRecyclerView() {
        mAdapter = new AddressListAdapter(this, new ArrayList<AddressListRow>());
        mRecyclerView.setAdapter(mAdapter);
    }


    public void getAddressListFromServer() {
        GetAddressListFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getAddressListFromServer_Callback(ArrayList<AddressListRow> addressListRows) {
        Log.d(LOG_TAG, "getAddressListFromServer_Callback: size = " + addressListRows.size());
        mAdapter.updateData(addressListRows);
        mAdapter.notifyDataSetChanged();
    }

    public void updateAddress(int idx, String mode) {
        SetAddressToServer.sendDataToServer(mContext, mRequestQueue, idx, mode);
    }


    public void updateAddress_Callback(String msg) {
        ToastAPI.showToast(msg);
        onResume();
    }

    @Override
    protected void onResume() {
        super.onResume();
        getAddressListFromServer();
        mAdapter.notifyDataSetChanged();
    }
}
