package com.plating.pages.i_set_location;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.network.i_set_location.GetAddressListFromServer;
import com.plating.object.AddressListRow;
import com.plating.util.PreCachingLayoutManager;

import java.util.ArrayList;

/**
 * Created by home on 16. 1. 4..
 */
public class AddressListActivity extends PlatingActivity{
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
}
