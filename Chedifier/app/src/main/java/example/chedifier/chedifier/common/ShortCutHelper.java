package example.chedifier.chedifier.common;

import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import example.chedifier.base.utils.StringUtils;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : ShortCutHelper
 * <p/>
 * Creation    : 2016/9/5
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/5, chengqianxing, Create the file
 * ****************************************************************************
 */
public class ShortCutHelper {

    static final String LAUNCHER_FAVORITES_INTENT= "intent";

    private static String getRunningLaucher(Context ctx, List<String> launcheres){
        if(ctx == null || launcheres== null || launcheres.isEmpty())
            return null;

        String launcherPackageName = null;
        try{
            ActivityManager am = (ActivityManager)ctx.getSystemService(Context.ACTIVITY_SERVICE);

            // 100个 目前应该能够包含所有的应用
            List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);

            int minIndex = -1;
            for(String packageName : launcheres){

                int i = 0;
                for (ActivityManager.RunningTaskInfo info : list) {

                    if (info.topActivity.getPackageName().equals(packageName) && info.baseActivity.getPackageName().equals(packageName)) {
                        //return packageName;
                        if(minIndex == -1 || i < minIndex){
                            launcherPackageName = packageName;
                            minIndex = i;
                            break;
                        }
                    }
                    i++;
                }
            }
        }catch(Exception e){
        }

        return launcherPackageName;
    }

    public static List<String> getLaunchers(Context ctx){
        try{

            List<String> names = null;
            PackageManager pkgMgr = ctx.getPackageManager();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            List<ResolveInfo> ra = pkgMgr.queryIntentActivities(intent, 0);

            if(ra.size() != 0){
                names = new ArrayList<String>();
            }

            int size = ra.size();
            for(int i=0; i<size; i++){
                String packageName = ra.get(i).activityInfo.packageName;
                names.add(packageName);
            }

            return names;
        }catch(Exception e){
            return null;
        }
    }


    private static String getLauncherProviderAuthority(Context context,String packageName) {
        String authority = null;
        try {
            ProviderInfo info = null;
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, PackageManager.GET_PROVIDERS);
            if (packageInfo != null && packageInfo.providers != null && packageInfo.providers.length > 0) {
                ProviderInfo[] providerArray = packageInfo.providers;
                //兼容特殊Launcher
                info = getSpecialLauncherProviderInfo(packageName, providerArray);

                if (info == null) {
                    info = getNormalLauncherProviderInfo(packageName, providerArray);
                }
            }

            if (info != null) {
                authority = info.authority;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(authority != null && authority.contains("com.huawei.android.launcher.settings")){
            authority = "com.huawei.android.launcher.settings";
        }

        return authority;
    }

    /**
     * 获取特殊Launcher的provider authority，
     * 后面有特殊Laucnher，可以在这个函数中继续补充
     *
     * @param packageName
     * @param infoArray
     * @return
     */
    private static ProviderInfo getSpecialLauncherProviderInfo(String packageName, ProviderInfo[] infoArray) {
        ProviderInfo info = null;

        if (StringUtils.equals(packageName, "com.baidu.launcher")) {
            for (int i = 0; i < infoArray.length; i ++) {
                if (infoArray[i] != null && StringUtils.equals("com.baidu.launcher", infoArray[i].authority)) {
                    info = infoArray[i];
                    break;
                }
            }
        } else if (StringUtils.equals(packageName, "com.baidu.home2")) {
            for (int i = 0; i < infoArray.length; i ++) {
                if (infoArray[i] != null && StringUtils.equals("com.baidu.home2", infoArray[i].authority)) {
                    info = infoArray[i];
                    break;
                }
            }
        }

        return info;
    }

    /**
     * 获取Launcher的provider authority。
     * 该函数对大部分Launcher试用，对于无法满足的Launcher，可以在{@link #getSpecialLauncherProviderInfo(String, ProviderInfo[])}}作一一兼容
     *
     * @param packageName
     * @param providerArray
     * @return
     */
    private static ProviderInfo getNormalLauncherProviderInfo(String packageName, ProviderInfo[] providerArray) {
        ProviderInfo info = null;
        ProviderInfo tempInfo = null;

        for (int i = 0; i < providerArray.length; i++) {
            tempInfo = providerArray[i];

            if (!StringUtils.isEmpty(tempInfo.readPermission)
                    && tempInfo.exported
                    && tempInfo.readPermission.contains("READ_SETTINGS")
                    && (StringUtils.isEmpty(tempInfo.writePermission) || tempInfo.writePermission.contains("WRITE_SETTINGS"))) {
                info = tempInfo;
                break;
            }
        }

        if (info == null) {
            info = providerArray[0];
        }

        return info;
    }

    /**
     * 兼容熊猫桌面
     * @param packageName
     * @param authority
     * @return
     */
    private static Uri getPandaLauncherProviderUri(String packageName, String authority) {
        Uri uri = null;
        if(!StringUtils.isEmpty(packageName) && (StringUtils.equals(packageName, "com.nd.android.pandahome2"
        )) || StringUtils.equals(packageName, "com.nd.android.smarthome")) {
            uri = Uri.parse("content://" + authority + "/favorites1/favorites/?notify=true");
        }

        return uri;
    }

    private static Uri getCurLauncherUri(Context context,final String packageName){
        if(packageName == null || packageName == "")
            return null;
        if(packageName.equals("com.android.launcher"))
            return Uri.parse("content://com.android.launcher2.settings/favorites?notify=true");

        if(packageName.equals("com.motorola.blur.home"))
            return Uri.parse("content://com.android.launcher.settings/favorites?notify=true");

        String authority = getLauncherProviderAuthority(context,packageName);
        Uri uri = null;

        if (StringUtils.isEmpty(authority)) {
            //如果authority计算不得，则用最初方案，猜测authority是packageName + ".settings"
            uri = Uri.parse("content://" + packageName + ".settings/favorites?notify=true");
        } else {
            //兼容熊猫桌面
            uri = getPandaLauncherProviderUri(packageName, authority);

            if (uri == null) {
                uri = Uri.parse("content://" + authority + "/favorites?notify=true");
            }
        }



        return uri;
    }

    /**
     * 检查快捷方式是否存在
     * <font color=red>注意：有些手机无法判断是否已经创建过快捷方式
     * 因此，在创建快捷方式时，请添加
     * shortcutIntent.putExtra("duplicate", false);// 不允许重复创建
     * 此处需要在AndroidManifest.xml中配置相关的桌面权限信息
     * 错误信息已捕获
     *
     * @param context
     * @return true 或者false
     */
    public static boolean isShortCutExist(Context context, String title, String[] baiduScAction) {
        boolean result = false;
        Cursor c = null;
        try {
            final ContentResolver cr = context.getContentResolver();
            String laucherPkg = getRunningLaucher(context, getLaunchers(context));
            if(laucherPkg == null)
                return false;

            Uri uri = getCurLauncherUri(context,laucherPkg);
//            context.grantUriPermission(laucherPkg,uri,Intent.FLAG_GRANT_READ_URI_PERMISSION);
            c = cr.query(uri, null,
                    "title = ?",
                    new String[] { title }, null);
            if (c != null && c.moveToFirst()) {
                do{
                    int intentIndex = c.getColumnIndex(LAUNCHER_FAVORITES_INTENT);
                    String intentStr = c.getString(intentIndex);
                    // 包含packagename 或者 第三方调用的action(com.UCMobile.intent.action.INVOKE)
                    if (intentStr != null && baiduScAction != null) {
                        for (String actionname : baiduScAction) {
                            if (intentStr.contains(actionname)) {
                                result = true;
                                break;
                            }

                        }
                    }
                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        } finally {
            closeSafely(c);
        }
        return result;
    }

    private static void closeSafely(Cursor cursor) {
        if (cursor != null) {
            try {
                cursor.close();
            } catch (Exception e) {}
        }
    }

}
