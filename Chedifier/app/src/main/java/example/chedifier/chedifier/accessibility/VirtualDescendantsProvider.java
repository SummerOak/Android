/**
 * ****************************************************************************
 * Description : a common relization of {@link android.view.accessibility.AccessibilityNodeProvider}
 *  always used in original view in android {@link android.view.View#getAccessibilityNodeProvider()}
 *
 *  1. provide the details(tree hierarchy) of virtual views inside target android view
 *  2. operate interfaces to perform actions on virtual view
 *
 * Creation : 2016/11/22
 * Author 	: chedifier
 * ****************************************************************************
 */
package example.chedifier.chedifier.accessibility;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.accessibility.AccessibilityNodeProvider;


import java.util.List;


@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
public class VirtualDescendantsProvider extends AccessibilityNodeProvider {

    private static final String TAG = "VirtualDescendantsProvider";

    private static final boolean DEBUG = false;

    private View mView;
    private List<IAccessable> mVirtualChildren;

    public VirtualDescendantsProvider(View root, List<IAccessable> virturalChildren){
        if(root == null) throw new NullPointerException("root cannot be null.");

        mView = root;
        mVirtualChildren = virturalChildren;
    }

    @Override
    public AccessibilityNodeInfo createAccessibilityNodeInfo(int virtualViewId) {
        if(DEBUG) Log.d(TAG, "createAccessibilityNodeInfo " + virtualViewId);
        AccessibilityNodeInfo info = null;

        if(virtualViewId == View.NO_ID){
            info = AccessibilityNodeInfo.obtain(mView);
            mView.onInitializeAccessibilityNodeInfo(info);
            if(mVirtualChildren != null){
                for(IAccessable child:mVirtualChildren){
                    child.getAccessibilityDelegator().fillVirtualViewDecendants(mView,info);
                }
            }
        }else {
            info = AccessibilityNodeInfo.obtain();
            info.setSource(mView,virtualViewId);
            info.setParent(mView,virtualViewId);

            IAccessable target = findAccessableByVirturalId(virtualViewId);
            if(target != null){
                target.getAccessibilityDelegator().
                        fillVirtualViewNodeInfo(mView,virtualViewId,info);
            }
        }

        return info;
    }

    @Override
    public List<AccessibilityNodeInfo> findAccessibilityNodeInfosByText(String searched,
                                                                        int virtualViewId) {
        if(DEBUG) Log.d(TAG, "findAccessibilityNodeInfosByText " + searched + " virtualViewId " + virtualViewId);
        return super.findAccessibilityNodeInfosByText(searched,virtualViewId);
    }

    @Override
    public boolean performAction(int virtualViewId, int action, Bundle arguments) {
        if(DEBUG) Log.d(TAG, "performAction " + virtualViewId + " action " + AccessibilityHelper.parseNodeAction(action));

        if(virtualViewId == View.NO_ID){
            switch (action) {
                case AccessibilityNodeInfo.ACTION_CLEAR_ACCESSIBILITY_FOCUS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
                    }
                    return true;
                case AccessibilityNodeInfo.ACTION_ACCESSIBILITY_FOCUS:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        mView.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED);
                    }
                    return true;

                default:
            }
            return mView.performAccessibilityAction(action,arguments);
        }else{
            IAccessable target = findAccessableByVirturalId(virtualViewId);
            if(target != null){
                return target.getAccessibilityDelegator()
                        .performAccessibilityAction(action,virtualViewId,arguments);
            }
        }

        return super.performAction(virtualViewId,action,arguments);
    }

    private IAccessable findAccessableByVirturalId(int vid){
        if(mVirtualChildren != null){
            for(IAccessable child:mVirtualChildren){
                IAccessable a = child.getAccessibilityDelegator().findAccessableByVirturalId(vid);

                if(a != null){
                    return a;
                }
            }
        }

        return null;
    }

}