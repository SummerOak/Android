package com.example.chedifier.previewofandroidn.download;

/**
 * Created by chedifier on 2016/3/21.
 */
public interface IDownloadListener {

    public abstract void onDownloadSucc(DownloadTask task);
    public abstract void onDownloadIng(DownloadTask task,int progress);
    public abstract void onDownloadPause(DownloadTask task);
    public abstract void onDownloadError(DownloadTask task);

}
