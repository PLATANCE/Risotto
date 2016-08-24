package com.plating.util;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by redjjol on 6/09/2014.
 */
public class SVUtil {
    private static String plating = "newsq";
    public static byte[] getBytes(final String data, final String charset) {

        if (data == null) {
            throw new IllegalArgumentException("data may not be null");
        }

        if (charset == null || charset.length() == 0) {
            throw new IllegalArgumentException("charset may not be null or empty");
        }

        try {
            return data.getBytes(charset);
        } catch (UnsupportedEncodingException e) {
            return data.getBytes();
        }
    }

    public static String sha256(String paramString)
    {
        try
        {
            byte[] arrayOfByte = MessageDigest.getInstance("SHA-256").digest(paramString.getBytes("UTF-8"));
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte.length; i++)
            {
                String str2 = Integer.toHexString(0xFF & arrayOfByte[i]);
                if (str2.length() == 1)
                    localStringBuffer.append('0');
                localStringBuffer.append(str2);
            }
            String str1 = localStringBuffer.toString();
            return str1;
        }
        catch (Exception localException)
        {
            throw new RuntimeException(localException);
        }
    }

    public static void showDialog(Context cx, String title, String msg) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(cx);
        dialog.setTitle(title);
        dialog.setMessage(msg);

        dialog.setPositiveButton("OK", null);

        dialog.show();
    }
    public static void showDialog2(Context cx, String title, String msg) {
        new MaterialDialog.Builder(cx)
                .title(title)
                .content(msg)

                        //.inputRangeRes(9, 11, R.color.orange_300)
//                .inputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED)
                .show();
    }

    public static void SetGcmtoken(Context cx, String token) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("gcm_token", token);
        editor.commit();

    }
    public static String GetGcmtoken(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        return prefs.getString("gcm_token", "");

    }

    public static void Set_AppsFlyer_media_source(Context cx, String token) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("media_source", token);
        editor.commit();

    }
    public static String AppsFlyer_media_source(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        return prefs.getString("media_source", "");

    }

    public static void Set_AppsFlyer_campaign(Context cx, String token) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("campaign", token);
        editor.commit();

    }
    public static String AppsFlyer_campaign(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        return prefs.getString("campaign", "");

    }

    public static void CreatedAlias(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("created_alias", true);
        editor.commit();
    }

    public static Boolean isCreatedAlias(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        return prefs.getBoolean("created_alias", false);
    }

    public static void SetUserIdx(Context cx, int idx) {
        Log.d("SVUtil", "SetUserIdx" + idx);
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("user_idx", idx);
        editor.commit();
    }
    public static int getUserIdx(Context cx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        return prefs.getInt("user_idx", 0);
    }

    public static void SetOrderIdx(Context cx, int idx) {
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putInt("order_idx", idx);
        editor.commit();
    }

    public static String getDeviceName() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        } else {
            return capitalize(manufacturer) + " " + model;
        }
    }

    public static String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public static String getUsername(Context cx) {
        AccountManager manager = AccountManager.get(cx);
        Account[] accounts = manager.getAccountsByType("com.google");
        List<String> possibleEmails = new LinkedList<String>();

        for (Account account : accounts) {
            // TODO: Check possibleEmail against an email regex or treat
            // account.name as an email address only for certain account.type values.
            possibleEmails.add(account.name);
        }

        if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");

            if (parts.length > 1)
                return parts[0];
        }
        return null;
    }

    public static void SetDeliveryAreaStatus(Context cx, boolean status) {
        Log.d("", "status : "+status);
        SharedPreferences prefs = cx.getSharedPreferences(plating, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean("delivery_area_status", status);
        editor.commit();
    }
}
