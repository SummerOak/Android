package example.chedifier.chedifier.common;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * ****************************************************************************
 * Copyright (C) 2005-2012 UCWEB Corporation. All rights reserved
 * File        : 2012-8-10
 * <p/>
 * Description : HandlerThreadWrapper
 * <p/>
 * Creation    : 2016/7/6
 * Author      : chengqianxing
 * Mail        : qianxing.cqx@alibaba-inc.com
 * History     : Creation, 2016/7/6, chengqianxing, Create the file
 * ****************************************************************************
 */
public class HandlerThreadWrapper {
    private Handler handler = null;

    public HandlerThreadWrapper(String name) {
        HandlerThread handlerThread = new HandlerThread("chedifier_" + name);
        handlerThread.start();
        this.handler = new Handler(handlerThread.getLooper());
    }

    public Handler getHandler() {
        return this.handler;
    }
}
