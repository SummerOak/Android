package com.example.chedifier.previewofandroidn.common;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by chedifier on 2016/3/29.
 */
public class ScreenUtils {

    public static int dip2px(Context context,int dip){
        Resources r = context.getResources();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, r.getDisplayMetrics());
    }

}
