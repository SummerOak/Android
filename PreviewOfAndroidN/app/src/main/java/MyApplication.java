import android.app.Application;
import android.util.Log;

import com.example.chedifier.previewofandroidn.download.DownloadTask;
import com.example.chedifier.previewofandroidn.download.DownloadTaskMgr;

/**
 * Created by chedifier on 2016/3/22.
 */
public class MyApplication extends Application{

    private static final String TAG = MyApplication.class.getSimpleName();

    @Override
    public void onCreate(){
        Log.d(TAG,"onCreate");

        super.onCreate();

        DownloadTaskMgr.getInstance().init(this);
    }

    @Override
    public void onTerminate() {
        Log.d(TAG,"onTerminate");

        super.onTerminate();

        DownloadTaskMgr.getInstance().release(this);
    }

}
