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
import com.plating.network.s_recommend.GetUserCodeFromServer;

/**
 * Created by home on 15. 12. 21..
 */
public class ReferActivity extends PlatingActivity implements View.OnClickListener {

    private ImageView refer_image_top;
    private LinearLayout recommend_layout_kakao;
    private LinearLayout recommend_layout_sms;
    private LinearLayout recommend_layout_url;
    private String refer_text;
    private TextView user_code;
    private TextView detail_text;
    private SpannableString content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.s_refer_activity);


        setAllViews();
        getUserCodeFromServer();
    }

    public void setAllViews() {
        refer_image_top = (ImageView) findViewById(R.id.refer_image_top);

        VolleySingleton.getsInstance().loadImageToImageView(refer_image_top, RequestURL.REFER_IMAGE_URL + "2015.12.23-refer_top.jpg");

        recommend_layout_kakao = (LinearLayout) findViewById(R.id.recommend_layout_kakao);
        recommend_layout_kakao.setOnClickListener(this);
        recommend_layout_sms = (LinearLayout) findViewById(R.id.recommend_layout_sms);
        recommend_layout_sms.setOnClickListener(this);
        recommend_layout_url = (LinearLayout) findViewById(R.id.recommend_layout_url);
        recommend_layout_url.setOnClickListener(this);

        // user_code
        user_code = (TextView) findViewById(R.id.user_code);

        // 자세히 underLine
        detail_text = (TextView) findViewById(R.id.detail_text);
        content = new SpannableString(detail_text.getText().toString());
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        detail_text.setText(content);
        detail_text.setOnClickListener(this);

        refer_text = "친구들에게 플레이팅을 알려주세요.\n친구가 코드를 입력하면 10,000원이 적립되고\n첫 주문시 나에게도 10,000원이 적립됩니다!";
    }

    public void getUserCodeFromServer() {
        GetUserCodeFromServer.getDataFromServer(this, mRequestQueue);
    }

    public void getUserCodeFromServer_Callback(String userCode) {
        user_code.setText(userCode);
        refer_text += "프로모션 코드 : " + userCode;
    }

    @Override
    public void onClick(View v) {
        if (v == recommend_layout_kakao) {
            recommendMsgForKakao();
        } else if (v == recommend_layout_sms) {
            recommendMsgForSms();
        } else if (v == recommend_layout_url) {
            copyURL();
        } else if(v == detail_text) {
            DialogAPI.showDialog(this, "친구 추천!!", refer_text, "닫기", null);
        }
    }

    public void recommendMsgForKakao() {
        //DialogAPI.showDialog(this, "고객 지원", "카카오톡 눌렸음", "확인", null);
        try {
            KakaoLink kakaoLink = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
            kakaoTalkLinkMessageBuilder.addText(refer_text);
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
        intent.putExtra("sms_body", refer_text);
        intent.setType("vnd.android-dir/mms-sms");
        startActivity(intent);
    }

    public void copyURL() {
        //DialogAPI.showDialog(this, "고객 지원", "URL 복사 눌렸음", "확인", null);
        ClipboardManager clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clipData = ClipData.newPlainText("refer_text", refer_text);
        clipboardManager.setPrimaryClip(clipData);
        ToastAPI.showToast("복사 되었습니다.");
    }
}
