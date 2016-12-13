package example.chedifier.chedifier.test.talkback;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeProvider;

/**
 * Created by chedifier on 2016/11/17.
 */
public class EmptyViewGroup extends ViewGroup {

    protected final String TAG = getClass().getSimpleName();

    public EmptyViewGroup(Context context) {
        super(context);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected boolean dispatchHoverEvent(MotionEvent event) {
        boolean ret = super.dispatchHoverEvent(event);
        Log.d(TAG,"dispatchHoverEvent " + MotionEvent.actionToString(event.getAction()) + " ret " + ret);
        return ret;
    }

    @Override
    public void setHovered(boolean hovered) {
        Log.d(TAG,"setHovered " + hovered);
        super.setHovered(hovered);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public boolean onInterceptHoverEvent(MotionEvent event) {
        boolean ret = super.onInterceptHoverEvent(event);
        Log.d(TAG,"onInterceptHoverEvent " + MotionEvent.actionToString(event.getAction()) + " ret " + ret);
        return ret;
    }

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        AccessibilityNodeProvider ret = super.getAccessibilityNodeProvider();
        Log.d(TAG,"getAccessibilityNodeProvider " + ret);
        return ret;
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        boolean ret = super.performAccessibilityAction(action,arguments);
        Log.d(TAG,"performAccessibilityAction " + ret);
        return ret;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void sendAccessibilityEvent(int eventType) {
        Log.d(TAG,"sendAccessibilityEvent " + AccessibilityEvent.eventTypeToString(eventType));
        super.sendAccessibilityEvent(eventType);
    }

    @Override
    protected void onAttachedToWindow() {
        Log.d(TAG, "onAttachedToWindow");
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        Log.d(TAG, "onDetachedFromWindow");
        super.onDetachedFromWindow();
    }


    @Override
    public View focusSearch(View focused, int direction) {
        Log.d(TAG, "focusSearch" + focused);
        return super.focusSearch(focused, direction);
    }

    @Override
    public void dispatchWindowFocusChanged(boolean hasFocus) {
        Log.d(TAG, "dispatchWindowFocusChanged" + hasFocus);
        super.dispatchWindowFocusChanged(hasFocus);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public boolean requestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        Log.d(TAG, "requestSendAccessibilityEvent" + child + " " + AccessibilityEvent.eventTypeToString(event.getEventType()));
        return super.requestSendAccessibilityEvent(child, event);
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"onInitializeAccessibilityEvent " + event);
        super.onInitializeAccessibilityEvent(event);
    }

}
