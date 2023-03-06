package com.cureius.pocket.util;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Prashant on 1/12/2017.
 */

public class SharedPreferencesUtil {

    public static void setValueToSharedPreferences(Context context,String key,String value)
    {
        SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }

    public static void setValueToSharedPreferences(Context context,String key,boolean value)
    {
        SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }

    public static void setValueToSharedPreferences(Context context,String key,int value)
    {
        SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key,value);
        editor.apply();
    }

    public static void setBooleanValueToSharedPreferences(Context context,String key,boolean value)
    {
        SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key,value);
        editor.apply();
    }



    public static String getValueToSharedPreferences(Context context,String key)
    {
        if (context!=null) {
            SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
            return preferences.getString(key, null);
        }
        return "";
    }

    public static boolean getBooleanValueToSharedPreferences(Context context,String key)
    {
        if (context!=null) {
            SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
            return preferences.getBoolean(key, true);
        }
        return true;
    }

    public static int getIntValueToSharedPreferences(Context context,String key)
    {
        if (context!=null) {
            SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
            return preferences.getInt(key, 0);
        }
        return 0;
    }

    public static void setInstanceListValueToSharedPreferences(Context context,String key,String value)
    {
        SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key,value);
        editor.apply();
    }
    public static String getInstanceListValueToSharedPreferences(Context context,String key)
    {
        if (context!=null) {
            SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
            return preferences.getString(key, null);
        }
        return "";
    }
    public static boolean removeData(Context context,String key)
    {
        if (context!=null) {
            SharedPreferences preferences = context.getSharedPreferences("PocketPref", Context.MODE_PRIVATE);
            return preferences.edit().remove(key).commit();
        }
        return false;
    }
}
