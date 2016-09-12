package example.chedifier.chedifier.utils;

import java.util.Locale;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : SystemUtils
 * <p/>
 * Creation    : 2016/9/7
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/9/7, chengqianxing, Create the file
 * ****************************************************************************
 */
public class SystemUtils {

    public static String getLanguage() {
        String tempString = null;
        Locale sysLangue = Locale.getDefault();

        /* 1、不区分台湾繁体香港繁体
         * 2、Locale 这个类里面没有zh-HK，暂时先用hardcode的方式
         */
        if((Locale.TAIWAN.getLanguage().equals(sysLangue.getLanguage())
                && Locale.TAIWAN.getCountry().equals(sysLangue.getCountry()))

                || sysLangue.toString().toLowerCase().equals("zh_hk")){
            tempString = "zh-tw";
        }else if(Locale.ENGLISH.equals(sysLangue) || Locale.UK.equals(sysLangue) || Locale.US.equals(sysLangue) ){
            tempString = "en-us";
        }else{
            tempString = "zh-cn";//default
        }
        return tempString;
    }

}
