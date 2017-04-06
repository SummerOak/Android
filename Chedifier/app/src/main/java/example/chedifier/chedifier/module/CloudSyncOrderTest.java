package example.chedifier.chedifier.module;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.MainActivity;
import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.window.CloudSyncOrderTestWindow;

/**
 * Created by Administrator on 2017/3/28.
 */

public class CloudSyncOrderTest extends AbsModule {
    public CloudSyncOrderTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("测试云同步");
        return textView;
    }

    @Override
    public void onClick(View v) {
        MainActivity.getEnv().pushWindow(new CloudSyncOrderTestWindow(mContext));
    }
}
