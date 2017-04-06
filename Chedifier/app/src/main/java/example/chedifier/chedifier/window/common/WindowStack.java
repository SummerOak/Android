package example.chedifier.chedifier.window.common;

import android.content.Context;
import android.widget.FrameLayout;

import java.util.Stack;

/**
 * Created by Administrator on 2017/3/6.
 */

public class WindowStack extends FrameLayout {

    private Stack<AbsWindow> mStackWindows = new Stack<>();

    public WindowStack(Context context) {
        super(context);
    }

    public void pushWindow(AbsWindow window){
        mStackWindows.push(window);
        addView(window);
        bringChildToFront(window);
        window.onAttached();
    }

    public  AbsWindow peek(){
        return mStackWindows.size() > 0?mStackWindows.peek():null;
    }

    public boolean popWindow(){
        if(mStackWindows.size() > 1){
            AbsWindow window = mStackWindows.pop();
            removeView(window);
            window.onDetatched();
            return true;
        }

        return false;
    }

}
