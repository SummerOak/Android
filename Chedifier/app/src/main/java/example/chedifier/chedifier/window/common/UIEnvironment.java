package example.chedifier.chedifier.window.common;

import android.content.Context;
import android.view.View;

/**
 * Created by Administrator on 2017/3/6.
 */

public class UIEnvironment {

    private WindowManager mWindowManager;

    public UIEnvironment(Context context){
        mWindowManager = new WindowManager(context);
    }

    public View getView(){
        return mWindowManager.getCurrentWindowStack();
    }

    public void pushWindow(AbsWindow window){
        mWindowManager.pushWindow(window);
    }

    public boolean popWindow(){
        return mWindowManager.popWindow();
    }

}
