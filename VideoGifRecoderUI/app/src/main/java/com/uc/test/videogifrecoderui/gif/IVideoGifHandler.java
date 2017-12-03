package com.uc.test.videogifrecoderui.gif;

/**
 * Created by uc on 30/11/2017.
 */

public interface IVideoGifHandler {

    /**
     * gif recording action handler
     * @param action the action of {@link IVideoGifHandler#STOP_RECORD},{@link IVideoGifHandler#START_RECORD}
     */
    void handleGifAction(int action);

    byte START_RECORD = 0x01;
    byte STOP_RECORD = 0x02;
}
