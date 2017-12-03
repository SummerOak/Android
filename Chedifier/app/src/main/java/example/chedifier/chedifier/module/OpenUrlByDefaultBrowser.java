package example.chedifier.chedifier.module;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
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
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView query = new TextView(mContext);
        decorateItem(query);
        query.setId(1);
        query.setOnClickListener(this);
        query.setOnLongClickListener(this);
        query.setText("open url by UCMobile");
        linearLayout.addView(query);

        TextView delete = new TextView(mContext);
        decorateItem(delete);
        delete.setId(2);
        delete.setOnClickListener(this);
        delete.setOnLongClickListener(this);
        delete.setText("open url by browser");
        linearLayout.addView(delete);

        return linearLayout;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case 1:
                mContext.startActivity(generateWebIntent("http://www.zhihu.com","com.UCMobile"));
                break;
            case 2:
                mContext.startActivity(generateWebIntent("http://www.zhihu.com","com.android.browser"));
                break;
        }



    }

    private Intent generateWebIntent(String url,String pkg){
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setPackage(pkg);
        intent.setData(Uri.parse(url));

        return intent;
    }
}
