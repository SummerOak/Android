/**
 * ****************************************************************************
 * Description : a wrapper class of accessibility focusable rect area
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.graphics.Rect;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.chedifier.utils.ScreenUtils;

public class AccessableRect implements IAccessable {

    //bounds in parent
    private Rect mRect;

    private IAccessable mParent;
    private AccessibilityDelegate mDelegate = new AccessibilityDelegate(this);
    private CharSequence mContentDescription = "";
    private boolean mCheckable=false,mChecked = false;
    private int mImportanceForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_NO;

    public AccessableRect(Rect rect, IAccessable parent){
        this(rect,parent,"");
    }

    public AccessableRect(Rect rect, IAccessable parent, CharSequence desc){
        mParent = parent;mRect = rect;mContentDescription = desc;

        if(mRect == null) throw new NullPointerException("area rect cannot be null.");
        if(mParent == null) throw new NullPointerException("parent cannot be null.");
    }

    public void updateRect(Rect rect){
        if(rect == null) throw new NullPointerException("area rect cannot be null.");

        mRect.set(rect);
    }

    public void setCheckable(boolean val){
        mCheckable = val;
    }

    public void setChecked(boolean val){
        mChecked = val;
    }

    public void setContentDescription(CharSequence desc){
        mContentDescription = desc;
    }

    public void setImportanceForAccessibility(int val){
        mImportanceForAccessibility = val;
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
    public CharSequence getContentDescription() {
        return mContentDescription;
    }

    @Override
    public boolean getBoundsInParent(Rect out) {
        out.set(mRect);
        return true;
    }

    private Rect mTempRect = new Rect();
    @Override
    public boolean getGlobalVisibleRectForAccessibility(Rect out) {
        if(getBoundsOnScreenForAccessbility(out) &&
                mParent.getGlobalVisibleRectForAccessibility(mTempRect)){

            return ScreenUtils.getIntersection(out,mTempRect,out);
        }

        return false;
    }

    @Override
    public int getChildOffsetInParentX() {
        return mRect.left;
    }

    @Override
    public int getChildOffsetInParentY() {
        return mRect.top;
    }

    @Override
    public boolean isVisible() {
        return true;
    }

    @Override
    public boolean isCheckable() {
        return mCheckable;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public boolean pointInView(float x, float y) {
        return 0<=x&&x<=mRect.width()&&0<=y&&y<=mRect.height();
    }

    @Override
    public int getAccessibilityImportance() {
        return mImportanceForAccessibility;
    }

    @Override
    public boolean getBoundsOnScreenForAccessbility(Rect out) {
        if(mParent.getBoundsOnScreenForAccessbility(out)){
            out.set(
                out.left+mRect.left,
                out.top + mRect.top,
                out.left+mRect.left + mRect.width(),
                out.top + mRect.top + mRect.height());

            return true;
        }

        return false;
    }

    private static final List<IAccessable> sEmptyChilds = new ArrayList<IAccessable>(0);
    @Override
    public List<IAccessable> getChildrenForAccessibility() {
        return sEmptyChilds;
    }
}
