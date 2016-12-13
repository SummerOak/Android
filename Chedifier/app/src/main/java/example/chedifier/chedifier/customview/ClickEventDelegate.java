package example.chedifier.chedifier.customview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by chedifier on 2016/11/18.
 */
public class ClickEventDelegate implements ITouchDelegate {


    private final static int LONG_CLICK_TIME = 700;
    protected static final int CLICK_SPOT = 15;

    protected boolean mOutsideClick = false;

    protected boolean mLongClickDone = false;

    private View.OnClickListener mOnClickListener;

    private View.OnLongClickListener mOnLongClickListener;

    public ClickEventDelegate(){

    }

    public void setOnClickListener(View.OnClickListener l){
        this.mOnClickListener = l;
    }

    public void setOnLongClickListener(View.OnLongClickListener l){
        this.mOnLongClickListener = l;
    }

    private void onClick(int x,int y){
        if(mOnClickListener != null){
            mOnClickListener.onClick(null);
        }
    }

    private void onLongClick(){
        if(mOnLongClickListener != null){
            mOnLongClickListener.onLongClick(null);
        }
    }

    private boolean hasClickDown = false;
    private boolean handle = false;
    private int mTouchDownX,mTouchDownY;

    @Override
    public boolean handleTouchEvent(MotionEvent e) {
        handle = false;
        int x = (int) e.getX();
        int y = (int) e.getY();
        switch(e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mTouchDownX = x;
                mTouchDownY = y;

                //reset touch state
                mOutsideClick = false;
                mLongClickDone = false;
                mLongClickHandler.sendMessageDelayed(mLongClickHandler.obtainMessage(MSG_LONGCLICK_TICK), LONG_CLICK_TIME);
                hasClickDown = true;
                handle = true;
                break;

            case MotionEvent.ACTION_MOVE:
                if (mLongClickDone) {
                    //long click trigger pushed, ignore any event after
                    return true;
                }
                boolean moveX = Math.abs(x - mTouchDownX) > CLICK_SPOT;
                boolean moveY = Math.abs(y - mTouchDownY) > CLICK_SPOT;
                if (moveX || moveY) {
                    mLongClickHandler.removeMessages(MSG_LONGCLICK_TICK);
                    mOutsideClick = true;
                } else {
                    handle = true;
                }

                break;

            case MotionEvent.ACTION_UP:
                //cancel the long click trigger
                mLongClickHandler.removeMessages(MSG_LONGCLICK_TICK);
                if (!mLongClickDone && !mOutsideClick && hasClickDown) {
                    onClick(x, y);
                    handle = true;
                }
                hasClickDown = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                mOutsideClick = true;
                break;
        }
        return handle;
    }

    private final static int MSG_LONGCLICK_TICK = 0x1001;

    /**
     * Long click trigger
     */
    private Handler mLongClickHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_LONGCLICK_TICK) {
                if (!mOutsideClick) {
                    onLongClick();
                    mLongClickDone = true;
                }
            }
        }
    };


}
