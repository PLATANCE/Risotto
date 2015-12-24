package com.plating.pages.n_navigation_drawer;


import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.plating.R;
import com.plating.application.Constant;
import com.plating.helperAPI.DialogAPI;
import com.plating.pages.q_promotion.RegisterPromotionActivity;
import com.plating.pages.r_coupon.MyCouponListActivity;
import com.plating.pages.s_refer.ReferActivity;
import com.plating.sdk_tools.mix_panel.MixPanel;
import com.plating.object.NavigationDrawerMenu;
import com.plating.pages.o_order_history.OrderHistoryListActivity;

import java.util.Collections;
import java.util.List;

/**
 * Created by cheehoonha on 10/6/14.
 */
public class NavigationDrawerListAdapter extends RecyclerView.Adapter<NavigationDrawerListAdapter.MenuViewHolder> {

    public String LOG_TAG = Constant.APP_NAME + "NavigationDrawerListAdapter";
    private Context mContext;
    private LayoutInflater inflater;
    List<NavigationDrawerMenu> data = Collections.emptyList();

    public NavigationDrawerListAdapter(Context context, List<NavigationDrawerMenu> data) {
        Log.d(LOG_TAG, "Start: ShopRecyclerListAdapter");

        mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        Log.d(LOG_TAG, "onCreateViewHolder: Start");
        View view = inflater.inflate(R.layout.n_left_navigation_drawer_row, viewGroup, false);
        MenuViewHolder holder = new MenuViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MenuViewHolder viewHolder, int position) {
        NavigationDrawerMenu current = data.get(position);
        viewHolder.image.setImageResource(current.iconId);
        viewHolder.title.setText(current.title);
        viewHolder.moreInfo.setText(current.moreInfo);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class MenuViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView image;
        TextView title;
        TextView moreInfo;

        public MenuViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            image = (ImageView) itemView.findViewById(R.id.list_icon);
            title = (TextView) itemView.findViewById(R.id.list_text);
            moreInfo = (TextView) itemView.findViewById(R.id.list_moreInfo);
        }

        //"주문 내역", "쿠폰 등록", "고객 지원", "소중한 사람에게 알려주기", "리뷰 써주기", "Plating 이란?"
        @Override
        public void onClick(View v) {
            switch (getAdapterPosition()) {
                case 0:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 친구 추천");
                    recommendToFriends();
                    break;
                case 1:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 프로모션 코드 등록");
                    showRegisterPromotionScreen();
                    break;
                case 2:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 내 쿠폰함");
                    showMyCouponList();
                    break;
                case 3:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 고객 지원");
                    getCustomerService();
                    break;
                case 4:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 주문 내역");
                    showOrderHistoryList();
                    break;
                case 5:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - 리뷰 써주기");
                    writeReviewToPlayStore();

                    break;
                case 6:
                    MixPanel.mixPanel_trackWithOutProperties("Click Navigation Item - Plating이란?");
                    whatIsPlating();
                    break;
            }
        }

        public void whatIsPlating() {
            DialogAPI.showDialog(mContext, "Plating 이란?", "플레이팅의 목표는 우리나라 모든 사람들이 저렴하고 합리적인 가격에 높은 퀄리티와 건강에 좋은 음식을 드실 수 있게 하는것 입니다.", "확인", null);
        }

        public void recommendToFriends() {
//            DialogAPI.showDialog(mContext, "소중한 사람에게 알려주기", "Coming Soon!", "확인", null);
//                    DialogAPI.showDialog(mContext, "", "Coming soon!");
/*                    try {
                        KakaoLink kakaoLink = KakaoLink.getKakaoLink(mContext);
                        final KakaoTalkLinkMessageBuilder kakaoTalkLinkMessageBuilder = kakaoLink.createKakaoTalkLinkMessageBuilder();
                        String text = "셰프의 음식을 집에서!\n\n맛있고 건강한 쉐프의 요리, 이제 편하게 집에서 저렴한 가격에 드세요!";
                        kakaoTalkLinkMessageBuilder.addText(text);
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
                    }*/
            Intent intent = new Intent(mContext, ReferActivity.class);
            mContext.startActivity(intent);
        }

        public void writeReviewToPlayStore() {
            Uri uri = Uri.parse("market://details?id=" + mContext.getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            try {
                mContext.startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                Toast.makeText(mContext, " unable to find market app", Toast.LENGTH_LONG).show();
                mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + mContext.getPackageName())));
            }
            ((Activity) mContext).overridePendingTransition(R.anim.transition_slide_in_from_left, R.anim.transition_slide_out_to_right);
        }

        public void showMyInformation() {
            DialogAPI.showDialog(mContext, "내 정보", "Coming Soon!", "확인", null);


        }

        public void getCustomerService() {
            //DialogAPI.showDialog(mContext, "고객 지원", "카카오톡에서 '플레이팅'을 친구 추가 해주세요", "확인", null);

            new MaterialDialog.Builder(mContext)
                    .title("고객 지원")
                    .content("카카오톡에서 '플레이팅'을 친구 추가 해주세요")
                    .positiveText("친구추가")
                    .negativeText("전화걸기")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {

                            try {
                                String url ="kakaoplus://plusfriend/friend/@플레이팅";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                mContext.startActivity(intent);
                            } catch (Exception e) {
                                String url ="http://goto.kakao.com/@플레이팅";
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                mContext.startActivity(intent);
                                e.printStackTrace();
                            }


                        }
                    })
                    .onNegative(new MaterialDialog.SingleButtonCallback() {
                        @Override
                        public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
                            // 전화걸기

                            Intent intent = new Intent(Intent.ACTION_CALL);
                            String phoneNumber = "070-7777-6114";
                            intent.setData(Uri.parse("tel:" + phoneNumber.replaceAll("-", "")));
                            MixPanel.mixPanel_trackWithOutProperties("Call Plating");
                            mContext.startActivity(intent);
                        }
                    })
                    .positiveColorRes(R.color.colorPrimary)
                    .neutralColorRes(R.color.colorPrimary)
                    .negativeColorRes(R.color.colorPrimary)
                    .show();
        }

        public void showOrderHistoryList() {
            Intent intent = new Intent(mContext, OrderHistoryListActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
        }
        public void showRegisterPromotionScreen() {
            Intent intent = new Intent(mContext, RegisterPromotionActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
        }

        public void showMyCouponList() {
            Intent intent = new Intent(mContext, MyCouponListActivity.class);
            mContext.startActivity(intent);
            ((Activity) mContext).overridePendingTransition(R.anim.transition_slide_in_from_right, R.anim.transition_slide_out_to_left);
        }
    }
}

