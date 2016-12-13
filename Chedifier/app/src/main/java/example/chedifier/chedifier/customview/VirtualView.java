package example.chedifier.chedifier.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.chedifier.accessibility.AccessibilityDelegate;
import example.chedifier.chedifier.accessibility.IAccessable;
import example.chedifier.chedifier.utils.ScreenUtils;

/**
 * Created by chedifier on 2016/11/17.
 */
public class VirtualView implements IAccessable{

    protected static final String TAG = "VirtualView";

    protected Context mContext;

    protected VirtualView mParent;

    protected View mAdapterParent;
    protected View mAdapterAncestor;

    protected int mX,mY;
    public static final byte MATCH_PARENT = -1;
    protected int mWidth=MATCH_PARENT,mHeight=MATCH_PARENT;
    protected int mBackGroundColor = 0xff000000; //默认黑色

    protected int mMarginLeft = 0,mMarginRight = 0,mMarginTop = 0,mMarginBottom = 0;

    protected boolean mFocusable = true;
    protected byte mVisibility = VISIBLE;

    public final static byte VISIBLE   = 0x00000000;
    public final static byte INVISIBLE = 0x00000004;
    public final static byte GONE      = 0x00000008;

    protected static int sVirturalViewIdCounter = 0;
    protected int mVirturalViewId = ++sVirturalViewIdCounter;

    protected ClickEventDelegate mClickEventDelegate;

    public VirtualView(Context context){
        this(context,MATCH_PARENT,MATCH_PARENT);
    }

    public VirtualView(Context context,int w,int h){
        this(context,w,h, Color.BLACK);
    }

    public VirtualView(Context context,int w,int h,int bg){
        mContext = context;
        mWidth = w;
        mHeight = h;
        mBackGroundColor = bg;
    }

    public void setX(int x){
        mX = x;
    }

    public int getX(){
        return mX;
    }

    public int getY(){
        return mY;
    }

    public void setY(int y){
        mY = y;
    }

    public void setWidth(int w){
        mWidth = w;
    }

    public void setHeight(int h){
        mHeight = h;
    }

    public int getWidth(){
        return mWidth;
    }

    public int getHeight(){
        return mHeight;
    }

    public int getMarginLeft(){
        return mMarginLeft;
    }

    public int getMarginTop(){
        return mMarginTop;
    }

    public int getMarginRight(){
        return mMarginRight;
    }

    public int getMarginBottom(){
        return mMarginBottom;
    }

    public void setMargin(int l,int t,int r,int b){
        mMarginLeft = l;mMarginTop = t;mMarginRight = r;mMarginBottom = b;
    }

    public void bindAdapterView(View view){
        mAdapterParent = view;
    }

    public void measure(int w,int h){
        if(mWidth == MATCH_PARENT){
            mWidth = w - mMarginRight - mMarginLeft;
        }else if(mWidth > w){
            mWidth = w - mMarginRight - mMarginLeft;
        }
        if(mWidth < 0) mWidth = 0;

        if(mHeight == MATCH_PARENT){
            mHeight = h - mMarginBottom - mMarginTop;
        }else if(mHeight > h){
            mHeight = h - mMarginBottom - mMarginTop;
        }
        if(mHeight < 0) mHeight = 0;
    }

    public void layout(int l,int t,int r,int b){
        setX(l + mMarginLeft);
        setY(t + mMarginTop);
    }

    public void draw(Canvas canvas){
        if(canvas != null){
            canvas.clipRect(0, 0, mWidth, mHeight);
            canvas.drawColor(mBackGroundColor);
        }
    }

    public boolean isFocusable(){
        return mFocusable;
    }

    public void setFocusable(boolean val){
        mFocusable = val;
    }

    public void setVisibility(byte visibility) {
        mVisibility = visibility;
    }

    public byte getVisibility(){
        return mVisibility;
    }

    public boolean isVisible() {
        return mVisibility == VISIBLE;
    }

    public boolean dispatchTouchEvent(MotionEvent event){
        if(isVisible() && pointInView(event.getX(),event.getY())){
            return onTouchEvent(event);
        }

        return false;
    }

    public boolean onTouchEvent(MotionEvent event){
        if(mClickEventDelegate != null){
            return mClickEventDelegate.handleTouchEvent(event);
        }

        return false;
    }

    public void setOnClickListener(View.OnClickListener l){
        if(mClickEventDelegate == null){
            mClickEventDelegate = new ClickEventDelegate();
        }

        mClickEventDelegate.setOnClickListener(l);
    }

    public void setOnLongClickListener(View.OnLongClickListener l){
        if(mClickEventDelegate == null){
            mClickEventDelegate = new ClickEventDelegate();
        }
        mClickEventDelegate.setOnLongClickListener(l);
    }

    public void setParent(VirtualView parent){
        mParent = parent;
    }

    public VirtualView getParent() {
        return mParent;
    }

    /********************* begin for accessibility ******************************/

    protected CharSequence mContentDescription;
    protected AccessibilityDelegate mAccessbilityDelegate = new AccessibilityDelegate(this);

    protected int mImportanceForAccessibility = View.IMPORTANT_FOR_ACCESSIBILITY_YES;

    public void setImportanceForAccessibility(int val){
        mImportanceForAccessibility = val;
    }

    public int getScrollX(){
        return 0;
    }

    public int getScrollY(){
        return 0;
    }

    protected void getLocationOnScreen(int[] location){

        int scrollX = 0,scrollY = 0;
        if(mAdapterParent != null){
            mAdapterParent.getLocationOnScreen(location);
            scrollX = mAdapterParent.getScrollX();
            scrollY = mAdapterParent.getScrollY();
        }else{
            if(getParent() != null){
                getParent().getLocationOnScreen(location);
                scrollX = getParent().getScrollX();
                scrollY = getParent().getScrollY();
            }
        }

        location[0] += (mX+scrollX);
        location[1] += (mY+scrollY);
    }

    public boolean getBoundsOnScreen(Rect out){
        int[] locationOnScreen = new int[2];
        getLocationOnScreen(locationOnScreen);
        out.set(locationOnScreen[0],locationOnScreen[1],
                locationOnScreen[0]+mWidth,locationOnScreen[1]+mHeight);
        return true;
    }

    public void setContentDescription(CharSequence contentDescription){
        mContentDescription = contentDescription;
    }

    @Override
    public AccessibilityDelegate getAccessibilityDelegator(){
        return mAccessbilityDelegate;
    }

    @Override
    public View getAdapterViewParent(){
        if(mAdapterParent != null){
            return mAdapterParent;
        }

        if(mAdapterAncestor == null){
            mAdapterAncestor = getParent()==null?null:getParent().getAdapterViewParent();
        }

        return mAdapterAncestor;
    }

    private Rect mTempRect = new Rect();
    @Override
    public boolean getBoundsInParent(Rect out){
        out.set(mX,mY,mX + mWidth,mY+mHeight);
        return true;
    }
    @Override
    public boolean getGlobalVisibleRectForAccessibility(Rect out){
        if(out == null){
            return false;
        }

        if(mAdapterParent != null){
            if(mAdapterParent.getGlobalVisibleRect(out)
                    && getBoundsOnScreen(mTempRect)
                    && ScreenUtils.getIntersection(out,mTempRect,out)){
                return true;
            }
        }

        if(getParent() != null && getParent().getGlobalVisibleRectForAccessibility(out)
                && getBoundsOnScreen(mTempRect)
                && ScreenUtils.getIntersection(out,mTempRect,out)){
            return true;
        }

        return false;
    }

    @Override
    public boolean pointInView(float x,float y){
        return x>=0&&x<mWidth && y>=0&&y<mHeight;
    }

    @Override
    public int getChildOffsetInParentX(){
        int offsetX = getX();
        if(mAdapterParent != null){
            return offsetX + mAdapterParent.getScrollX();
        }else if(getParent() != null){
            return offsetX + getParent().getScrollX();
        }

        return offsetX;
    }

    @Override
    public int getChildOffsetInParentY(){
        int offsetY = getY();
        if(mAdapterParent != null){
            return offsetY + mAdapterParent.getScrollY();
        }else if(getParent() != null){
            return offsetY + getParent().getScrollY();
        }

        return offsetY;
    }

    @Override
    public CharSequence getContentDescription(){
        return mContentDescription;
    }

    @Override
    public int getAccessibilityImportance() {
        return mImportanceForAccessibility;
    }

    @Override
    public boolean getBoundsOnScreenForAccessbility(Rect out) {
        return getBoundsOnScreen(out);
    }

    @Override
    public boolean isCheckable() {
        return false;
    }

    @Override
    public boolean isChecked() {
        return false;
    }

    private static final List<IAccessable> sEmptyAccessChilds = new ArrayList<IAccessable>(0);
    @Override
    public List<IAccessable> getChildrenForAccessibility(){
        return sEmptyAccessChilds;
    }


    /********************* end for accessibility ******************************/

}
