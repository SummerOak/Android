package com.uc.framework.resources;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.res.ResourcesCompat;

import com.uc.test.videogifrecoderui.FullscreenActivity;

/**
 * Created by uc on 03/12/2017.
 */

public class ResTools {

    private static Context getContext(){
        return FullscreenActivity.sContext.getApplicationContext();
    }

    public static String getUCString(int strId){
        return getContext().getResources().getString(strId);
    }

    public static int getDimenInt(int resId){
        return (int)getContext().getResources().getDimension(resId);
    }

    public static int dpToPxI(float dp){
        return (int)(dp * getContext().getResources().getDisplayMetrics().density + 0.5f);
    }

    public static int getColor(String resName){

        int id = getContext().getResources().getIdentifier(resName,"color",getContext().getPackageName());

        return ResourcesCompat.getColor(getContext().getResources(),id,null);
    }

    public static Drawable getDrawable(String resName){
        return new ColorDrawable(Color.GREEN);
    }

}
