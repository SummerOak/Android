package example.chedifier.chedifier.module;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.util.Log;
import android.view.Choreographer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by Administrator on 2017/1/14.
 */

public class HWAccTest extends AbsModule {

    CustomView customView;

    public HWAccTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        customView = new CustomView(mContext);
        customView.setText("hardware acc test");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            customView.setLayerType(View.LAYER_TYPE_HARDWARE,null);
        }
        customView.setOnClickListener(this);
        return customView;
    }

    int count = 0;
    @Override
    public void onClick(View v) {
//        customView.invalidate();

        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run");
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                @Override
                public void doFrame(long frameTimeNanos) {
                    Log.i(TAG,"doFrame");
                }
            });

            Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
                @Override
                public void doFrame(long frameTimeNanos) {
                    Log.i(TAG,"doFrame2");
                }
            });
        }

        Log.i(TAG,"last1");

        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run2");
            }
        });
        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run3");
            }
        });
        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run4");
            }
        });
        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.i(TAG,"run5");
            }
        });
        customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"run6");
            }
        });customView.getHandler().post(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG,"run7");
            }
        });


    }


    private final class CustomView extends TextView{


        public CustomView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                if(canvas.isHardwareAccelerated()){
                    Log.d(TAG,"hardware Accelerated");
                    return;
                }
            }

            Log.d(TAG,"software Accelerated");
        }
    }

}
