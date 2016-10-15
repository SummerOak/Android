package example.chedifier.chedifier.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
public class WindowLeakTest extends AbsModule {

    public WindowLeakTest(Context context) {
        super(context);
    }

    @Override
    protected View createView(int pos) {
        TextView textView = new TextView(mContext);
        textView.setText("测试窗口泄漏");

        TreeMap<String, String> map = new TreeMap<>();
        Set<TreeMap.Entry<String, String>> entrySets = map.entrySet();
        for (TreeMap.Entry<String, String> entrySet: entrySets) {
            String key = entrySet.getKey();
            String value = entrySet.getValue();
        }
        return textView;
    }

    @Override
    public void onClick(View v) {
        //testWindowLeak();

        android.util.Log.d(TAG,getDiviceId());

        try {
            createPersonalAccountJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            createCommonODMAccountJson();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getDiviceId(){
        TelephonyManager tm = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
        String DEVICE_ID = tm.getDeviceId();
        return DEVICE_ID;
    }

    private void createPersonalAccountJson() throws JSONException {
        JSONObject root = new JSONObject();

        //agoo
        JSONObject account = new JSONObject();
        account.put("key","21711551");
        account.put("secret","52794363abe78b68c1fbebe92a6c96ed");
        root.put("agoo",account);

        //微信
        account = new JSONObject();
        account.put("key","wx020a535dccd46c11");
        account.put("secret","e73cedfab8d7a82f56814488f9bdb035");
        root.put("weixin",account);

        //QQ
        account = new JSONObject();
        account.put("key","1101031180");
        account.put("secret","");
        root.put("qq",account);

        //微博
        account = new JSONObject();
        account.put("key","3982225019");
        account.put("secret","");
        root.put("qq",account);

        Log.d(TAG,root.toString());
    }

    private void createCommonODMAccountJson() throws JSONException {
        JSONObject root = new JSONObject();

        //agoo
        JSONObject account = new JSONObject();
        account.put("key","23467295");
        account.put("secret","e616557cd810027df1591a82ec94c0ea");
        root.put("agoo",account);

        //微信
        account = new JSONObject();
        account.put("key","wx110341b81c08d810");
        account.put("secret","eed1402a6072ecacf1769109e7fe87a2");
        root.put("weixin",account);

        Log.d(TAG,root.toString());
    }

    private void testWindowLeak(){

        final AlertDialog dialog = new AlertDialog.Builder(mContext)
                .setTitle("title")
                .setMessage("message")
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {

                    }
                }).create();


//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }

        dialog.show();

        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
//        mH.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Log.i("cqx","show");
//                while(true){
//                    dialog.show();
//                }
//            }
//        },200);

        if(mContext instanceof Activity){
            ((Activity)mContext).finish();
        }
    }
}
