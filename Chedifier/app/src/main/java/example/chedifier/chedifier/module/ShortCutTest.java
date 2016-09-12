package example.chedifier.chedifier.module;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import example.chedifier.chedifier.MainActivity;
import example.chedifier.chedifier.R;
import example.chedifier.chedifier.base.AbsModule;
import example.chedifier.chedifier.common.ShortCutHelper;
import example.chedifier.chedifier.utils.ScreenUtils;

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
public class ShortCutTest extends AbsModule {

    public ShortCutTest(Context context) {
        super(context);
    }

    private final int createShortcutID = 1;
    private final int checkShortcutID = 2;
    private final int removeShortcutID = 3;
    private String mShortTitle = "程~";

    @Override
    protected View createView(int pos) {

        int padding = (int)ScreenUtils.dipToPixels(10);
        LinearLayout linearLayout = new LinearLayout(mContext);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        TextView createShortcut = new TextView(mContext);
        createShortcut.setPadding(padding,0,padding,padding);
        createShortcut.setId(createShortcutID);
        createShortcut.setOnClickListener(this);
        createShortcut.setText("创建快捷方式");
        linearLayout.addView(createShortcut);

        TextView checkShortcut = new TextView(mContext);

        checkShortcut.setPadding(padding,padding,padding,0);
        checkShortcut.setOnClickListener(this);
        checkShortcut.setId(checkShortcutID);
        checkShortcut.setText("检测快捷方式");
        linearLayout.addView(checkShortcut);

        TextView removeShortcut = new TextView(mContext);

        removeShortcut.setPadding(padding,padding,padding,0);
        removeShortcut.setOnClickListener(this);
        removeShortcut.setId(removeShortcutID);
        removeShortcut.setText("删除快捷方式");
        linearLayout.addView(removeShortcut);

        return linearLayout;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case checkShortcutID:
                Toast.makeText(mContext,
                        mShortTitle + " shortcut " + ShortCutHelper.isShortCutExist(mContext,
                                mShortTitle,
                                new String[]{"com.UCMobile.intent.action.INVOKE"})
                        , Toast.LENGTH_SHORT).show();
                break;

            case createShortcutID:
                createShortcut(mShortTitle, R.drawable.hotnews_shortcut, generateUCHotNews());

                break;

            case removeShortcutID:
                removeShortcut(mShortTitle,generateUCHotNews());

                break;
        }
    }

    private void removeShortcut(String title,Intent intent){
        Intent shortcut = new Intent("com.android.launcher.action.UNINSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        try {
            mContext.sendBroadcast(shortcut);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    private void createShortcut(String title,int iconResId,Intent intent){
        Intent shortcut = new Intent("com.android.launcher.action.INSTALL_SHORTCUT");
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_NAME, title);
        shortcut.putExtra("duplicate", false); //不允许重复创建
        shortcut.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent);

        shortcut.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(mContext, iconResId));

        try {
            mContext.sendBroadcast(shortcut);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private Intent generateUCHotNews(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setComponent(new ComponentName("com.UCMobile", "com.UCMobile.main.UCMobile"));
        String data = "ext:info_flow_open_channel:ch_id=100&from=9";
        intent.putExtra("openurl",data);
        intent.putExtra("uc_partner","chedifier");

        return intent;
    }
}
