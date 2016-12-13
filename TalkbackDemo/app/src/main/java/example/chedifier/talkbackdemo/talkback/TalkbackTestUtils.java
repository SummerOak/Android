package example.chedifier.talkbackdemo.talkback;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


/**
 * Created by chedifier on 2016/11/15.
 */
public class TalkbackTestUtils {

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public static void logSendAccessibilityEvent(View child, AccessibilityEvent event) {

        Log.d("cqx","requestSendAccessibilityEvent " + child.getClass().getSimpleName() + " " + AccessibilityEvent.eventTypeToString(event.getEventType()));

        ViewParent tp = child.getParent();
        while(tp != null){
            Log.d("cqx","requestSendAccessibilityEvent " + tp.getClass().getSimpleName());
            tp = tp.getParent();
        }

        Log.d("cqx","------------------------------------");
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
