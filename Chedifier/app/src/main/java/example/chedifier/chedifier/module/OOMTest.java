package example.chedifier.chedifier.module;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Binder;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import example.chedifier.chedifier.base.AbsModule;

/**
 * Created by chedifier on 2016/12/13.
 */
public class OOMTest extends AbsModule {

    public OOMTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("OOM tester");
        return textView;
    }

    @Override
    public void onClick(View v) {

        testBinder();

    }

    private void testBinder(){
        for(int i = 0; i < 100; i ++) {

            final int index = i;
            new Thread(){
                public void run() {
                    ActivityManager activityManager =
                            (ActivityManager) mContext.getSystemService(Activity.ACTIVITY_SERVICE);

                    synchronized (OOMTest.this){
//                    for(int i=0;i<100000;i++){
                        List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(Integer.MAX_VALUE);
                        Log.d(TAG,">>> " + index + " size =" + serviceList.size());
//                        Binder.flushPendingCommands();
                        Log.d(TAG,">>> " + index + " flushed size =" + serviceList.size());
//                    }
                    }


                    try {
                        Thread.sleep(100000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }.start();


            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

}
