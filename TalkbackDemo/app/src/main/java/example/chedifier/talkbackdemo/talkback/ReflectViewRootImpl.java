package example.chedifier.talkbackdemo.talkback;

import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chedifier on 2016/12/1.
 */
public class ReflectViewRootImpl {

    private Field mAccessibilityFocusedVirtualView_Field = null; //AccessibilityNodeInfo
    private Field mAccessibilityFocusedHost_Field = null;  //View
    private Method setAccessibilityFocusMethod = null;

    private Field mAttachInfo_Field = null; //View.AttachInfo

    private Field mHardwareRenderer_Field = null;//HardwareRenderer
    private Method invalidateRoot_Method = null;

    private Class viewRootImp;

    private static ReflectViewRootImpl sInstance;
    public static ReflectViewRootImpl getInstance(){
        if(sInstance == null){
            sInstance = new ReflectViewRootImpl();
        }

        return sInstance;
    }

    private ReflectViewRootImpl(){

        try {
            viewRootImp = Class.forName("android.view.ViewRootImpl");
            setAccessibilityFocusMethod = viewRootImp.getDeclaredMethod("setAccessibilityFocus",View.class,AccessibilityNodeInfo.class);
            setAccessibilityFocusMethod.setAccessible(true);

            mAccessibilityFocusedVirtualView_Field = viewRootImp.getDeclaredField("mAccessibilityFocusedVirtualView");
            mAccessibilityFocusedVirtualView_Field.setAccessible(true);
            mAccessibilityFocusedHost_Field = viewRootImp.getDeclaredField("mAccessibilityFocusedHost");
            mAccessibilityFocusedHost_Field.setAccessible(true);
            mAttachInfo_Field = viewRootImp.getDeclaredField("mAttachInfo");
            mAttachInfo_Field.setAccessible(true);

            mHardwareRenderer_Field = mAttachInfo_Field.getType().getDeclaredField("mHardwareRenderer");
            mHardwareRenderer_Field.setAccessible(true);
            invalidateRoot_Method = mHardwareRenderer_Field.getType().getDeclaredMethod("invalidateRoot");
            invalidateRoot_Method.setAccessible(true);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

    }

    public Class getTargetClass(){
        return viewRootImp;
    }

    public void setAccessibilityFocus(Object receiver,View view,AccessibilityNodeInfo nodeInfo){
        if(setAccessibilityFocusMethod != null){
            try {
                setAccessibilityFocusMethod.invoke(receiver,view,nodeInfo);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    public AccessibilityNodeInfo mAccessibilityFocusedVirtualView(Object receiver){
        if(mAccessibilityFocusedVirtualView_Field != null){
            try {
                return (AccessibilityNodeInfo)mAccessibilityFocusedVirtualView_Field.get(receiver);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setmAccessibilityFocusedVirtualView(Object receiver,AccessibilityNodeInfo value){
        if(mAccessibilityFocusedVirtualView_Field != null){
            try {
                mAccessibilityFocusedVirtualView_Field.set(receiver,value);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public View mAccessibilityFocusedHost(Object receiver){
        if(mAccessibilityFocusedHost_Field != null){
            try {
                return (View) mAccessibilityFocusedHost_Field.get(receiver);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void setmAccessibilityFocusedHost(Object receiver,View value){
        if(mAccessibilityFocusedHost_Field != null){
            try {
                mAccessibilityFocusedHost_Field.set(receiver,value);
                Log.d(receiver.getClass().getSimpleName(),"setmAccessibilityFocusedHost");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public void invalidateRoot(Object receiver){
        if(invalidateRoot_Method != null){
            try {

                Object attachInfo = mAttachInfo_Field.get(receiver);
                Object renderer = mHardwareRenderer_Field.get(attachInfo);
                invalidateRoot_Method.invoke(renderer);

                Log.d(receiver.getClass().getSimpleName(),"invalidateRoot");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

}
