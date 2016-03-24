package com.chedifier.plugin;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.Message;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.baselibrary.utils.StringUtils;
import com.chedifier.plugin.ClassLoaderInjectHelper.InjectResult;
import com.chedifier.plugin.base.IPluginDescriptor;

import dalvik.system.DexClassLoader;


public class ApkLoader {
	
	private static final String TAG = "ApkLoader";
	
	private Context mHostContext;
	private String mApkPath;
	private String mOutDexPath;
	private ILoadListener mListener;

	private PackageInfo mPackageInfo;
	private ApplicationInfo mApplicationInfo;
	private String mPackageName;
	private String mDescriptorClassPath;
	private IPluginDescriptor mPluginDescriptor;
	private Plugin mPlugin;
	
	private final int LOAD_SUCC = 1;
	private final int LOAD_FAIL = 2;
	
	
	private Handler mH = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case LOAD_SUCC:
				if(mListener != null){
					mListener.onSucc(mPlugin);
				}
				
				break;
				
				
			case LOAD_FAIL:
				if(mListener != null){
					mListener.onFail(msg.arg1);
				}
				break;

			default:
				
				break;
			}
			
			
	    }
		
	};
	
	public ApkLoader(Context hostContext,String apkPath,String outDexPath,ILoadListener l){
		mHostContext = hostContext;
		mApkPath = apkPath;
		mOutDexPath = outDexPath;
		mListener = l;
	}
	

	public void load(){
		
		new Thread(new WorkerThread()).run();
		
	}
	
	public void loadAsync(){
		new Thread(new WorkerThread()).start();
	}
	
	private boolean preparePluginInfo(){
		DebugLog.d(TAG,"preparePluginInfo");
		
		PackageManager pm = mHostContext.getPackageManager();
		mPackageInfo = pm.getPackageArchiveInfo(mApkPath,
                PackageManager.GET_META_DATA|
                PackageManager.GET_UNINSTALLED_PACKAGES|
                PackageManager.GET_ACTIVITIES|
                PackageManager.GET_SERVICES);
		
		if(mPackageInfo != null){
			mApplicationInfo = mPackageInfo.applicationInfo;
			if(mApplicationInfo != null){
				
				mPackageName = mApplicationInfo.packageName;
				if(mApplicationInfo.metaData != null){
					mDescriptorClassPath = mApplicationInfo.metaData.getString(PluginConfig.META_DATA_DESCRIPTOR_CLASS_PATH);
				}
			}
		}
		
		if(StringUtils.isEmpty(mPackageName)
				|| StringUtils.isEmpty(mDescriptorClassPath)
				|| mPackageInfo == null
				|| mApplicationInfo == null){
			return false;
		}
		
		return true;
	}
	
	private void createPlugin(){
		DebugLog.d(TAG,"getPlugin");
		
		try {
			mPluginDescriptor = (IPluginDescriptor)Class.forName(mDescriptorClassPath).newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(mPluginDescriptor != null){
			
			PluginApplication pluginApplication = new PluginApplication(mHostContext, mApkPath,mPackageInfo);
			mPluginDescriptor.init(mHostContext.getApplicationContext(), pluginApplication.getApplicationContext());
			
			mPlugin = new Plugin();
			mPlugin.entry = mPluginDescriptor;
			mPlugin.application = pluginApplication;
		}
	}
	
	private class WorkerThread implements Runnable{
		
		@Override
		public void run() {
				
			boolean isOk = loadApk();
			
			if(isOk){
				createPlugin();
			}
			
			DebugLog.i(TAG, "init  isOk = " + isOk);
			
			mH.obtainMessage(mPlugin!=null?LOAD_SUCC:LOAD_FAIL).sendToTarget();
		}
		
	}
	
	private boolean loadApk(){
    	DebugLog.d(TAG, "doLoadPlayerPluginApk mApkPath= " + mApkPath + " mOutDexPath = " + mOutDexPath);
    	
    	if(StringUtils.isEmpty(mApkPath) || StringUtils.isEmpty(mOutDexPath)){
    		return false;
    	}
    	
    	if(!preparePluginInfo()){
    		return false;
    	}
    	
    	long time = System.currentTimeMillis();
    	
    	InjectResult result = ClassLoaderInjectHelper.inject(
    			this.getClass().getClassLoader(), 
				new DexClassLoader(mApkPath,
						mOutDexPath, 
						null, 
						this.getClass().getClassLoader()),
						mPackageName + ".R");
		
    	DebugLog.d(TAG, "result = " +result + " cost = " + (System.currentTimeMillis() - time));
    	
		return result.mIsSuccessful;
    }
	
	public static interface ILoadListener{
		public abstract void onSucc(Plugin plugin);
		public abstract void onFail(int errorCode);
	}
}
