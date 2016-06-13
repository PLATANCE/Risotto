package com.plating.application;

import android.location.Location;

/**
 * Created by cheehoonha on 6/3/15.
 */
public class Constant {
    public static String APP_NAME = "PlatingActivity.";

    // Constants for Intent
    public static String DAILY_MENU_ACTIVITY__MENU_IDX = "DAILY_MENU_ACTIVITY__MENU_IDX";
    public static String DAILY_MENU_ACTIVITY__MENU_D_IDX = "DAILY_MENU_ACTIVITY__MENU_D_IDX";
    public static String DAILY_MENU_ACTIVITY__STOCK = "DAILY_MENU_ACTIVITY__STOCK";
    public static String ORDER_HISTORY_LIST_ACTIVITY__ORDER_HISTORY_ID = "ORDER_HISTORY_LIST_ACTIVITY__ORDER_HISTORY_ID";
    public static String ORDER_HISTORY_ACTIVITY__ORDER_HISTORY_ID = "ORDER_HISTORY_ACTIVITY__ORDER_HISTORY_ID";
    public static String ORDER_PLACED = "ORDER_PLACED";


    // Constants for Server Requests
    public static String SERVER_CONNECTION_FAILURE = "서버와의 연결이 불안정합니다.";

    // Empty cart
    public static String CART_IS_EMPTY = "장바구니가 비어있습니다.";

    // Item Added
    public static String ITEM_ADDED = "장바구니에 메뉴가 추가되었습니다.";

    // main 0
    // 요리 준비중 1
    // 배송중 2
    // 리뷰 화면 3
    public static int screen_main = 0, screen_cooking = 1, screen_delivering = 2, screen_review = 3, screen_no_menu = 4, screen_no_address = 5;



    // GPS Related
    public static Location currentLocation;

    // MixPanel
    public static String MIX_PANEL_JSON_ERROR = "Error: Unable to add properties to JSONObject";

    // Cart Activity Button Enable Check
    public static String PLEASE_ENTER_ADDRESS = "주소를 입력해주세요";
    public static String PLEASE_ENTER_PHONE_NUMBER = "전화번호를 입력해주세요";
    public static String PLEASE_ENTER_PHONE_NUMBER_NEW = "핸드폰 번호를 입력해주세요";
    public static String PLEASE_ENTER_DELIVERY_TIME = "배달 시간을 선택해주세요";
    public static String DELIVERY_IS_FINISHED = "선택 가능한 배달 시간이 없습니다.";
    public static String PLEASE_ENTER_CREDIT_CARD = "카드를 등록해주세요";

    // Chef Constants
    public static String CHEF_IMAGE_URL = "CHEF_IMAGE_URL";
    public static String CHEF_NAME = "CHEF_NAME";
    public static String CHEF_CAREER = "CHEF_CAREER";
    public static String CHEF_CAREER_SUMMARY = "CHEF_CAREER_SUMMARY";

    // Pay Method
    public static String PAY_METHOD_CARD = "카드";
    public static String PAY_METHOD_DIRECT_CARD = "현장카드";
    public static String PAY_METHOD_DIRECT_CREDIT = "현금";
}
