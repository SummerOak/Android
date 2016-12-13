package example.chedifier.hook.testproxy;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.lang.reflect.Field;

import example.chedifier.hook.HookApplication;
import example.chedifier.hook.hook.HookByDescriptor;
import example.chedifier.hook.hook.HookType;

/**
 * Created by chedifier on 2016/11/25.
 */
public class HookTalkbackProxy {

    private static final String TAG = "HookTalkbackProxy";

    @HookByDescriptor(
            className = "android.view.ViewRootImpl",
            methodName = "requestSendAccessibilityEvent",
            methodDescriptor = "(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z",
            hookType = HookType.BEFORE_TARGET
    )
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        Log.d(TAG,"requestSendAccessibilityEvent" + event);

        return true;
    }

    @HookByDescriptor(
            className = "android.view.ViewRootImpl",
            methodName = "requestChildRectangleOnScreen",
            methodDescriptor = "(Landroid/view/View;Landroid/graphics/Rect;B)Z",
            hookType = HookType.BEFORE_TARGET
    )
    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
        Log.d(TAG,"requestChildRectangleOnScreen" + child + " rectangle " + rectangle + " immediate " + immediate);

        return true;
    }

}
