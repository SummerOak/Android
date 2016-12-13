package example.chedifier.chedifier.customview;

import android.content.Context;
import android.graphics.Canvas;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.chedifier.accessibility.IAccessable;

/**
 * Created by chedifier on 2016/11/17.
 */
public class VirtualViewGroup extends VirtualView {

    protected List<VirtualView> mChilds = new ArrayList<>();

    public VirtualViewGroup(Context context) {
        super(context);
    }

    public void addChild(VirtualView view){
        mChilds.add(view);
        view.setParent(this);
    }

    public boolean dispatchTouchEvent(MotionEvent event){
        for(int i=0;i<mChilds.size();i++){
            VirtualView c = mChilds.get(i);
            MotionEvent ce = MotionEvent.obtain(event);
            ce.offsetLocation(-c.getX(),-c.getY());

            if(c.dispatchTouchEvent(ce)){
                return true;
            }
        }

        return onTouchEvent(event);
    }

    @Override
    public void measure(int w, int h) {
        super.measure(w, h);

        for(int i=0;i<mChilds.size();i++) {
            VirtualView child = mChilds.get(i);
            child.measure(mWidth,mHeight);
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        for(int i=0;i<mChilds.size();i++){
            VirtualView child = mChilds.get(i);
            if (!child.isVisible()) {
                continue;
            }
            canvas.save();
            int childX = child.getX();
            int childY = child.getY();
            canvas.translate(childX, childY);
            child.draw(canvas);
            canvas.restore();

        }
    }

    public List<VirtualView> getChildern() {
        return mChilds;
    }

    private List<IAccessable> mChildsForAccessibility = new ArrayList<IAccessable>();
    private boolean mChildrenChanged = true;

    private void updateAccessibilityChild(){
        mChildrenChanged = true;
    }

    @Override
    public List<IAccessable> getChildrenForAccessibility(){
        if(mChildrenChanged){
            mChildsForAccessibility.clear();
            mChildsForAccessibility.addAll(getChildern());

            mChildrenChanged = false;
        }

        return mChildsForAccessibility;
    }

}
