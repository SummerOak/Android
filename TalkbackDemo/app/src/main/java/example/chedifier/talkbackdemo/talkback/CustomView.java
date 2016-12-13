package example.chedifier.talkbackdemo.talkback;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.view.accessibility.AccessibilityNodeProvider;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.talkbackdemo.customview.VirtualView;
import example.chedifier.talkbackdemo.customview.accessibility.AccessibilityHelper;
import example.chedifier.talkbackdemo.customview.accessibility.IAccessable;
import example.chedifier.talkbackdemo.customview.accessibility.VirtualDescendantsProvider;


/**
 * Created by chedifier on 2016/11/17.
 */
public class CustomView extends EmptyViewGroup implements IAccessable {
    private static final boolean DEBUG = false;

    private VirtualView mBaseView;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public CustomView(Context context, VirtualView baseView) {
        super(context);

        setWillNotDraw(false);
        mBaseView = baseView;
        mBaseView.bindAdapterView(this);

//        setFocusable(true);
//        setEnabled(true);
//        if (getImportantForAccessibility() == IMPORTANT_FOR_ACCESSIBILITY_AUTO) {
//            setImportantForAccessibility(IMPORTANT_FOR_ACCESSIBILITY_YES);
//        }
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

            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
            return true;
        }

        return false;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if(mBaseView != null){
            event.offsetLocation(-mBaseView.getX(),-mBaseView.getY());
            if(mBaseView.dispatchTouchEvent(event)){
                event.offsetLocation(mBaseView.getX(),mBaseView.getY());
                return true;
            }
            event.offsetLocation(mBaseView.getX(),mBaseView.getY());
        }

        return super.dispatchTouchEvent(event);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if(mBaseView != null){
            mBaseView.measure(getMeasuredWidth(),getMeasuredHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if(mBaseView != null){
            mBaseView.layout(l,t,r,b);
        }

        super.onLayout(changed,l,t,r,b);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if(mBaseView != null){
            canvas.save();
            int childX = mBaseView.getX();
            int childY = mBaseView.getY();
            canvas.translate(childX, childY);
            mBaseView.draw(canvas);
            canvas.restore();
        }
    }

    private VirtualDescendantsProvider mVirturalDecendantsProvider = null;
    private example.chedifier.talkbackdemo.customview.accessibility.AccessibilityDelegate mAccessbilityDelegate
            = new example.chedifier.talkbackdemo.customview.accessibility.AccessibilityDelegate(this,true);
    private List<IAccessable> mChildsForAccessibility = new ArrayList<IAccessable>(1);

    @Override
    public boolean dispatchHoverEvent(MotionEvent event) {
        if(DEBUG) Log.d(TAG, "dispatchHoverEvent" + event);

        if(!mAccessbilityDelegate.dispatchHoverEvent(event)){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                onHoverEvent(event);
            }
        }
        return true;
    }

    @Override
    public AccessibilityNodeProvider getAccessibilityNodeProvider() {
        if(DEBUG) Log.d(TAG,"getAccessibilityNodeProvider ");
        if(mVirturalDecendantsProvider == null){
            mChildsForAccessibility.add(mBaseView);
            mVirturalDecendantsProvider = new VirtualDescendantsProvider(this,mChildsForAccessibility);
        }

        return mVirturalDecendantsProvider;
    }

    @Override
    public example.chedifier.talkbackdemo.customview.accessibility.AccessibilityDelegate getAccessibilityDelegator() {
        return mAccessbilityDelegate;
    }

    @Override
    public View getAdapterViewParent() {
        return this;
    }

    @Override
    public boolean isVisible() {
        return getVisibility() == View.VISIBLE;
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public int getChildOffsetInParentX() {
        int offset = getLeft();
        if(getParent() != null){
            return offset + ((ViewGroup)getParent()).getScrollX();
        }

        return offset;
    }

    @Override
    public int getChildOffsetInParentY() {
        int offset = getTop();
        if(getParent() != null){
            return offset + ((ViewGroup)getParent()).getScrollY();
        }

        return offset;
    }

    @Override
    public boolean pointInView(float x, float y) {
        return 0<=x&&x<=getWidth()&&0<=y&&y<=getHeight();
    }

    @Override
    public boolean getBoundsInParent(Rect out) {
        if(out != null){
            out.set(getLeft(),getTop(),getLeft() + getWidth(),getTop()+getHeight());
            return true;
        }
        return false;
    }

    @Override
    public boolean getBoundsOnScreenForAccessbility(Rect out) {
        return getGlobalVisibleRect(out);
    }

    @Override
    public boolean getGlobalVisibleRectForAccessibility(Rect out) {
        return super.getGlobalVisibleRect(out);
    }

    @Override
    public int getAccessibilityImportance() {
        return View.IMPORTANT_FOR_ACCESSIBILITY_YES;
    }

    @Override
    public List<IAccessable> getChildrenForAccessibility(){
        return mChildsForAccessibility;
    }
    /****************************** end for accessibility************************************/
}
