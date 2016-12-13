package example.chedifier.talkbackdemo.utils;

import android.graphics.Rect;

import example.chedifier.talkbackdemo.MyApplication;

/**
 * Created by chedifier on 2016/11/28.
 */
public class ScreenUtils {

    public static int getScreenWidth() {
        return MyApplication.getAppContext().getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return MyApplication.getAppContext().getResources().getDisplayMetrics().heightPixels;
    }

    public static boolean getIntersection(Rect r1, Rect r2, Rect intersection){
        int left = Math.max(r1.left, r2.left);
        int right = Math.min(r1.right, r2.right);
        if (right > left) {
            int top = Math.max(r1.top, r2.top);
            int bottom = Math.min(r1.bottom, r2.bottom);
            if (bottom > top) {
                intersection.set(left,top,right,bottom);
                return true;
            }
        }
        return false;
    }
}
