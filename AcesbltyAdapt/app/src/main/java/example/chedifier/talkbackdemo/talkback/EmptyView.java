package example.chedifier.talkbackdemo.talkback;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

import example.chedifier.talkbackdemo.customview.accessibility.AccessibilityHelper;

/**
 * Created by chedifier on 2016/11/17.
 */
public class EmptyView extends View {

    protected final String TAG = getClass().getSimpleName();

    public boolean block_access_focus = false;

    public boolean block_send_focus = false;



    public EmptyView(Context context) {
        super(context);


    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        boolean ret = super.dispatchHoverEvent(event);
        Log.d(TAG,getId() + " dispatchHoverEvent " + MotionEvent.actionToString(event.getAction()) + " ret " + ret);
        return ret;
    }

    @Override
    public void setHovered(boolean hovered) {
        Log.d(TAG,getId() + " setHovered " + hovered);
        super.setHovered(hovered);
    }

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        AccessibilityNodeProvider ret = super.getAccessibilityNodeProvider();
        Log.d(TAG,getId() + " getAccessibilityNodeProvider " + ret);
        return ret;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,getId() + " onInitializeAccessibilityEvent " + event);
        super.onInitializeAccessibilityEvent(event);
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        Log.d(TAG,getId() + " pre performAccessibilityAction " + TalkbackTestUtils.parseNodeAction(action));
        boolean ret = false;
        if(!block_access_focus || action != AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS){
            ret |= super.performAccessibilityAction(action,arguments);
        }else{
            ret = true;
        }
        Log.d(TAG,getId() + " post performAccessibilityAction " + TalkbackTestUtils.parseNodeAction(action) + " ret: " + ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,getId() + " onTouchEvent " + event);
        return super.onTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean requestAccessibilityFocus() {
        Log.d(TAG,getId() + " requestAccessibilityFocus ");
        AccessibilityManager manager = AccessibilityHelper.getAccessibilityManager();
        if (!manager.isEnabled() || !manager.isTouchExplorationEnabled()) {
            return false;
        }
        if (getVisibility() != View.VISIBLE) {
            return false;
        }

        if(!isAccessibilityFocused()){

            ReflectView.getInstance().setAccessibilityFocused(this);
            ReflectViewRootImpl.getInstance().setAccessibilityFocus(ReflectView.getInstance().getViewRootImpl(this),this,null);
            invalidate();

            postDelayed(new Runnable() {
                @Override
                public void run() {
                    invalidate();
                    invalidate();
                    invalidate();
                }
            },400);

            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
            return true;
        }

        return false;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void sendAccessibilityEvent(int eventType) {
        Log.d(TAG,getId() + " sendAccessibilityEvent " + AccessibilityEvent.eventTypeToString(eventType));

        if(!block_send_focus || eventType != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
            super.sendAccessibilityEvent(eventType);
        }
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        Log.d(TAG,getId() + " onInitializeAccessibilityNodeInfo " + info);
        super.onInitializeAccessibilityNodeInfo(info);
    }

}
