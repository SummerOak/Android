package com.chedifier.plugin;

import android.app.Activity;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.base.IPluginActivityInterface;

public abstract class AbstractProxyActivty extends Activity{
	private static final String TAG = "ProxyActivty";
	
	protected IPluginActivityInterface mPluginActivity;
	
	public void bindRemoteActivity(IPluginActivityInterface remoteActivity){
		
		DebugLog.d(TAG,"bindRemoteActivity remoteActivity = " + remoteActivity);
		
		mPluginActivity = remoteActivity;
	}
}
