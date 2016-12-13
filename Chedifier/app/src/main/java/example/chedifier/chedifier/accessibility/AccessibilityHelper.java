/**
 * ****************************************************************************
 * Description :
 * 1. manage triggers of accessibility
 * 2. basic accessibility interface wrapper
 *
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.app.Service;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;


import example.chedifier.chedifier.MyApplication;

public class AccessibilityHelper {
    public static final boolean DEBUG = false;

    public static AccessibilityManager getAccessibilityManager(){
        Context context = MyApplication.getAppContext().getApplicationContext();
        if(context != null){
            return (AccessibilityManager)context.getSystemService(
                    Service.ACCESSIBILITY_SERVICE);
        }
        return null;
    }

    public static boolean isAccessibilityEnable(){
        AccessibilityManager accMgr = getAccessibilityManager();
        if(accMgr != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                return accMgr.isTouchExplorationEnabled();
            }
        }

        return false;
    }

    public static void setImportantForAccessibility(View view, int importance){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setImportantForAccessibility(importance);
        }
    }

    public static void logSendAccessibilityEvent(View child, AccessibilityEvent event) {

        if(!DEBUG) return;


        ViewParent tp = child.getParent();
        while(tp != null){
            tp = tp.getParent();
        }

    }


    public static String parseNodeAction(int action){
        String actions = "";

        if((AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS & action) > 0){
            actions += "ACTION_CLEAR_ACCESSIBILITY_FOCUS";
        }

        if((AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS & action) > 0){
            actions += "ACTION_ACCESSIBILITY_FOCUS";
        }

        if((AccessibilityNodeInfo.ACTION_FOCUS & action) > 0){
            actions += "ACTION_FOCUS";
        }

        if((AccessibilityNodeInfo.ACTION_CLEAR_FOCUS & action) > 0){
            actions += "ACTION_CLEAR_FOCUS";
        }

        if((AccessibilityNodeInfo.ACTION_SELECT & action) > 0){
            actions += "ACTION_SELECT";
        }

        if((AccessibilityNodeInfo.ACTION_CLEAR_SELECTION & action) > 0){
            actions += "ACTION_CLEAR_SELECTION";
        }

        return actions;
    }

}
