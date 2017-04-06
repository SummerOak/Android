package example.chedifier.chedifier.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.test.SimpleView;

/**
 * Created by Administrator on 2017/3/1.
 */

public class RemoveViewInSubThread extends AbsModule {

    public RemoveViewInSubThread(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView t = new TextView(mContext);
        t.setText("startRemoveViewInSubThread");

        return t;
    }

    @Override
    public void onClick(View v) {
        if(mContext instanceof Activity){

            Activity activity = (Activity)mContext;
            final ViewGroup decor = (ViewGroup)activity.getWindow().getDecorView();

            final int maxView = 100;
            final View[] mViews = new View[maxView];

            for(int i=0;i<maxView;i++){
                mViews[i] = new SimpleView(activity).getView();
                decor.addView(mViews[i],ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
            }

            for(int i=maxView-1;i>0;i--){
                final int fi = i;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        decor.removeView(mViews[fi]);
                    }
                }).start();
            }
        }
    }
}
