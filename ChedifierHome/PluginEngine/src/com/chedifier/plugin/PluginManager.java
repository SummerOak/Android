package com.chedifier.plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.ApkLoader.ILoadListener;
import com.chedifier.plugin.base.IPluginDescriptor;

public class PluginManager {
	
	private static final String TAG = "PluginManager";
	
	private Map<String,Plugin> mPlugins = new HashMap<String,Plugin>();
	
	private static PluginManager sInstance;
	
	private PluginManager(){
		;
	}
	
	public static synchronized PluginManager getInstance(){
		if(sInstance == null){
			sInstance = new PluginManager();
		}
		
		return sInstance;
	}
	
	public void loadPluginsInAsserts(final Context ctx,final PluginHandler h){
		
		try {
			
			
			String[] filePahts = ctx.getAssets().list("plugins");
			if(filePahts != null){
				for(int i=0;i<filePahts.length;i++){
					String apkPath = filePahts[i];
					copyAssertPluginToDir(ctx, apkPath, PluginConfig.s_default_plugin_path);
				}
			}
			
			File pluginDir = new File(PluginConfig.s_default_plugin_path);
			if(pluginDir.isDirectory()){
				
				File[] plugins = pluginDir.listFiles();
				if(plugins != null){
					for(int i=0;i<plugins.length;i++){
						
						final String apkPath = plugins[i].getAbsolutePath();
						final String outDex = plugins[i].getParent();
						
						new ApkLoader(ctx,apkPath, outDex, new ILoadListener() {
								
								@Override
								public void onSucc(Plugin plugin) {
									
									DebugLog.d(TAG, "onSucc " + plugin);
									
									if(plugin != null){
										
										addPlugin(plugin);
										
										if(h != null){
											h.obtainMessage(PluginHandler.PLUGIN_ADD, plugin.entry).sendToTarget();
										}
									}
								}
								
								@Override
								public void onFail(int errorCode) {
									
								}
							}).load();
						
					}
				}
				
			}
			
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean copyAssertPluginToDir(Context ctx,String assertPlugin,String aimDir){
    	DebugLog.d("apkPlayer", "copyAssertPluginToDir aimDir = " + aimDir);
    	
		boolean isOk 			= false;
		InputStream is 			= null;
		FileOutputStream fos 	= null;
		
		try {
			is = ctx.getAssets().open("plugins/" + assertPlugin);
			File file = new File(aimDir + File.separator + assertPlugin);
			
			if(file.exists()){
				file.delete();
			}
			
			fos = new FileOutputStream(file);
			
			byte[] temp = new byte[16 * 1024];
			int i = 0;
			while ((i = is.read(temp)) > 0){
				fos.write(temp, 0, i);
			}
			
			fos.flush();
			
			isOk = true;
		} catch (IOException e) {
			e.printStackTrace();
		} catch(Exception e){
			e.printStackTrace();
		} finally{
			if(null != is){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					
					isOk = false;
				}
			}
			
			if(null != fos){
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
					
					isOk = false;
				}
			}
		}
		
		return isOk;
	
    }
	
	public synchronized IPluginDescriptor getPluginEntry(String packageName){
		return mPlugins.get(packageName).entry;
	}
	
	public synchronized void addPlugin(Plugin plugin){
		String packageName = plugin.application.getPackageName();
		mPlugins.put(packageName, plugin);
	}
	
	public synchronized void clearPlugins(){
		mPlugins.clear();
	}
	
	public void release(){
		clearPlugins();
	}
	
	public static abstract class PluginHandler extends Handler{
		
		public static final int PLUGIN_ADD = 1;
		
		public abstract void onPluginLoaded(IPluginDescriptor desc);
		
		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case PLUGIN_ADD:
				if(msg.obj instanceof IPluginDescriptor){
					onPluginLoaded((IPluginDescriptor)msg.obj);
				}
				break;

			default:
				break;
			}
			
	    }
	    
	}
}
