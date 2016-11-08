package example.chedifier.base.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;


/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : ScreenUtils
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class ScreenUtils {

    public static float dipToPixels(Context context,float dipValue) {
        if(context != null && context.getResources() != null){
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
        }

        return 0f;
    }

}
