package example.chedifier.chedifier.window;

import android.content.Context;
import android.view.View;

import example.chedifier.chedifier.test.cloudsync.BookmarkCloudSyncCtrl;
import example.chedifier.chedifier.test.cloudsync.list.CloudSyncController;
import example.chedifier.chedifier.window.common.AbsWindow;

/**
 * Created by Administrator on 2017/3/28.
 */

public class CloudSyncOrderTestWindow extends AbsWindow{

    private Context mContext;
    private BookmarkCloudSyncCtrl mCtrl;

    public CloudSyncOrderTestWindow(Context context) {
        super(context);
        mContext= context;
        setBackgroundColor(0xff1d1d1d);

        mCtrl = new BookmarkCloudSyncCtrl(mContext);
    }

    @Override
    protected View onCreateContent() {
        return mCtrl.getView();
    }



}
