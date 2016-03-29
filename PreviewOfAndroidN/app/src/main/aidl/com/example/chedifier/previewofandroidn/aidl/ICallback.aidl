// IMyAidlInterface.aidl
package com.example.chedifier.previewofandroidn.aidl;

import com.example.chedifier.previewofandroidn.aidl.IPCBean;
// Declare any non-default types here with import statements

interface ICallback {
    void onEvent(int code,in IPCBean data);
}
