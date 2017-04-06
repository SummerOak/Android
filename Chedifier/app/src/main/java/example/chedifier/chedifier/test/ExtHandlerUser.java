package example.chedifier.chedifier.test;

import android.os.Message;
import android.util.Log;

import example.chedifier.chedifier.base.ExtHandler;

/**
 * Created by chedifier on 2016/12/19.
 */
public class ExtHandlerUser {


    public void start(){
        mHandler.sendEmptyMessageDelayed(0,1000);
//        mHandler2.sendEmptyMessageDelayed(0,1000);
//        mHandler3.sendEmptyMessageDelayed(0,1000);
    }

    int i = 0;
    private void log(){
        Log.d("ExtHandlerUser","" + (++i));
    }

    ExtHandler mHandler = new ExtHandler("a"){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if(ExtHandlerUser.this != null){
                log();
                mHandler.sendEmptyMessageDelayed(0,1000);
            }

        }
    };

    ExtHandler mHandler2 = new ExtHandler("b", new ExtHandler.IMessageHandler() {
        @Override
        public void handleMessage(Message msg) {
            if(ExtHandlerUser.this != null){
                log();
                mHandler2.sendEmptyMessageDelayed(0,1000);
            }
        }
    });

    ExtHandler.IMessageHandler mIMessageHandler = new ExtHandler.IMessageHandler() {
        @Override
        public void handleMessage(Message msg) {
            if(ExtHandlerUser.this != null){
                log();
                mHandler3.sendEmptyMessageDelayed(0,1000);
            }
        }
    };
    ExtHandler mHandler3 = new ExtHandler("b",mIMessageHandler);
}
