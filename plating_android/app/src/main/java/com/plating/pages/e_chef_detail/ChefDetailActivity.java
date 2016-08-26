package com.plating.pages.e_chef_detail;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.plating.R;
import com.plating.application.Constant;
import com.plating.application.PlatingActivity;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;

public class ChefDetailActivity extends PlatingActivity {
    // Views that contain chef information
    private ImageView mChefImageView;
    private TextView mChefNameTextView;
    private TextView mChefCareerSummaryTextView;
    private TextView mChefCareerTextView;

    // Chef information
    private String mChefImageUrl;
    private String mChefName;
    private String mChefCareer;
    private String mChefCareerSummary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.f_chef_detail_activity);

        getAllViews();
        getAllIntentExtras();
        setAllDataToViews();
    }

    public void getAllViews() {
        mChefImageView = (ImageView) findViewById(R.id.chef_image);
        mChefNameTextView = (TextView) findViewById(R.id.chef_name);
        mChefCareerSummaryTextView = (TextView) findViewById(R.id.chef_career_summary);
        mChefCareerTextView = (TextView) findViewById(R.id.chef_career);
    }

    public void getAllIntentExtras() {
        Intent intent = getIntent();
        mChefImageUrl = intent.getStringExtra(Constant.CHEF_IMAGE_URL);
        mChefName = intent.getStringExtra(Constant.CHEF_NAME);
        mChefCareer = intent.getStringExtra(Constant.CHEF_CAREER);
        mChefCareerSummary = intent.getStringExtra(Constant.CHEF_CAREER_SUMMARY);
    }

    public void setAllDataToViews() {
        VolleySingleton.getInstance().loadImageToImageView(mChefImageView, RequestURL.CHEF_IMAGE_URL + mChefImageUrl);
        mChefNameTextView.setText(mChefName);
        mChefCareerSummaryTextView.setText(mChefCareerSummary);
        mChefCareerTextView.setText(mChefCareer);
    }
}
