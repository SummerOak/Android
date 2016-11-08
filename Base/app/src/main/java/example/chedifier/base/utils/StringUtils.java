package example.chedifier.base.utils;

import java.util.Calendar;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : StringUtils
 * <p/>
 * Creation    : 2016/6/18
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/6/18, chengqianxing, Create the file
 * ****************************************************************************
 */
public class StringUtils {

    public static boolean isEmpty(StringBuffer aStringBuffer){
        return aStringBuffer == null || aStringBuffer.length() == 0;
    }

    public static boolean equals(String str1, String str2) {
        if (str1 == null) {
            return str2 == null;
        }

        return str1.equals(str2);
    }

    public static boolean isEmpty(String aText) {
//        return aText == null || aText.length() == 0;
        return aText == null || aText.trim().length() == 0;
    }


    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        String date =
                "" + calendar.get(Calendar.YEAR)
                        + "-" + (calendar.get(Calendar.MONTH)+1)
                        + "-" + calendar.get(Calendar.DAY_OF_MONTH)
                        + "-" + calendar.get(Calendar.HOUR_OF_DAY)
                        + "-" + calendar.get(Calendar.MINUTE)
                        + "-" + calendar.get(Calendar.SECOND);

        return date;
    }

}
