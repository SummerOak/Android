// IRemoteService.aidl
package com.example.chedifier.previewofandroidn.aidl;

import com.example.chedifier.previewofandroidn.aidl.ICallback;
import com.example.chedifier.previewofandroidn.aidl.IPCBean;
// Declare any non-default types here with import statements

interface IRemoteService {

    boolean addRemoteCallback(ICallback callback);

    void startWakeLockLoop(long t);

    void stopWakeLockLoop();

    boolean wakeLockLooping();

    void countingSheep();

    void stopCountSheep();

    boolean isCountingSheep();

    int getSheepCount();

}
