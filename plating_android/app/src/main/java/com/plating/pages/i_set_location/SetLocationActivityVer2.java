package com.plating.pages.i_set_location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.pages.i_set_location.dialog.SetLocationDialog;
import com.plating.pages.c_daily_menu_list.DailyMenuListActivity;

public class SetLocationActivityVer2 extends PlatingActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_set_location_activity_ver2);

        getAllViews();
        setAreaButtonListeners();
    }

    public void getAllViews() {
    }

    public void setAreaButtonListeners() {
        Button button1 = (Button) findViewById(R.id.area_button_1);
        Button button2 = (Button) findViewById(R.id.area_button_2);
        Button button3 = (Button) findViewById(R.id.area_button_3);
        Button button4 = (Button) findViewById(R.id.area_button_4);
        Button button5 = (Button) findViewById(R.id.area_button_5);
        Button button6 = (Button) findViewById(R.id.area_button_6);
        Button button7 = (Button) findViewById(R.id.area_button_7);
        Button button8 = (Button) findViewById(R.id.area_button_8);
        Button button9 = (Button) findViewById(R.id.area_button_9);
        button1.setOnClickListener(areaButtonClickHandler);
        button2.setOnClickListener(areaButtonClickHandler);
        button3.setOnClickListener(areaButtonClickHandler);
        button4.setOnClickListener(areaButtonClickHandler);
        button5.setOnClickListener(areaButtonClickHandler);
        button6.setOnClickListener(areaButtonClickHandler);
        button7.setOnClickListener(areaButtonClickHandler);
        button8.setOnClickListener(areaButtonClickHandler);
        button9.setOnClickListener(areaButtonClickHandler);
    }

    View.OnClickListener areaButtonClickHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Log.d(LOG_TAG, "areaButtonClickHandler: Button Clicked");
            SetLocationDialog.showDialog(mContext, ((Button) v).getText().toString());
        }
    };

    /*********************
     * * SYSTEM OPERATION *
     *********************/
    public void AddressUpdateFinished() {
        Intent intent;
        intent = new Intent(this, DailyMenuListActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        //Apply splash exit (fade out) and main entry (fade in) animation transitions.
        overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
    }
}
