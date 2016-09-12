package example.chedifier.chedifier.module;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.AbsModule;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : CopySelf
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class OpenUrlByDefaultBrowser extends AbsModule {

    public OpenUrlByDefaultBrowser(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("打开知乎");
        return textView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {
        mContext.startActivity(generateWebIntent("http://www.zhihu.com"));
    }

    private Intent generateWebIntent(String url){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
//        String pkg = "com.UCMobile";
        String pkg = "com.android.browser";
        intent.setPackage(pkg);
//        intent.setClassName(pkg,"com.android.browser.BrowserActivity");
        intent.setData(Uri.parse(url));

        return intent;
    }
}
