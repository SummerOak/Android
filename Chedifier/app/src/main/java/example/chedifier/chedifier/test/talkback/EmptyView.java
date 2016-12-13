package example.chedifier.chedifier.test.talkback;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;

/**
 * Created by chedifier on 2016/11/17.
 */
public class EmptyView extends View {

    protected final String TAG = getClass().getSimpleName();

    public EmptyView(Context context) {
        super(context);
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

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        AccessibilityNodeProvider ret = super.getAccessibilityNodeProvider();
        Log.d(TAG,"getAccessibilityNodeProvider " + ret);
        return ret;
    }

    @Override
    public void onInitializeAccessibilityEvent(AccessibilityEvent event) {
        Log.d(TAG,"onInitializeAccessibilityEvent " + event);
        super.onInitializeAccessibilityEvent(event);
    }

    @Override
    public boolean performAccessibilityAction(int action, Bundle arguments) {
        boolean ret = super.performAccessibilityAction(action,arguments);
        Log.d(TAG,"performAccessibilityAction " + ret);
        return ret;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(TAG,"onTouchEvent " + event);
        return super.onTouchEvent(event);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    @Override
    public void sendAccessibilityEvent(int eventType) {
        Log.d(TAG,"sendAccessibilityEvent " + AccessibilityEvent.eventTypeToString(eventType));

        if(eventType != AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
            super.sendAccessibilityEvent(eventType);
        }
    }

    @Override
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
        Log.d(TAG,"onInitializeAccessibilityNodeInfo " + info);
        super.onInitializeAccessibilityNodeInfo(info);
    }

}
