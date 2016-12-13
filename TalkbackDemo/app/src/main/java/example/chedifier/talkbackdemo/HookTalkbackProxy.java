package example.chedifier.talkbackdemo;

import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

import java.lang.reflect.Field;
import java.sql.Ref;

import example.chedifier.hook.hook.HookAnnotation;
import example.chedifier.hook.hook.HookByDescriptor;
import example.chedifier.hook.hook.HookType;
import example.chedifier.talkbackdemo.talkback.ReflectView;
import example.chedifier.talkbackdemo.talkback.ReflectViewRootImpl;

/**
 * Created by chedifier on 2016/11/25.
 */
public class HookTalkbackProxy {

    private static final String TAG = "HookTalkbackProxy";

//    @HookByDescriptor(
//            className = "android.view.ViewRootImpl",
//            methodName = "requestSendAccessibilityEvent",
//            methodDescriptor = "(Landroid/view/View;Landroid/view/accessibility/AccessibilityEvent;)Z",
//            hookType = HookType.BEFORE_TARGET
//    )
//    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
//        Log.d(TAG,"requestSendAccessibilityEvent" + event);
//
//        return true;
//    }
//
//
//    @HookByDescriptor(
//            className = "android.view.ViewRootImpl",
//            methodName = "requestChildRectangleOnScreen",
//            methodDescriptor = "(Landroid/view/View;Landroid/graphics/Rect;Z)Z",
//            hookType = HookType.BEFORE_TARGET
//    )
//    public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
//        Log.d(TAG,"requestChildRectangleOnScreen" + child + " rectangle " + rectangle + " immediate " + immediate);
//
//        return true;
//    }

//    @HookByDescriptor(
//            className = "android.view.ViewRootImpl",
//            methodName = "draw",
//            methodDescriptor = "(Z)V",
//            hookType = HookType.BEFORE_TARGET
//    )
//    private void draw(boolean fullRedrawNeeded) {
//        Log.d(TAG,"draw" + fullRedrawNeeded);
//    }
//
//    @HookByDescriptor(
//            className = "android.view.ViewRootImpl",
//            methodName = "invalidateRectOnScreen",
//            methodDescriptor = "(Landroid/graphics/Rect;)V",
//            hookType = HookType.BEFORE_TARGET
//    )
//    private void invalidateRectOnScreen(Rect dirty) {
//        Log.d(TAG,"invalidateRectOnScreen" + dirty);
//    }





    @HookByDescriptor(
            className = "android.view.ViewRootImpl",
            methodName = "setAccessibilityFocus",
            methodDescriptor = "(Landroid/view/View;Landroid/view/accessibility/AccessibilityNodeInfo;)V",
            hookType = HookType.REPLACE_TARGET
    )
    void setAccessibilityFocus(View view, AccessibilityNodeInfo node) {
        Log.d(TAG,"setAccessibilityFocus " + view.getId() + " node " + node);

        Object viewRootImpl = ReflectView.getInstance().getViewRootImpl(view);
        if (ReflectViewRootImpl.getInstance().mAccessibilityFocusedVirtualView(viewRootImpl) != null) {

            AccessibilityNodeInfo focusNode = ReflectViewRootImpl.getInstance().mAccessibilityFocusedVirtualView(viewRootImpl);
            View focusHost = ReflectViewRootImpl.getInstance().mAccessibilityFocusedHost(viewRootImpl);

            // Wipe the state of the current accessibility focus since
            // the call into the provider to clear accessibility focus
            // will fire an accessibility event which will end up calling
            // this method and we want to have clean state when this
            // invocation happens.
            ReflectViewRootImpl.getInstance().setmAccessibilityFocusedHost(viewRootImpl,null);
            ReflectViewRootImpl.getInstance().setmAccessibilityFocusedVirtualView(viewRootImpl,null);

            // Clear accessibility focus on the host after clearing state since
            // this method may be reentrant.
            ReflectView.getInstance().clearAccessibilityFocusNoCallbacks(focusHost);

            focusNode.recycle();
        }

        if (ReflectViewRootImpl.getInstance().mAccessibilityFocusedHost(viewRootImpl) != null) {
            // Clear accessibility focus in the view.
            ReflectView.getInstance().clearAccessibilityFocusNoCallbacks(ReflectViewRootImpl.getInstance().mAccessibilityFocusedHost(viewRootImpl));
        }

        ReflectViewRootImpl.getInstance().setmAccessibilityFocusedVirtualView(viewRootImpl,node);
        ReflectViewRootImpl.getInstance().setmAccessibilityFocusedHost(viewRootImpl,view);
        ReflectViewRootImpl.getInstance().invalidateRoot(viewRootImpl);
    }

//    @HookAnnotation(
//            targetClass = AccessibilityNodeInfo.class,
//            targetMethodName = "getBoundsInScreen",
//            targetMethodParams = {Rect.class}
//    )
//    public void getBoundsInScreen(Rect outBounds) {
//        Log.d(TAG,this + "getBoundsInScreen" + outBounds);
//    }


}
