package com.example.selfie.utils;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;

/**
 * Created by dpavlov on 24.6.2014 г..
 */
public class MyAccountManager {

    public static String getFirstGoogleAccUsername(Context context){
        AccountManager manager = AccountManager.get(context);
        Account[] accounts = manager.getAccountsByType("com.google");
        if(accounts.length > 0){
            return accounts[0].name;
        } else {
            return null;
        }
    }
}
