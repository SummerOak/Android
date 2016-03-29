package com.example.chedifier.previewofandroidn;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.backgroupopt.ConnectivityChangeTest;
import com.example.chedifier.previewofandroidn.backgroupopt.DozeTestActivity;
import com.example.chedifier.previewofandroidn.backgroupopt.JobSchedulerTestActivity;
import com.example.chedifier.previewofandroidn.backgroupopt.NewPic$VideoActionTest;
import com.example.chedifier.previewofandroidn.common.FileUtils;
import com.example.chedifier.previewofandroidn.datasaver.DownloadTestActivity;
import com.example.chedifier.previewofandroidn.multiwindows.MultiWindowActivityMain;
import com.example.chedifier.previewofandroidn.notifications.NotificationTestActivity;

public class MainActivity extends BaseActivity implements View.OnClickListener{

    private static final int OPEN_DIRECTORY_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            Log.d(TAG,savedInstanceState.getString("cqx"));
        }

        setContentView(R.layout.activity_main);//

        ((TextView)findViewById(R.id.multi_window_test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.notification_test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.datasaver_test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.scoped_access)).setOnClickListener(this);
        ((TextView)findViewById(R.id.account_permission)).setOnClickListener(this);
        ((TextView)findViewById(R.id.register_connectivity_receiver)).setOnClickListener(this);
        ((TextView)findViewById(R.id.register_pic_receiver)).setOnClickListener(this);
        ((TextView)findViewById(R.id.job_scheduler_test)).setOnClickListener(this);
        ((TextView)findViewById(R.id.doze_mode_test)).setOnClickListener(this);


        doSomeBusiness();
    }

    private void doSomeBusiness(){
        FileUtils.saveDrawable2Internal(this);

        Intent it = new Intent("com.example.chedifier.previewofandroidn.remote");
        it.setPackage("com.example.chedifier.previewofandroidn");
        startService(it);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("cqx","123123");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        Log.d(TAG,savedInstanceState.getString("cqx"));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OPEN_DIRECTORY_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            getContentResolver().takePersistableUriPermission(data.getData(),
                    Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

    private void scopedAccess(){
        StorageManager storageManager = getSystemService(StorageManager.class);
        StorageVolume storageVolume = storageManager.getPrimaryVolume();
        Intent intent = storageVolume.createAccessIntent(Environment.DIRECTORY_MOVIES);
        startActivityForResult(intent, OPEN_DIRECTORY_REQUEST_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent it = new Intent("com.example.chedifier.previewofandroidn.remote");
        it.setPackage("com.example.chedifier.previewofandroidn");
        stopService(it);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.multi_window_test://测试多窗口特性

                startActivity(new Intent(MainActivity.this, MultiWindowActivityMain.class));

                break;

            case R.id.notification_test://测试新通知栏特性

                startActivity(new Intent(MainActivity.this, NotificationTestActivity.class));

                break;

            case R.id.datasaver_test://测试后台付费流量控制
                startActivity(new Intent(MainActivity.this, DownloadTestActivity.class));

                break;

            case R.id.scoped_access://测试外部存储目录访问权限

                scopedAccess();

                break;

            case R.id.account_permission://测试 权限 android.permission.GET_ACCOUNTS

                AccountTest.getAccount(this);

                break;

            case R.id.register_connectivity_receiver:

                if(ConnectivityChangeTest.sHasRegister){
                    ConnectivityChangeTest.unregisterReceiver(this);
                }else{
                    ConnectivityChangeTest.registerReceiver(this);
                }

                ((TextView)findViewById(R.id.register_connectivity_receiver)).setText(ConnectivityChangeTest.sHasRegister?
                "取消注册 监听网络变化":"注册监听 网络变化");

                ConnectivityChangeTest.listenConnectivity(this);

                break;

            case R.id.register_pic_receiver:

                if(NewPic$VideoActionTest.sHasRegister){
                    NewPic$VideoActionTest.unregisterReceiver(this);
                }else{
                    NewPic$VideoActionTest.registerReceiver(this);
                }

                ((TextView)findViewById(R.id.register_pic_receiver)).setText(NewPic$VideoActionTest.sHasRegister?
                        "取消注册监听 Camera 拍照摄像":"注册监听 Camera 拍照摄像");

                NewPic$VideoActionTest.listenNewPic$Video(this);

                break;

            case R.id.job_scheduler_test:

                startActivity(new Intent(this, JobSchedulerTestActivity.class));

                break;

            case R.id.doze_mode_test:
                startActivity(new Intent(this,DozeTestActivity.class));

                break;

        }
    }
}
