package com.plating.pages.s_refer;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kakao.kakaolink.AppActionBuilder;
import com.kakao.kakaolink.AppActionInfoBuilder;
import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.plating.R;
import com.plating.application.PlatingActivity;
import com.plating.helperAPI.DialogAPI;
import com.plating.helperAPI.ToastAPI;
import com.plating.network.RequestURL;
import com.plating.network.VolleySingleton;
import com.plating.network.s_recommend.GetPolicyReferPointFromServer;
import com.plating.network.s_recommend.GetUserCodeFromServer;

/**
 * Created by home on 15. 12. 21..
 */
public class ReferActivity extends PlatingActivity implements View.OnClickListener {

    private ImageView refer_image_top;
    private LinearLayout recommend_layout_kakao;
    private LinearLayout recommend_layout_sms;
    private LinearLayout recommend_layout_url;
    private TextView user_code;
    private String refer_text;

    private String refer_link;
    private String refer_code;
    private String numReferPoint;
    private String korReferPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_refer_activity);


        setAllViews();
        getUserCodeFromServer();
        getPolicyReferPointFromServer();
    }

    public void setAllViews() {
        refer_image_top = (ImageView) findViewById(R.id.refer_image_top);

        VolleySingleton.getsInstance().loadImageToImageView(refer_image_top, RequestURL.REFER_IMAGE_URL + "new_refer_friend.png");

        recommend_layout_kakao = (LinearLayout) findViewById(R.id.recommend_layout_kakao);
        recommend_layout_kakao.setOnClickListener(this);
        recommend_layout_sms = (LinearLayout) findViewById(R.id.recommend_layout_sms);
        recommend_layout_sms.setOnClickListener(this);
        recommend_layout_url = (LinearLayout) findViewById(R.id.recommend_layout_url);
        recommend_layout_url.setOnClickListener(this);

        // user_code
        user_code = (TextView)findViewById(R.id.user_code);
        numReferPoint = "";
        korReferPoint = "";

        refer_link = "http://goo.gl/t5lrSL";
    }

    public void getUserCodeFromServer() {
        GetUserCodeFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getUserCodeFromServer_Callback(String userCode) {
        user_code.setText(userCode);
        this.refer_code = userCode;
    }

    public void getPolicyReferPointFromServer() {
        GetPolicyReferPointFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getPolicyReferPointFromServer_Callback(String numReferPoint, String korReferPoint) {
        this.numReferPoint = numReferPoint;
        this.korReferPoint = korReferPoint;

        refer_text = "집에서 간편하게 셰프의 요리를 즐겨요!\n 지금 플레이팅 앱을 다운받고 첫끼를 무료로 맛보세요!\n" +
                "[추천 코드: " + refer_code + "]";
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
            kakaoTalkLinkMessageBuilder.addText(refer_text + "\n[추천 코드: " + refer_code + "]");
            kakaoTalkLinkMessageBuilder.addAppButton("집에서 셰프의 요리를!",
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
        intent.putExtra("sms_body", refer_text + "\n다운로드 링크: " + refer_link);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void copyURL() {
        //DialogAPI.showDialog(this, "고객 지원", "URL 복사 눌렸음", "확인", null);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("refer_text", refer_text + "\n다운로드 링크: " + refer_link);
        clipboardManager.setPrimaryClip(clipData);
        ToastAPI.showToast("복사 되었습니다.");
    }
}
