package com.plating.pages.c_daily_menu_list;

import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.plating.R;
import com.plating.application.PlatingActivity;

/**
 * Created by home on 16. 1. 12..
 */
public class MainBannerActivity extends PlatingActivity {
    private WebView webView;
    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.d_daily_menu_list_banner_activity);

        webView = (WebView) findViewById(R.id.webView);
        webSettings = webView.getSettings();

        webSettings.setSaveFormData(false);
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);


        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.clearCache(true);
        webView.loadUrl("http://plating.co.kr/admin/admin_banner_main.php");
    }
}
