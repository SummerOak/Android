package example.chedifier.chedifier.common;

import android.content.res.Configuration;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : SysConfigurationChangedHandler
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class SysConfigurationChangedHandler {

    private final String TAG = "SysConfigurationChangedHandler";

    private Configuration mLastConfiguration = new Configuration();

    private int HWTHEME_CONFIG;

    private SysConfigurationChangedHandler(){

        try {
            Class c = Class.forName("android.content.pm.ActivityInfoEx");

            Field field = c.getDeclaredField("CONFIG_HWTHEME");
            HWTHEME_CONFIG = field.getInt(null);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        Log.i(TAG,"HWTHEME_CONFIG " + HWTHEME_CONFIG);
    }

    private static class InstanceHolder{
        public static SysConfigurationChangedHandler sInstance = new SysConfigurationChangedHandler();
    }

    public static SysConfigurationChangedHandler getInstance(){
        return InstanceHolder.sInstance;
    }

    public boolean handleConfigurationChange(Configuration newCfg){
        if(newCfg != null){
            int update = mLastConfiguration.updateFrom(newCfg);
            Log.i(TAG,"HWTHEME_CONFIG " + HWTHEME_CONFIG);
            if((update&HWTHEME_CONFIG) != 0){
                Log.i(TAG, "hw theme changed.");
            }
        }

        return false;
    }



}
