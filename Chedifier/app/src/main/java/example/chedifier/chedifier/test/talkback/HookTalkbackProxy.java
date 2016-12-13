package example.chedifier.chedifier.test.talkback;

import android.util.Log;
import android.view.InputEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookByDescriptor;
import example.chedifier.hook.hook.HookType;

/**
 * Created by chedifier on 2016/11/25.
 */
public class HookTalkbackProxy {

    private static final String TAG = "HookTalkbackProxy";

    @HookByDescriptor(
            className = "android.view.ViewRootImpl",
            methodName = "deliverInputEvent",
            methodDescriptor = "(Landroid/view/ViewRootImpl$QueuedInputEvent;)V",
            hookType = HookType.BEFORE_TARGET
    )
    private void deliverInputEvent(Object q) {
        Log.d(TAG,"deliverInputEvent" + q);

    }

    @HookByDescriptor(
            className = "android.view.InputEventReceiver",
            methodName = "dispatchInputEvent",
            methodDescriptor = "(ILandroid/view/InputEvent;)V",
            hookType = HookType.BEFORE_TARGET
    )
    private void dispatchInputEvent(int seq, InputEvent event) {
        Log.d(TAG,"dispatchInputEvent" + event);
    }

    @HookByDescriptor(
            className = "android.view.ViewRootImpl",
            methodName = "requestSendAccessibilityEvent",
            methodDescriptor = "(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)B",
            hookType = HookType.BEFORE_TARGET
    )
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        Log.d(TAG,"requestSendAccessibilityEvent" + event);

        return true;
    }

}
