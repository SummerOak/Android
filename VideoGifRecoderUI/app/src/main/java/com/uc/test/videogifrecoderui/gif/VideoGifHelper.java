package com.uc.test.videogifrecoderui.gif;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

/**
 * Created by uc on 03/12/2017.
 */

public class VideoGifHelper {

    protected static void setDrawable(View v, Drawable drawable){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            v.setBackground(drawable);
        }else{
            v.setBackgroundDrawable(drawable);
        }
    }
}
