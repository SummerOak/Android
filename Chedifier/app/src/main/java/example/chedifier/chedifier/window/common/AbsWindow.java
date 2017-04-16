package example.chedifier.chedifier.window.common;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Administrator on 2017/3/6.
 */

public abstract class AbsWindow extends FrameLayout {

    protected String TAG = getClass().getSimpleName();

    private View mRoot;

    public AbsWindow(Context context) {
        super(context);

        
    }

    protected void onAttached(){
        if(mRoot == null){
            mRoot = onCreateContent();
        }

        if(mRoot != null){
            addView(mRoot);
        }
    }

    protected void onDetatched(){

    }

    protected abstract View onCreateContent();

}
