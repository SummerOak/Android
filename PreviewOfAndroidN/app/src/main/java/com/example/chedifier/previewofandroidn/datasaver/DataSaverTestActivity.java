package com.example.chedifier.previewofandroidn.datasaver;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.chedifier.previewofandroidn.BaseActivity;
import com.example.chedifier.previewofandroidn.R;
import com.example.chedifier.previewofandroidn.download.DownloadService;
import com.example.chedifier.previewofandroidn.download.DownloadTask;
import com.example.chedifier.previewofandroidn.download.DownloadTaskMgr;
import com.example.chedifier.previewofandroidn.download.IDownloadListener;
import com.example.chedifier.previewofandroidn.download.IDownloadMgrListener;
import com.example.chedifier.previewofandroidn.notifications.ServiceHelper;

import java.util.List;

/**
 * Created by chedifier on 2016/3/21.
 */
public class DataSaverTestActivity extends BaseActivity implements View.OnClickListener,IDownloadMgrListener{

    private boolean mNeedLaunchData = true;
    private MyBaseAdapter mAdapter;
    private ListView mListView;

    private Button mBtnStartDownloadService;

    private static final int OPEN_DIRECTORY_REQUEST_CODE = 1;

    private int mFileIndex = 1;
    private String mUrl = "http://p.gdown.baidu.com/560d7b246a5f123156ad9db1f4d2b7c55ea8d4def6a04452141cf0cf4368df619fd1de5078c52374d6b145d51f7fd1394fcb1568fb332118c0b23a6af7eb61ca05372b4a5ed2691527e292bf973bc32edc2699f2981cc41c6fcd56e25427af1562de7ad53afadf42";

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.download_layout);

        mListView = (ListView)findViewById(R.id.listview);
        mAdapter = new MyBaseAdapter();
        mListView.setAdapter(mAdapter);

        ((Button)findViewById(R.id.addtask)).setOnClickListener(this);
        mBtnStartDownloadService = ((Button)findViewById(R.id.start_download_service));
        mBtnStartDownloadService.setOnClickListener(this);

        DownloadTaskMgr.getInstance().addListener(this);

        checkNetUsageState();
        checkSdcardPermission();
    }

    private void checkNetUsageState(){
        ConnectivityManager manager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager.isActiveNetworkMetered()){
            switch (manager.getRestrictBackgroundStatus()){
                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_DISABLED:
                    // 用户没有对收费流量进行限制

                    break;

                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_WHITELISTED:
                    //用户对收费浏览进行了限制，但是我们在白名单里面，也就是可以后台访问网络

                    break;

                case ConnectivityManager.RESTRICT_BACKGROUND_STATUS_ENABLED:
                    //用户对收费浏览进行了限制

                    guide();

                    break;
            }
        }
    }

    private void guide(){
        Intent intent = new Intent(Settings.ACTION_IGNORE_BACKGROUND_DATA_RESTRICTIONS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));//为了能够打开界面后定位到我们的app
//        startActivity(intent);
    }

    private void checkSdcardPermission(){
        boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

        if(!hasPermission){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    OPEN_DIRECTORY_REQUEST_CODE);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        checkNetUsageState();
        checkSdcardPermission();

        DownloadTaskMgr.getInstance().init(this);

        if(mNeedLaunchData){
            mNeedLaunchData = false;
            mAdapter.setData(DownloadTaskMgr.getInstance().getDownloadTasks(),true);
        }

        if(ServiceHelper.isServiceRunning(this,DownloadService.class)){
            mBtnStartDownloadService.setText("停止后台下载");
        }else{
            mBtnStartDownloadService.setText("开始后台下载");
        }
    }

    @Override
    protected void onDestroy(){
        DownloadTaskMgr.getInstance().removeListener(this);

        super.onDestroy();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addtask:

                String dir = Environment.getExternalStorageDirectory().getPath();
                Log.d(TAG,"dir: " + dir);

                boolean hasPermission = (ContextCompat.checkSelfPermission(this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);

                if(!hasPermission){
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            OPEN_DIRECTORY_REQUEST_CODE);
                }else{
                    DownloadTask task = DownloadTaskMgr.getInstance().addDownloadTask("testfile" + (++mFileIndex),mUrl);

                    if(task != null){
                        task.start();
                        mAdapter.setData(DownloadTaskMgr.getInstance().getDownloadTasks(),true);
                    }
                }


                break;

            case R.id.start_download_service:

                if(ServiceHelper.isServiceRunning(this,DownloadService.class)){
                    stopService(new Intent(this, DownloadService.class));
                    mBtnStartDownloadService.setText("开始后台下载");
                }else{
                    startService(new Intent(this, DownloadService.class));
                    mBtnStartDownloadService.setText("停止后台下载");
                }

                break;
        }
    }

    @Override
    public void onTaskAdd(DownloadTask task) {
        mAdapter.setData(DownloadTaskMgr.getInstance().getDownloadTasks(),true);
    }

    @Override
    public void onTaskDelete(DownloadTask task) {
        mAdapter.setData(DownloadTaskMgr.getInstance().getDownloadTasks(),true);
    }

    private class MyBaseAdapter extends BaseAdapter{

        private List<DownloadTask> mTasks;


        public MyBaseAdapter(){
        }

        public void setData(List<DownloadTask> lst,boolean notify){
            mTasks = lst;

            if(notify){
                notifyDataSetChanged();
            }
        }

        @Override
        public int getCount() {
            return mTasks==null?0:mTasks.size();
        }

        @Override
        public DownloadTask getItem(int i) {
            return mTasks==null?null:(mTasks.size()>i?mTasks.get(i):null);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            if(view == null){
                view = View.inflate(DataSaverTestActivity.this,R.layout.download_listitem,null);

                new ViewHolder(view);
            }

            ((ViewHolder)view.getTag()).bindData(getItem(i));

            return view;
        }
    }

    private class ViewHolder implements IDownloadListener,View.OnClickListener,View.OnLongClickListener{

        public TextView info;
        public SeekBar seekBar;

        public DownloadTask task;

        public ViewHolder(View v){
            info = (TextView)v.findViewById(R.id.info);
            seekBar = (SeekBar)v.findViewById(R.id.progress);
            seekBar.setMax(100);
            seekBar.setEnabled(false);
            v.setTag(this);
        }

        public void bindData(DownloadTask task){
            this.task = task;

            info.setText(task.toString());
            seekBar.setProgress(task.getProgress());

            task.addListener(this);
            seekBar.setOnClickListener(this);
            info.setOnClickListener(this);
            info.setOnLongClickListener(this);
        }

        @Override
        public void onDownloadSucc(DownloadTask task) {
            if(this.task.equals(task)){
                seekBar.setBackgroundColor(0xff00ff00);
                seekBar.setProgress(100);
            }
        }

        @Override
        public void onDownloadIng(DownloadTask task, int progress) {
            if(this.task.equals(task)){
                Log.d(TAG,this + ":  onDownloading progress: " + progress);

                seekBar.setBackgroundColor(0xffffffff);
                seekBar.setProgress(task.getProgress());
            }

        }

        @Override
        public void onDownloadPause(DownloadTask task) {
            if(this.task.equals(task)){
                seekBar.setBackgroundColor(0xffffff00);
            }

        }

        @Override
        public void onDownloadError(DownloadTask task) {
            if(this.task.equals(task)){
                seekBar.setBackgroundColor(0xffff0000);
            }

        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.progress:
                case R.id.info:

                    if(task.getDownloadState() == DownloadTask.DONWNLOAD_STATE.DOWNLOADING){
                        task.stop();
                    }else{
                        task.start();
                    }

                    break;
            }
        }

        @Override
        public boolean onLongClick(View view) {
            DownloadTaskMgr.getInstance().deleteTask(task);
            mAdapter.setData(DownloadTaskMgr.getInstance().getDownloadTasks(),true);
            return true;
        }
    }
}
