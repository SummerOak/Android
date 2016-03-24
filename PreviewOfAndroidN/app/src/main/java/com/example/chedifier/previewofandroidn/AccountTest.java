package com.example.chedifier.previewofandroidn;

import android.*;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Patterns;

import java.util.regex.Pattern;

/**
 *
 * Created by chedifier on 2016/3/23.
 */
public class AccountTest {

    private static final String TAG = AccountTest.class.getSimpleName();

    public static void getAccount(Context ctx){
        boolean hasPermission = (ContextCompat.checkSelfPermission(ctx,
                android.Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED);

        if(!hasPermission){
            Log.d(TAG,"permission denied!try to request it.");
            ActivityCompat.requestPermissions((Activity)ctx,
                    new String[]{android.Manifest.permission.GET_ACCOUNTS},
                    2);
        }

        Account[] accounts = AccountManager.get(ctx).getAccounts();
        for (Account account : accounts) {

            Log.d(TAG,"account.type = " + account.type + " account.name = " + account.name
                    + "account.describeContents()= " + account.describeContents());
        }
    }

}
