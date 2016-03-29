package com.example.chedifier.previewofandroidn.directboot;

import android.Manifest;
import android.os.Bundle;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;
import com.example.chedifier.previewofandroidn.common.FileUtils;
import com.example.chedifier.previewofandroidn.common.MyPermissionManager;

/**
 * Created by chedifier on 2016/3/29.
 */
public class BootCompleteActivity extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.boot_complete_activity);

        if(MyPermissionManager.checkSdcardPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            FileUtils.createFile("/sdcard/reboot_test/"+ System.currentTimeMillis());
        }

    }
}
