package example.chedifier.chedifier.test.talkback;

import android.content.Context;
import android.util.Log;

import example.chedifier.chedifier.MyApplication;
import example.chedifier.chedifier.customview.VirtualView;
import example.chedifier.chedifier.customview.VirtualViewGroup;

/**
 * Created by chedifier on 2016/11/17.
 */
public class GridLayoutVirtualView extends VirtualViewGroup {

    private int mColumns;

    public GridLayoutVirtualView(Context context,int colums) {
        super(context);

        mColumns = colums;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);

        Log.d("cqx_trace","before get lock");
        synchronized (MyApplication.getAppContext()){
            Log.d("cqx_trace","after get lock");
        }

        int x=l,y=t;
        for(int i=0;i<mChilds.size();i++){
            VirtualView c = mChilds.get(i);

            c.layout(x,y,x+mWidth,y+mHeight);

            if((i+1)%mColumns == 0){
                x = 0;
                y += (c.getHeight() + c.getMarginTop() + c.getMarginBottom());
            }else{
                x += (c.getWidth() + c.getMarginLeft() + c.getMarginRight());
            }

        }

    }
}
