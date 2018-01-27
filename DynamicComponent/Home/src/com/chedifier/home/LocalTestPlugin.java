package com.chedifier.home;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.View;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.base.IHostExchanger;
import com.chedifier.plugin.base.IPluginDescriptor;

public class LocalTestPlugin implements IPluginDescriptor{
	
	private static String TAG = "LocalTestPlugin";

	@Override
	public boolean init(Context hostContext, Context pluginContext) {
		DebugLog.d(TAG, "init");
		
		return true;
	}

	@Override
	public void setHostExchanger(IHostExchanger iExchanger) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View getPluginListItemView(Context ctx) {
		DebugLog.d(TAG, "getPluginListItemView");
		
		return View.inflate(ctx, R.layout.home_item_show, null);
	}

	@Override
	public boolean start(Context ctx) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ClassLoader getClassLoader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AssetManager getAssets() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Resources getResources() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Theme getTheme() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getBaseContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Context getApplicationContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getFilesDir() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		// TODO Auto-generated method stub
		return null;
	}

	

}
