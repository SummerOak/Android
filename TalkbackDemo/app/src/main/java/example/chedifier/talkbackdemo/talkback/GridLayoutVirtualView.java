package example.chedifier.talkbackdemo.talkback;

import android.content.Context;

import example.chedifier.talkbackdemo.customview.VirtualView;
import example.chedifier.talkbackdemo.customview.VirtualViewGroup;


/**
 * Created by chedifier on 2016/11/17.
 */
public class GridLayoutVirtualView extends VirtualViewGroup {

    private int mColumns;

    public GridLayoutVirtualView(Context context, int colums) {
        super(context);

        mColumns = colums;
    }

    @Override
    public void layout(int l, int t, int r, int b) {
        super.layout(l, t, r, b);

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
