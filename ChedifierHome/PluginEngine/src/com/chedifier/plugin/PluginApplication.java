package com.chedifier.plugin;

import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.LayoutInflater;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.baselibrary.utils.JavaCalls;

public class PluginApplication extends Application{
	private static final String TAG = "PluginApplication";
	
	private Context mHostContext;
	private Resources mResources = null;
	private AssetManager mAssetManager = null;
	private Theme mTheme = null;
	private ApplicationInfo mApplicationInfo = null;
	private PackageInfo mPackageInfo = null;
	
	public PluginApplication(Context hostContext,String apkPath,PackageInfo pi){

		mHostContext = hostContext;
		PluginContextWrapper baseContext = new PluginContextWrapper(this, hostContext);
		super.attachBaseContext(baseContext);
		
		mPackageInfo = pi;
		PackageManager pm = mHostContext.getPackageManager();
//		mApplicationInfo = new ApplicationInfo(mHostContext.getApplicationInfo());
//		mApplicationInfo.packageName = mPackageInfo.packageName;
//		mApplicationInfo.name = (String)mPackageInfo.applicationInfo.loadLabel(pm);
		
		mApplicationInfo = mPackageInfo.applicationInfo;
		
		initPluginResource(apkPath);
	}
	
	private void initPluginResource(String apkPath) {
	       
    	try {
			mAssetManager = (AssetManager) AssetManager.class.newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
        
    	JavaCalls.callMethod(mAssetManager, "addAssetPath", new Object[] { apkPath });
        
        Resources hostRes = mHostContext.getResources();
        Configuration config = new Configuration();
        config.setTo(hostRes.getConfiguration());
        config.orientation = Configuration.ORIENTATION_UNDEFINED;
        
        mResources = new Resources(mAssetManager, hostRes.getDisplayMetrics(), config);
        mTheme = mResources.newTheme();
        mTheme.setTo(mHostContext.getTheme());
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
		
		return mApplicationInfo.packageName;
	}

	@Override
	public Resources getResources() {
		DebugLog.d(TAG,"getResources");

		return mResources;
	}

	@Override
	public AssetManager getAssets() {
		DebugLog.d(TAG,"getAssets");

		return mAssetManager;
	}

	@Override
	public Theme getTheme() {
		DebugLog.d(TAG,"getTheme");

		return mTheme;
	}

	@Override
	public ApplicationInfo getApplicationInfo() {
		DebugLog.d(TAG,"getApplicationInfo");
		
		return mApplicationInfo;
	}
	
	@Override
    public Object getSystemService(String name) {
		DebugLog.d(TAG,"getSystemService name = " + name);
		
		DebugLog.d(TAG,"this = " + this);
		if(LAYOUT_INFLATER_SERVICE.equals(name)){
			LayoutInflater inflater = LayoutInflater.from(mHostContext).cloneInContext(this);
			
			DebugLog.d(TAG,"inflater.getContext() = " + inflater.getContext());
			return inflater;
		}
		
        return super.getSystemService(name);
    }
}
