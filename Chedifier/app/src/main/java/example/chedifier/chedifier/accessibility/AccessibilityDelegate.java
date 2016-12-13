/**
 * ****************************************************************************
 * Description : a delegate to
 *      1. handle accessibility event
 *      2. and provide abilities to descript the tree hierarchy of virtual view
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */

package example.chedifier.chedifier.accessibility;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;


import java.util.List;

public class AccessibilityDelegate{

    private static final String TAG = "AccessibilityDelegate";

    private static final boolean DEBUG = false;

    protected static int sVirturalViewIdCounter = 0;
    protected int mVirturalViewId = (++sVirturalViewIdCounter== View.NO_ID?++sVirturalViewIdCounter:sVirturalViewIdCounter);

    private IAccessable mHost;
    private boolean mAccessibilityFocused = false;

    private boolean mOnlyTakeOverEventOfChildren = false;

    public AccessibilityDelegate(IAccessable host){
        this(host,false);
    }

    public AccessibilityDelegate(IAccessable host,boolean onlyTakeOverEventOfChildren){
        if(host == null) throw new NullPointerException("host cannot be null.");

        mHost = host;
        mOnlyTakeOverEventOfChildren = onlyTakeOverEventOfChildren;
    }

    private Context getContext(){
        return mHost.getAdapterViewParent()==null?mHost.getAdapterViewParent().getContext():null;
    }

    public boolean isAccessibilityFocused(){
        return mAccessibilityFocused;
    }

    public boolean dispatchHoverEvent(MotionEvent event) {
        return mHoverEventDispatcher.dispatchHoverEvent(event);
    }

    private boolean mHaveHoverEntered = false;
    private boolean onHoverEvent(MotionEvent event) {
        if(DEBUG) Log.d(TAG,"onHoverEvent " + event);

        final int action = event.getAction();
        if(mHost.pointInView((int)event.getX(),(int)event.getY())){
            switch (action) {
                case MotionEvent.ACTION_HOVER_ENTER: {

                    if(DEBUG) Log.d(TAG,"event_state entering " + mVirturalViewId);

                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_ENTER,mVirturalViewId);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                        sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED,mVirturalViewId);
//                    }

                    mHaveHoverEntered = true;
                } break;
                case MotionEvent.ACTION_HOVER_MOVE: {
                    if(DEBUG) Log.d(TAG,"event_state moving " + mVirturalViewId);
                } break;
                case MotionEvent.ACTION_HOVER_EXIT:{
                    if(DEBUG) Log.d(TAG,"event_state exiting " + mVirturalViewId);
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_EXIT,mVirturalViewId);

                    mHaveHoverEntered = false;
                }break;
            }

            return true;
        }else if(mHaveHoverEntered &&
                (action == MotionEvent.ACTION_HOVER_EXIT ||
                        action == MotionEvent.ACTION_HOVER_MOVE)){
            if(DEBUG) Log.d(TAG,"event_state exiting " + mVirturalViewId);
            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_HOVER_EXIT,mVirturalViewId);

            mHaveHoverEntered = false;
        }

        return false;
    }

    public void fillVirtualViewDecendants(View adapterView, AccessibilityNodeInfo info){
        if(adapterView != null && info != null){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                info.addChild(adapterView,mVirturalViewId);
            }
            for(int i=0;i<mHost.getChildrenForAccessibility().size();i++){
                mHost.getChildrenForAccessibility().get(i).getAccessibilityDelegator()
                        .fillVirtualViewDecendants(adapterView,info);
            }
        }
    }

    private Rect mTempRect = new Rect();
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void fillVirtualViewNodeInfo(View adapterView, int targetId,AccessibilityNodeInfo info){
        if(adapterView != null && info != null){
            IAccessable target = findAccessableByVirturalId(targetId);
            if(target != null){
                info.addAction(AccessibilityNodeInfo.ACTION_SELECT);
                info.addAction(AccessibilityNodeInfo.ACTION_CLEAR_SELECTION);

                info.setPackageName(getContext()==null?"":getContext().getPackageName());
                info.setClassName(getClass().getName());
                if(target.getBoundsInParent(mTempRect)){
                    info.setBoundsInParent(mTempRect);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    info.setVisibleToUser(target.isVisible() && target.getGlobalVisibleRectForAccessibility(mTempRect));
                }

                info.setBoundsInScreen(mTempRect);
                info.setText(target.getContentDescription());

                if(target.isCheckable()){
                    info.setCheckable(target.isCheckable());
                    info.setChecked(target.isChecked());
                }
            }
        }
    }

    public IAccessable findAccessableByVirturalId(int virtualId){
        if(virtualId == mVirturalViewId){
            return mHost;
        }

        List<IAccessable> childs = mHost.getChildrenForAccessibility();
        for(int i=0;i<childs.size();i++){
            IAccessable target = childs.get(i).getAccessibilityDelegator()
                    .findAccessableByVirturalId(virtualId);
            if(target != null){
                return target;
            }
        }

        return null;
    }

    public boolean performAccessibilityAction(int action, int vid, Bundle arguments) {
        if(DEBUG) Log.d(TAG,getClass().getSimpleName()
                + "performAccessibilityAction action " + AccessibilityHelper.parseNodeAction(action)
                + " arg: " + arguments);

        switch (action) {
            case AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED,vid);
                }
                return true;
            case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED,vid);
                }
                return true;

            default:
        }
        return false;
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    public void sendAccessibilityEvent(int eventType) {
        if(DEBUG) Log.d(TAG,"sendAccessibilityEvent " + AccessibilityEvent.eventTypeToString(eventType)
                + " " + getClass().getSimpleName()
                + " mVirturalViewId " + mVirturalViewId);

        sendAccessibilityEvent(eventType,mVirturalViewId);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    protected void sendAccessibilityEvent(int eventType, int virturalViewId){
        if(!AccessibilityHelper.isAccessibilityEnable()){
            return;
        }

        IAccessable target = findAccessableByVirturalId(virturalViewId);
        if (target != null) {
            AccessibilityEvent event = AccessibilityEvent.obtain(eventType);
            event.setPackageName(getContext()==null?"":getContext().getPackageName());
            event.setClassName(getClass().getName());
            View adapterView = target.getAdapterViewParent();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                event.setSource(adapterView, virturalViewId);
            }
            event.getText().add(mHost.getContentDescription());

            if(adapterView != null){
                AccessibilityHelper.logSendAccessibilityEvent(adapterView,event);
                adapterView.getParent().requestSendAccessibilityEvent(adapterView, event);

                if(virturalViewId == mVirturalViewId){
                    updateAccessibilityFocusState(eventType);
                }
            }
        }
    }

    private void updateAccessibilityFocusState(int eventType){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            if(eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED){
                mAccessibilityFocused = true;
            }

            if(eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED){
                mAccessibilityFocused = false;
            }
        }
    }

    protected HoverEventDispatcher mHoverEventDispatcher = new HoverEventDispatcher(new HoverEventDispatcher.IHoverLogicProvider<IAccessable>() {
        @Override
        public int getChildCount() {
            return mHost.getChildrenForAccessibility().size();
        }

        @Override
        public IAccessable getChildAt(int index) {
            return mHost.getChildrenForAccessibility().get(index);
        }

        @Override
        public boolean isChildHoverableCurrent(IAccessable child) {
            return child!=null&&child.isVisible()
                    &&child.getAccessibilityImportance()!= View.IMPORTANT_FOR_ACCESSIBILITY_NO;
        }

        @Override
        public boolean isChildUnderPoint(IAccessable child, float x, float y) {
            return child!=null&&child.pointInView(x-getChildOffsetInParentX(child),
                    y-getChildOffsetInParentY(child));
        }

        @Override
        public int getChildOffsetInParentX(IAccessable child) {
            return (child!=null?child.getChildOffsetInParentX():0);
        }

        @Override
        public int getChildOffsetInParentY(IAccessable child) {
            return (child!=null?child.getChildOffsetInParentY():0);
        }

        @Override
        public boolean dispatchEventToChild(IAccessable child, MotionEvent event) {
            return child!=null&&child.getAccessibilityDelegator().dispatchHoverEvent(event);
        }

        @Override
        public boolean dispatchEventToSelf(MotionEvent event) {
            if(mOnlyTakeOverEventOfChildren){ //如果指定了只处理子控件的事件，对于根节点控件，返回false给根结点自己处理
                return false;
            }

            if(mHost.isVisible()
                    && mHost.getAccessibilityImportance()== View.IMPORTANT_FOR_ACCESSIBILITY_YES){
                return onHoverEvent(event);
            }
            return false;
        }
    });

}
