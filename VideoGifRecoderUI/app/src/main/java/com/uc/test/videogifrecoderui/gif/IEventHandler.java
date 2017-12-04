package com.uc.test.videogifrecoderui.gif;

/**
 * Created by uc on 03/12/2017.
 */

interface IEventHandler {
    void onStopEvent(int recordTime);
    void onExitEvent();
    void onGifMenuClickEvent(ShareGifLayer.MenuItem item);
}
