package com.example.chedifier.previewofandroidn.download;

/**
 * Created by chedifier on 2016/3/22.
 */
public interface IDownloadMgrListener {

    public abstract void onTaskAdd(DownloadTask task);
    public abstract void onTaskDelete(DownloadTask task);

}
