package com.plating.pages.i_set_location;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.plating.R;
import com.plating.application.PlatingActivity;

/**
 * Created by home on 16. 1. 5..
 */
public class DeliveryCoverageActivity extends PlatingActivity {
    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.b_delivery_coverage_activity);

        webView = (WebView) findViewById(R.id.webView);
        WebSettings set = webView.getSettings();
        set.setJavaScriptEnabled(true);
        set.setBuiltInZoomControls(true);
        webView.loadUrl("http://plating.co.kr/app/media/chef/2015.11.06-chef_andrew_big.png");
    }
}
