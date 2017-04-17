package example.chedifier.chedifier.test;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

/**
 * Created by Administrator on 2017/4/10.
 */

public class HandlerTest {

    private int count = 0;

    private Handler mHandler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Log.i("HandlerTest","" + (count++));

            removeMessages(0);
            sendEmptyMessageDelayed(0,200);
        }
    };

    public void start(){
        mHandler.sendEmptyMessage(0);
    }

}
