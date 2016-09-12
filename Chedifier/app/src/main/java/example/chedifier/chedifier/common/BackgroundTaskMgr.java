package example.chedifier.chedifier.common;

import android.os.Handler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by chedifier on 2016/6/8.
 */
public class BackgroundTaskMgr {

    private static final String TAG = BackgroundTaskMgr.class.getSimpleName();

    private static BackgroundTaskMgr sInstance;

    private boolean mHasInited = false;

    private ExecutorService mCachedThreadPool;

    private HandlerThreadWrapper mBackgroundThread;

    private BackgroundTaskMgr(){
        ;
    }

    public void init(){
        if(mHasInited){
            return;
        }

        mCachedThreadPool = Executors.newFixedThreadPool(3);

        mBackgroundThread = new HandlerThreadWrapper(TAG);

        mHasInited = true;
    }

    public static synchronized BackgroundTaskMgr getInstance(){
        if(sInstance == null){
            sInstance = new BackgroundTaskMgr();
        }

        return sInstance;
    }


    public boolean runTask(Runnable task){
        if(!mHasInited){
            return false;
        }

        mCachedThreadPool.execute(task);


        return true;
    }

    public boolean runTask(final Runnable task,long delay){
        if(!mHasInited){
            return false;
        }

        mBackgroundThread.getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runTask(task);
            }
        }, delay);

        return true;
    }


}
