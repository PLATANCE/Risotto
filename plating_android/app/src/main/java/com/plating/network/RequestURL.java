package com.plating.network;

/**
 * Created by cheehoonha on 2/21/15.
 */
public class RequestURL {

    // IP for Development
    public static final String PLATING_IP = "0.0.0.0:3000";

    // IP for Production
    public static final String DOMAIN_NAME = "api.plating.co.kr";   // USE THIS FOR PRODUCTION VERSION

    // This is the IP address that this program is using.
    // This can change depending on the mode (development or production mode)
    public static final String CURRENTLY_USING_IP_ADDRESS = DOMAIN_NAME;

    // URL header for getting data
    public static final String FULL_URL = "http://" + CURRENTLY_USING_IP_ADDRESS;

    public static final String SERVER__USER_INFOMATION = FULL_URL + "/user";

    // URLs for getting data
    public static final String SERVER__APP_UPDATE_AVAILABLE = FULL_URL + "/check_app_update_available";
    public static final String SERVER__CHECK_FOR_WHICH_SCREEN = FULL_URL + "/which_screen";
    public static final String SERVER__DAILY_MENU = FULL_URL + "/daily_menu";
    public static final String SERVER__IS_REVIEW_AVAILABLE = FULL_URL + "/review/isAvailable";
    public static final String SERVER__CANCEL_WRITING_REVIEW = FULL_URL + "/review/cancel";
    public static final String SERVER__GET_MENU_DETAIL = FULL_URL + "/menu_detail";
    public static final String SERVER__GET_MORE_REVIEW_FROM_SERVER = FULL_URL + "/review/view_more";
    public static final String SERVER__PLACE_ORDER = FULL_URL + "/place_order";
    public static final String SERVER__GET_AVAILABLE_LOCATIONS = "https://address.plating.co.kr/";
    public static final String SERVER__GET_ORDER_HISTORY = FULL_URL + "/order/history";
    public static final String SERVER__GET_ORDER_HISTORY_DETAIL = FULL_URL + "/order/detail";
    public static final String SERVER__SET_USER_GPS_LOCATION = FULL_URL + "/update_info";

    // URL for Coupon
    public static final String SERVER__GET_COUPON_LIST = FULL_URL + "/coupon/my_coupon";

    // URL Dialog
    public static final String SERVER__GET_DIALOG = FULL_URL + "/dialog/get_dialog";

    // URL UserCode
    public static final String SERVER__GET_USERCODE = FULL_URL + "/user/user_code";
    public static final String SERVER__GET_POLICY_REFER_POINT = FULL_URL + "/policy/policy_refer_point";

    // URL User Address List
    public static final String SERVER__GET_ADDRESS_LIST = FULL_URL + "/user/get_address_list";
    public static final String SERVER__GET_ADDRESS_INUSE = FULL_URL + "/user/get_address";
    public static final String SERVER__UPDATE_ADDRESS = FULL_URL + "/user/update_address";

    // URL header for daily menu image
    public static final String DAILY_MENU_IMAGE_URL = "http://plating.co.kr/app/media/";
    // URL header for chef image
    public static final String CHEF_IMAGE_URL = "http://plating.co.kr/app/media/chef/";
    // URL heaader for dialog image
    public static final String DIALOG_IMAGE_URL = "http://plating.co.kr/app/media/dialog/";
    // URL header for coupon image
    public static final String COUPON_IMAGE_URL = "http://plating.co.kr/app/media/coupon/";
    // URL header for refer image
    public static final String REFER_IMAGE_URL = "http://plating.co.kr/app/media/refer/";

    public static final String BANNER_IMAGE_URL = "http://plating.co.kr/app/media/banner/";

    public static final String REQUEST_AUTH_NUMBER = "https://smsauth.plating.co.kr/user/{userIdx}/requestAuthNumber";
    public static final String VALIDATE_AUTH_NUMBER = "https://smsauth.plating.co.kr/user/{userIdx}/validateAuthNumber";

}
