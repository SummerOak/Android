package com.chedifier.home;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.PluginConfig;

public class HomeApplication extends Application{
	
	private static final String TAG = "HomeApplication";
	
	public static Application sApplication;
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		sApplication = this;
		
		SystemValues.init(getBaseContext());
		PluginConfig.init(getBaseContext());
	}
	
	@Override
	public Context getApplicationContext() {
		DebugLog.d(TAG,"getApplicationContext");
		
		return this;
	}

	@Override
	public Context getBaseContext() {
		DebugLog.d(TAG,"getBaseContext");
		
		return super.getBaseContext();
	}

	@Override
	public String getPackageName() {
		DebugLog.d(TAG,"getPackageName");
		
		return super.getPackageName();
	}

	@Override
	public Resources getResources() {
		DebugLog.d(TAG,"getResources");

		return super.getResources();
	}

	@Override
	public AssetManager getAssets() {
		DebugLog.d(TAG,"getAssets");

		return super.getAssets();
	}

	@Override
	public Theme getTheme() {
		DebugLog.d(TAG,"getTheme");

		return super.getTheme();
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		DebugLog.d(TAG,"getApplicationInfo");
		
		return super.getApplicationInfo();
	}
	
	@Override
    public Object getSystemService(String name) {
		DebugLog.d(TAG,"getSystemService");
		
		DebugLog.d(TAG,"this = " + this);
		
        return super.getSystemService(name);
    }
}
