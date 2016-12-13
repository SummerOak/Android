package example.chedifier.talkbackdemo.talkback;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;
import android.widget.LinearLayout;

/**
 * Created by chedifier on 2016/12/1.
 */
public class EmptyLinearLayout extends LinearLayout {
    protected final String TAG = getClass().getSimpleName();

    public EmptyLinearLayout(Context context) {
        super(context);
    }

    public EmptyLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EmptyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public EmptyLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
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
        if(action != AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS){
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

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void sendAccessibilityEvent(int eventType) {
        Log.d(TAG,getId() + " sendAccessibilityEvent " + AccessibilityEvent.eventTypeToString(eventType));

        if(eventType != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
            super.sendAccessibilityEvent(eventType);
        }
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        Log.d(TAG,getId() + " onInitializeAccessibilityNodeInfo " + info);
        super.onInitializeAccessibilityNodeInfo(info);
    }
}
