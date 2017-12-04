package com.uc.test.videogifrecoderui.gif;

import android.graphics.drawable.Drawable;

/**
 * Created by uc on 30/11/2017.
 */

public interface IVideoGifRecordImpl {
    Drawable getCurrentVideoSnapture();
    boolean startVideoRecode(String gifFilePath);
    boolean stopVideoRecord();
}
