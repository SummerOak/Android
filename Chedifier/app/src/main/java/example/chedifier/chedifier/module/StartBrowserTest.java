package example.chedifier.chedifier.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.TextView;

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
public class StartBrowserTest extends AbsModule {

    public StartBrowserTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("测试启动浏览器");
        return textView;
    }

    @Override
    public void onClick(View v) {

        startWXEntryInUC();

//        Intent intent = new Intent();
//        intent.setComponent(new ComponentName("com.UCMobile", "com.uc.urlfilter.UrlFilterLocalBlackListActivity"));
//        mContext.startActivity(intent);

//        mContext.startActivity(generateUCHotNews());

//                createShortcut("草",R.drawable.hotnews_shortcut,generateUCHotNews());

//                Intent baiduIntent = new Intent("com.uc.baidu.v1.action.SHORTCUT");
//                baiduIntent.addCategory("com.ucmobile.category.SHORTCUT");
//                try{
//                    startActivity(baiduIntent);
//                }catch (Exception e){
//                    e.printStackTrace();
//                }
    }

    private Intent generateUCHotNews(){
        Intent intent = new Intent();
        intent.setAction("com.UCMobile.intent.action.INVOKE");
//        intent.setPackage("com.UCMobile");
        intent.setComponent(new ComponentName("com.UCMobile", "com.UCMobile.main.UCMobile"));
        String data = "ext:info_flow_open_channel:ch_id=100&from=9";
//        intent.setData(Uri.parse(data));
        intent.putExtra("openurl","http://www.baidu.com");
//        intent.setData(Uri.parse("http://www.baidu.com"));


        return intent;
    }

    private void startWXEntryInUC(){
        String pkgName = "com.UCMobile.odm";
        String actName = pkgName + ".wxapi.WXEntryActivity";
        Intent intent = new Intent();
        intent.setClassName(pkgName,actName);

        mContext.startActivity(intent);
    }
}
