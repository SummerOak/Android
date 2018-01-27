package com.chedifier.plugin;

import com.chedifier.baselibrary.utils.DebugLog;

import android.app.Application;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.LayoutInflater;

public class PluginContextWrapper extends ContextWrapper{
	
	private static final String TAG = "PluginContextWrapper";

	private Application mPluginApplication = null;

	/**
	 * @param pluginApp
	 * @param hostContext
	 */
	public PluginContextWrapper(Application pluginApp, Context hostContext) {
		super(hostContext);
		this.mPluginApplication = pluginApp;
	}

	@Override
	public Context getApplicationContext() {
		DebugLog.d(TAG,"getApplicationContext");
		
		return this;
	}

	@Override
	public Context getBaseContext() {
		DebugLog.d(TAG,"getBaseContext");
		return this;
	}

	@Override
	public String getPackageName() {
		DebugLog.d(TAG,"getPackageName");

		return mPluginApplication.getPackageName();
	}

	@Override
	public Resources getResources() {
		DebugLog.d(TAG,"getResources");
		
		return mPluginApplication.getResources();
	}

	@Override
	public AssetManager getAssets() {
		DebugLog.d(TAG,"getAssets");

		return mPluginApplication.getAssets();
	}

	@Override
	public Theme getTheme() {
		DebugLog.d(TAG,"getTheme");

		return mPluginApplication.getTheme();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		DebugLog.d(TAG,"getApplicationInfo");
		
		return mPluginApplication.getApplicationInfo();
	}
	
	@Override
    public Object getSystemService(String name) {
		DebugLog.d(TAG,"getSystemService name = " + name);
		
		DebugLog.d(TAG,"this = " + this);
		if(LAYOUT_INFLATER_SERVICE.equals(name)){
			LayoutInflater inflater = LayoutInflater.from(super.getBaseContext()).cloneInContext(this);
			
			DebugLog.d(TAG,"inflater.getContext() = " + inflater.getContext());
			return inflater;
		}
		
        return super.getSystemService(name);
    }
}
