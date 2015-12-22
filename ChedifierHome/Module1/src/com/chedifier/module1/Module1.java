package com.chedifier.module1;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.View;
import android.widget.TextView;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.base.IHostExchanger;
import com.chedifier.plugin.base.IHostExchanger.Event;
import com.chedifier.plugin.base.IPluginDescriptor;

public class Module1 implements IPluginDescriptor{
	
	private static String TAG = "Module1";
	
	public Module1(){
	}

	@Override
	public View getPluginListItemView(Context ctx) {
		
		DebugLog.d(TAG, "getPluginListItemView");
		
		View v = View.inflate(GloableConstants.getPluginContext(), R.layout.module1_item_show, null);
		TextView info = (TextView)v.findViewById(R.id.info);
		
		ApplicationInfo appInfo = GloableConstants.getPluginContext().getApplicationInfo();
		String name = appInfo.name;
		String packageName = appInfo.packageName;
		info.setText(name + "|" + packageName + "|");
		
		return v;
	}

	@Override
	public boolean init(Context hostContext,Context pluginContext) {
		
		DebugLog.d(TAG, "init");
		
		GloableConstants.setHostContext(hostContext);
		GloableConstants.setPluginContext(pluginContext);
		
		return true;
	}

	@Override
	public boolean start(Context ctx) {
		DebugLog.d(TAG, "start");
		
		IHostExchanger hostExchanger = GloableConstants.getHostExchanger();
		if(hostExchanger != null){
			
			String packageName = GloableConstants.getPluginContext().getPackageName();
			String activityName = MainActivity.class.getName();
			DebugLog.d(TAG, "packageName = " + packageName + "activityName = " + activityName);
			hostExchanger.doExchange(Event.E_START_ACTIVITY, ctx,packageName,activityName);
		}
		
		return false;
	}

	@Override
	public void setHostExchanger(IHostExchanger iExchanger) {
		GloableConstants.setHostExchanger(iExchanger);
	}

	@Override
	public ClassLoader getClassLoader() {
		return Module1.class.getClassLoader();
	}

	@Override
	public AssetManager getAssets() {
		return GloableConstants.getPluginContext().getAssets();
	}

	@Override
	public Resources getResources() {
		return GloableConstants.getPluginContext().getResources();
	}

	@Override
	public Theme getTheme() {
		return GloableConstants.getPluginContext().getTheme();
	}

	@Override
	public Context getBaseContext() {
		return getApplicationContext();
	}

	@Override
	public Context getApplicationContext() {
		return GloableConstants.getPluginContext().getApplicationContext();
	}

	@Override
	public File getFilesDir() {
		return getApplicationContext().getFilesDir();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		return null;
	}

}
