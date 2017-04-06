package example.chedifier.chedifier.module;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/1/17.
 */

public class CoverPanel extends AbsModule {

    final int COVER_GREEN = 1;
    final int COVER_BLUE = 2;
    
    public CoverPanel(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView coverGreen = new TextView(mContext);
        decorateItem(coverGreen);
        coverGreen.setId(COVER_GREEN);
        coverGreen.setOnClickListener(this);
        coverGreen.setOnLongClickListener(this);
        coverGreen.setText("Cover Green");
        linearLayout.addView(coverGreen);

        TextView coverBlue = new TextView(mContext);
        decorateItem(coverBlue);
        coverBlue.setId(COVER_BLUE);
        coverBlue.setOnClickListener(this);
        coverBlue.setOnLongClickListener(this);
        coverBlue.setText("Cover Blue");
        linearLayout.addView(coverBlue);

        return linearLayout;
    }

    protected boolean rootViewDecoratable(){
        return false;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case COVER_GREEN:
                tryCover(0x4400ff00);
                break;

            case COVER_BLUE:
                tryCover(0x440000ff);
                break;
        }

    }

    private void tryCover(int color){
        if(mContext instanceof Activity){

            CoverLayer layer = new CoverLayer(mContext);
            layer.setBackgroundColor(color);

            Activity activity = (Activity)mContext;
            ((ViewGroup)activity.getWindow().getDecorView()).addView(layer);
        }
    }

    private void tryUnCover(){
        if(mContext instanceof Activity){
            Activity activity = (Activity)mContext;
            ViewGroup viewGroup = (ViewGroup)activity.getWindow().getDecorView();
            viewGroup.removeViewAt(viewGroup.getChildCount()-1);
        }
    }

    private class CoverLayer extends FrameLayout implements View.OnLongClickListener{

        public CoverLayer(Context context) {
            super(context);

            setOnLongClickListener(CoverLayer.this);
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent ev) {
            return true;
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            return super.dispatchTouchEvent(ev);
        }

        @Override
        public boolean onLongClick(View v) {
            Log.d(TAG,"onLongClick " + v.getClass().getSimpleName());
            tryUnCover();
            return true;
        }
    }
}
