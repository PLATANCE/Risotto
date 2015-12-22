package com.plating.pages.s_refer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.s_recommend.GetUserCodeFromServer;

/**
 * Created by home on 15. 12. 21..
 */
public class ReferActivity extends PlatingActivity implements View.OnClickListener {

    private ImageView recommend_image;
    private LinearLayout recommend_layout_kakao;
    private LinearLayout recommend_layout_sms;
    private LinearLayout recommend_layout_url;
    private String recommendText;
    private String userCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_recommend_activity);


        setAllViews();
        getUserCodeFromServer();
    }

    public void setAllViews() {
        recommend_image = (ImageView) findViewById(R.id.recommend_image);
        VolleySingleton.getsInstance().loadImageToImageView(recommend_image, RequestURL.COUPON_IMAGE_URL + "2015.12.15-first_pay_coupon_20000.jpeg");

        recommend_layout_kakao = (LinearLayout) findViewById(R.id.recommend_layout_kakao);
        recommend_layout_kakao.setOnClickListener(this);
        recommend_layout_sms = (LinearLayout) findViewById(R.id.recommend_layout_sms);
        recommend_layout_sms.setOnClickListener(this);
        recommend_layout_url = (LinearLayout) findViewById(R.id.recommend_layout_url);
        recommend_layout_url.setOnClickListener(this);

        recommendText = "셰프의 음식을 집에서!\n" +
                "\n" +
                "맛있고 건강한 쉐프의 요리, 이제 편하게 집에서 저렴한 가격에 드세요!";
    }

    public void getUserCodeFromServer() {
        GetUserCodeFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getUserCodeFromServer_Callback(String userCode) {
        this.userCode = userCode;
        recommendText = recommendText + "\n" + "프로모션 코드 : " + userCode;
    }

    @Override
    public void onClick(View v) {
        if (v == recommend_layout_kakao) {
            recommendMsgForKakao();
        } else if (v == recommend_layout_sms) {
            recommendMsgForSms();
        } else if (v == recommend_layout_url) {
            copyURL();
        }
    }

    public void recommendMsgForKakao() {
        //DialogAPI.showDialog(this, "고객 지원", "카카오톡 눌렸음", "확인", null);
        try {
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText(recommendText);
            kakaoTalkLinkMessageBuilder.addAppButton("'플레이팅' 앱 다운받기",
                    new AppActionBuilder().addActionInfo(AppActionInfoBuilder.createAndroidActionInfoBuilder()
                            .setMarketParam("referrer=kakaotalklink")
                            .build())
                            .addActionInfo(AppActionInfoBuilder.createiOSActionInfoBuilder()
                                    .build())
                            .setUrl("http://www.plating.co.kr") // PC 카카오톡 에서 사용하게 될 웹사이트 주소
                            .build());

//                    요즘 미세먼지가 많다는데 아래 링크 클릭해서 미세미세 앱 써봐요~ 예보도되고 좋아요~ 항상 건강하세요! (안드로이드 전용)
            kakaoLink.sendMessage(kakaoTalkLinkMessageBuilder.build(), mContext);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    public void recommendMsgForSms() {
        //DialogAPI.showDialog(this, "고객 지원", "문자 눌렸음", "확인", null);

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.putExtra("sms_body", recommendText);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void copyURL() {
        //DialogAPI.showDialog(this, "고객 지원", "URL 복사 눌렸음", "확인", null);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("recommendText", recommendText);
        clipboardManager.setPrimaryClip(clipData);
        ToastAPI.showToast("복사 되었습니다.");
    }
}
