/**
 * ****************************************************************************
 * Description : a wrapper class of accessibility focusable android view
 *               which usually draw on canvas of custom view,since be drawn on custom view the accessibility
 *               inside view is not effective,so we wrap those view and take over accessibility event
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chedifier on 2016/11/23.
 */
public class AccessableView implements IAccessable{

    private static final String TAG = "AccessableView";
    private static final boolean DEBUG = false;

    private WeakReference<View> mRefView;

    private IAccessable mParent;
    private AccessibilityDelegate mDelegate = new AccessibilityDelegate(this);

    public AccessableView(View view, IAccessable parent){
        if(view == null) throw new NullPointerException("view cannot be null.");
        if(parent == null) throw new NullPointerException("parent cannot be null.");

        mRefView = new WeakReference<View>(view);
        mParent = parent;
    }

    public View getView(){
        return mRefView.get();
    }

    @Override
    public AccessibilityDelegate getAccessibilityDelegator() {
        return mDelegate;
    }

    @Override
    public View getAdapterViewParent() {
        return mParent.getAdapterViewParent();
    }

    @Override
    public boolean isVisible() {
        View v = mRefView.get();
        if(v != null){
            return v.getVisibility() == View.VISIBLE;
        }
        return false;
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
    public CharSequence getContentDescription() {
        View v = mRefView.get();
        if(v != null){
            return v.getContentDescription();
        }
        return "";
    }

    @Override
    public int getChildOffsetInParentX() {
        View v = mRefView.get();
        if(v != null){
            return v.getLeft();
        }
        return 0;
    }

    @Override
    public int getChildOffsetInParentY() {
        View v = mRefView.get();
        if(v != null){
            return v.getTop();
        }
        return 0;
    }

    @Override
    public boolean pointInView(float x, float y) {
        View v = mRefView.get();
        if(v != null){
            return 0<=x&&x<v.getWidth()&&0<=y&&y<v.getHeight();
        }

        return false;
    }

    @Override
    public boolean getBoundsInParent(Rect out) {
        View v = mRefView.get();
        if(v != null){
            return v.getLocalVisibleRect(out);
        }
        return false;
    }

    @Override
    public boolean getBoundsOnScreenForAccessbility(Rect out) {
        View v = mRefView.get();
        if(v != null){
            return v.getGlobalVisibleRect(out);
        }
        return false;
    }

    @Override
    public boolean getGlobalVisibleRectForAccessibility(Rect out) {
        View v = mRefView.get();
        if(v != null){
            return v.getGlobalVisibleRect(out);
        }
        return false;
    }

    @Override
    public int getAccessibilityImportance() {
        View v = mRefView.get();
        if(v != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                return v.getImportantForAccessibility();
            }
        }
        return View.IMPORTANT_FOR_ACCESSIBILITY_NO;
    }

    private final List<IAccessable> mChilds = new ArrayList<IAccessable>(0);

    public void addChild(IAccessable child){
        mChilds.add(child);
    }

    public void clearAccessibilityFocus(boolean includeDescendents){
        if(getAccessibilityDelegator().isAccessibilityFocused()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                getAccessibilityDelegator().sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
            }
            return;
        }

        if(includeDescendents){
            for(IAccessable a: mChilds){
                if(a instanceof AccessableView){
                    ((AccessableView) a).clearAccessibilityFocus(includeDescendents);
                }
            }
        }
    }

    @Override
    public List<IAccessable> getChildrenForAccessibility() {
        return mChilds;
    }
}
