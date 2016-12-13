package example.chedifier.talkbackdemo.talkback;

import android.annotation.TargetApi;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by chedifier on 2016/12/1.
 */
public class ReflectView {

    private Method getViewRootImplMethod = null;
    private Object viewRootImpObj = null;
    private Field mPrivateFlags2Field = null;  //int

    private static ReflectView sInstance;
    public static ReflectView getInstance(){
        if(sInstance == null){
            sInstance = new ReflectView();
        }

        return sInstance;
    }

    private ReflectView(){

        try {
            mPrivateFlags2Field = View.class.getDeclaredField("mPrivateFlags2");
            mPrivateFlags2Field.setAccessible(true);

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            getViewRootImplMethod = View.class.getDeclaredMethod("getViewRootImpl");
            getViewRootImplMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

    }

    public void setAccessibilityFocused(View view){
        if(mPrivateFlags2Field != null){
            try {
                int mPrivateFlags2 = mPrivateFlags2Field.getInt(view);
                mPrivateFlags2Field.set(view,mPrivateFlags2|0x04000000);
                Log.d(view.getClass().getSimpleName(),view.getId() + "setAccessibilityFocused mPrivateFlags2|0x04000000");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getViewRootImpl(View view){
        if(getViewRootImplMethod != null){
            if(viewRootImpObj == null){
                try {
                    viewRootImpObj = getViewRootImplMethod.invoke(view);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }

        return viewRootImpObj;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void clearAccessibilityFocusNoCallbacks(View view) {
        if (view.isAccessibilityFocused()) {
            setAccessibilityFocused(view);

            view.invalidate();
            view.sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED);
        }
    }

}
