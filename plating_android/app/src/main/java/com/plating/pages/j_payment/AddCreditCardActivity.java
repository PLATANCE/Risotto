package com.plating.pages.j_payment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.util.SVUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by redjjol on 21/09/15.
 */
public class AddCreditCardActivity extends PlatingActivity {

    private WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_addcard);

        wv = (WebView) findViewById(R.id.act_addcard_webView);

        this.wv.getSettings().setDefaultTextEncodingName("euc-kr");
        wv.getSettings().setJavaScriptEnabled(true);
        wv.setWebChromeClient(new WebChromeClient());
        wv.setWebViewClient(new myWebClient());

        if (Build.VERSION.SDK_INT <= 18) {
            this.wv.getSettings().setSavePassword(false);
        } else {

        }

        AndroidBridge localAndroidBridge = new AndroidBridge(this);
        wv.addJavascriptInterface(localAndroidBridge, "android");


        SimpleDateFormat localSimpleDateFormat1 = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar localCalendar = Calendar.getInstance();
        String mid = "plating001";
        String orderid = System.currentTimeMillis()+"_"+ SVUtil.getUserIdx(this);
        String timestamp = localSimpleDateFormat1.format(localCalendar.getTime());
        String merchantkey = "V0pLcWJOR2RpYWx6ZjBxMUkzRXRVQT09";
        String hashdata = SVUtil.sha256(mid + orderid + timestamp + merchantkey);

        String params = "";
        params += "mid="+"plating001";
        params += "&hashdata="+hashdata;

        PackageManager localPackageManager = getPackageManager();
        int versionCode = 0;
        try {
            versionCode = localPackageManager.getPackageInfo(getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException localNameNotFoundException) {
            while (true)
            {
                localNameNotFoundException.printStackTrace();
                versionCode = 0;
            }
        }

        StringBuilder localStringBuilder = new StringBuilder();
        localStringBuilder.append("mid=").append("plating001").append("&buyername=").append(SVUtil.getUserIdx(this));
        localStringBuilder.append("&goodname=플레이팅 카드등록&price=0&orderid=").append(orderid).append("&returnurl=").append("http://api.plating.co.kr/payResult");
        localStringBuilder.append("&timestamp=").append(timestamp).append("&period=").append("").append("&p_noti=").append(SVUtil.getUserIdx(this) + "-" + versionCode).append("&hashdata=").append(hashdata);

        loadPage("http://inilite.inicis.com/inibill/inibill_card.jsp", localStringBuilder.toString());
//        wv.loadUrl("http://api.plating.co.kr/payResult.php");
    }

    // Server Calls this method
    public class AndroidBridge {
        private Context mContext;
        public AndroidBridge(Context cx) {
            mContext = cx;
        }

        @JavascriptInterface
        public void payResult(String bk_len, String paramString2, String paramString3)
        {
            Log.d("AddCreditCardActivity", bk_len + paramString2 + paramString3);
            if (Integer.parseInt(bk_len) != 40){
                Toast.makeText(mContext, "카드 등록 실패", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(mContext, "카드 등록 완료", Toast.LENGTH_SHORT).show();
            }
            Log.d("payResult", "payResult!!!!");

            AddCreditCardActivity.this.finish();

        }
    }
    public class WebChromeClient extends android.webkit.WebChromeClient
    {
        public WebChromeClient()
        {
        }

        public boolean onCreateWindow(WebView paramWebView, boolean paramBoolean1, boolean paramBoolean2, Message paramMessage)
        {
            return super.onCreateWindow(paramWebView, paramBoolean1, paramBoolean2, paramMessage);
        }

        public boolean onJsAlert(WebView paramWebView, String paramString1, String paramString2, final JsResult paramJsResult)
        {
            new AlertDialog.Builder(paramWebView.getContext()).setTitle("")
                    .setMessage(paramString2)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            paramJsResult.confirm();
                        }
                    })
                    .setCancelable(false).create().show();
            return true;
        }

        public boolean onJsConfirm(WebView paramWebView, String paramString1, String paramString2, JsResult paramJsResult)
        {

            return true;
        }
    }

    public class myWebClient extends WebViewClient
    {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            // TODO Auto-generated method stub
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            // TODO Auto-generated method stub

            view.loadUrl(url);
            return true;

        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && wv.canGoBack()) {
            wv.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void loadPage(String paramString1, String paramString2)
    {
        Log.d("AddCreditCardActivity", paramString1 + paramString2);
        wv.postUrl(paramString1, SVUtil.getBytes(paramString2, "BASE64"));
    }
}
