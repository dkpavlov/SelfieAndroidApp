package com.example.selfie.utils;

import android.content.Context;
import android.content.Intent;

import com.example.selfie.app.HomeActivity;

/**
 * Created by dpavlov on 29.7.2014 г..
 */
public class Utils {

    public static final String SERVER_EXCEPTION_KAY = "SERVER_EXCEPTION_KAY";

    public static void startHomeActivityWithToast(Context context){
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra(SERVER_EXCEPTION_KAY, true);
        context.startActivity(intent);
    }

    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        String emailRegEx = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
        try {
            result = email.matches(emailRegEx);
        } catch (Exception ex) {
            result = false;
        }
        return result;
    }
}
