package example.chedifier.chedifier.window.common;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/3/6.
 */

public class WindowManager {


    private WindowStack mWindowStack;

    public WindowManager(Context context){
        mWindowStack = new WindowStack(context);
    }

    public WindowStack getCurrentWindowStack(){
        return mWindowStack;
    }

    public void pushWindow(AbsWindow window){
        if(mWindowStack != null){
            AbsWindow peek = mWindowStack.peek();
            mWindowStack.pushWindow(window);
            if(peek != null){
                peek.setVisibility(View.GONE);
            }
        }
    }

    public boolean popWindow(){
        if(mWindowStack != null){
            boolean result = mWindowStack.popWindow();
            AbsWindow peek = mWindowStack.peek();
            if(peek != null){
                peek.setVisibility(View.VISIBLE);
            }

            return result;
        }

        return false;
    }



}
