package com.plating.pages.z_other;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

import com.plating.R;

/**
 * Created by redjjol on 24/09/15.
 */
public class ActWebView extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_webview);

        openWebView();
    }

    public void openWebView() {
        // Get URL string from intent and open the website
        WebView webView = (WebView) findViewById(R.id.act_webview_wv);
        webView.loadUrl(getIntent().getStringExtra("url"));
    }

}