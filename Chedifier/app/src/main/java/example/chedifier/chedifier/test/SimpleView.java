package example.chedifier.chedifier.test;

import android.content.Context;
import android.graphics.Canvas;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2017/3/1.
 */

public class SimpleView {

    private Context mContext;
    private View mView;

    private int c = 0;

    public SimpleView(Context context){
        mContext = context;

        FrameLayout frameLayout = new FrameLayout(mContext);
        frameLayout.setBackgroundColor(0xffededed);
        final TextView textView = new MyTextView(mContext);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT,FrameLayout.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        frameLayout.addView(textView,lp);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(18);
        textView.setTextColor(0xffff0000);
        textView.setText(""+c);

        frameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                textView.setText("onTouch"+(c++));
                return false;
            }
        });

        frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("onClick"+(c++));
                Log.d("MyTextView","setText  " + (c) + "  " + textView);
            }
        });

        mView = frameLayout;
    }

    public View getView(){
        return mView;
    }

    private class MyTextView extends TextView{
        public MyTextView(Context context) {
            super(context);
        }

        @Override
        protected void dispatchDraw(Canvas canvas) {
            Log.d("MyTextView","dispatchDraw  " + this);
            super.dispatchDraw(canvas);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            Log.d("MyTextView","onDraw  " + this);
            super.onDraw(canvas);
        }

        @Override
        public void draw(Canvas canvas) {
            Log.d("MyTextView","draw  " + this);
            super.draw(canvas);
        }
    }


}
