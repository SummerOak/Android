package com.example.chedifier.previewofandroidn.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

/**
 * Created by chedifier on 2016/3/28.
 */
public class MyPermissionManager {

    private static int sReqCode = 1;

    public static boolean checkSdcardPermission(Activity activity, String permission){
        boolean hasPermission = (ContextCompat.checkSelfPermission(activity,permission)
                == PackageManager.PERMISSION_GRANTED);

        if(!hasPermission){
            ActivityCompat.requestPermissions(activity,
                    new String[]{permission},
                    ++sReqCode);
        }

        return hasPermission;
    }

}
