package com.chedifier.plugin;

import java.io.File;
import java.lang.reflect.Constructor;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.base.IPluginActivityInterface;
import com.chedifier.plugin.base.IPluginDescriptor;

public class ProxyActivityImpl {
	
	private static final String TAG = "ProxyImpl";
	
	public static final String EXTRA_PACKAGE_NAME = "EXTRA_PACKAGE_NAME";
	public static final String EXTRA_ACTIVITY_NAME = "EXTRA_ACTIVITY_NAME";
	
	private AbstractProxyActivty mProxyActivity;
	private IPluginActivityInterface mIPluginActivity;
	private IPluginDescriptor mPluginEntry;
	
	public ProxyActivityImpl(AbstractProxyActivty activity) {
        mProxyActivity = activity;
    }

	public void launchPluginActivity(Intent it){
		if(it != null){
			
			String packageName = it.getStringExtra(EXTRA_PACKAGE_NAME);
			String activityName = it.getStringExtra(EXTRA_ACTIVITY_NAME);
			
			mPluginEntry = PluginManager.getInstance().getPluginEntry(packageName);
			try {
	            Class<?> localClass = ProxyActivityImpl.class.getClassLoader().loadClass(activityName);
	            Constructor<?> localConstructor = localClass.getConstructor(new Class[] {});
	            Object instance = localConstructor.newInstance(new Object[] {});
	            
	            mIPluginActivity = (IPluginActivityInterface) instance;
	            mProxyActivity.bindRemoteActivity(mIPluginActivity);
	            DebugLog.d(TAG, "instance = " + instance);
	            mIPluginActivity.attachProxyAcvity(mProxyActivity);
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
			
		}
		
	}
	
	public ClassLoader getClassLoader() {
        return mPluginEntry==null?null:mPluginEntry.getClassLoader();
    }

    public AssetManager getAssets() {
        return mPluginEntry==null?null:mPluginEntry.getAssets();
    }

    public Resources getResources() {
        return mPluginEntry==null?null:mPluginEntry.getResources();
    }

    public Theme getTheme() {
        return mPluginEntry==null?null:mPluginEntry.getTheme();
    }
    
	public String getPackageName() {
		return mPluginEntry==null?null:mPluginEntry.getApplicationContext().getPackageName();
	}

	public Context getBaseContext() {
		return mPluginEntry==null?null:mPluginEntry.getBaseContext();
	}

	public Context getApplicationContext() {
		return mPluginEntry==null?null:mPluginEntry.getApplicationContext();
	}
	
	public ApplicationInfo getApplicationInfo() {
		return mPluginEntry==null?null:mPluginEntry.getApplicationInfo();
	}

	public File getFilesDir() {
		return mPluginEntry==null?null:mPluginEntry.getFilesDir();
	}
}
