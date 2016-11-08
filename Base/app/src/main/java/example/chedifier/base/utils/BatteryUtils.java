package example.chedifier.base.utils;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : BatteryUtils
 * <p/>
 * Creation    : 2016/6/28
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/6/28, chengqianxing, Create the file
 * ****************************************************************************
 */
public class BatteryUtils {

    public enum CHARGE_STATE{
        UNKNOWN,
        NONE,
        AC,
        USB,
    }

    public static CHARGE_STATE getCurrentChargeState(Context context){
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, ifilter);

        // Are we charging / charged?
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING ||
                status == BatteryManager.BATTERY_STATUS_FULL;

        if(!isCharging){
            return CHARGE_STATE.NONE;
        }

        // How are we charging?
        int chargePlug = batteryStatus.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
        switch (chargePlug){
            case BatteryManager.BATTERY_PLUGGED_USB:
                return CHARGE_STATE.USB;

            case BatteryManager.BATTERY_PLUGGED_AC:
                return CHARGE_STATE.AC;
        } ;

        return CHARGE_STATE.UNKNOWN;
    }


}
