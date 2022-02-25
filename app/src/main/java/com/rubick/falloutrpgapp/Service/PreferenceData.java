package com.rubick.falloutrpgapp.Service;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.google.gson.Gson;
import com.rubick.falloutrpgapp.Model.Atribute;
import com.rubick.falloutrpgapp.Model.UserData;

public class PreferenceData {
    private static final String USERDATA_KEY = "userData";

    public static void SaveUserData(Context context, UserData user){
        Gson gson = new Gson();
        String data = gson.toJson(user);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(USERDATA_KEY, data);
        editor.apply();
    }

    public static UserData LoadUserData(Context context){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Gson gson = new Gson();
        return gson.fromJson(preferences.getString(USERDATA_KEY, ""), UserData.class);
    }

}
