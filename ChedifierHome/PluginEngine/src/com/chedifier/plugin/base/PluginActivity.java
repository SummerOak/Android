package com.chedifier.plugin.base;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.AbstractProxyActivty;

public class PluginActivity extends Activity implements IPluginActivityInterface{
	
	private static final String TAG = "PluginActivity";

	private Activity mProxyActivity;

	@Override
	public void _onCreated(Bundle savedInstanceState) {
		DebugLog.d(TAG,"_onCreated");
		
		this.onCreate(savedInstanceState);
	}

	@Override
	public void _onStarted() {
		DebugLog.d(TAG,"_onStarted");
		
		this.onStart();
	}

	@Override
	public void _onResumed() {
		DebugLog.d(TAG,"_onResumed");
		
		this.onResume();
	}

	@Override
	public void _onPaused() {
		DebugLog.d(TAG,"_onPaused");
		
		this.onPause();
	}

	@Override
	public void _onStop() {
		DebugLog.d(TAG,"_onStop");
		
		this.onStop();
	}

	@Override
	public void _onSaveInstanceState(Bundle outState) {
		DebugLog.d(TAG,"_onSaveInstanceState");
		
		this.onSaveInstanceState(outState);
	}

	@Override
	public void _onRestoreInstanceState(Bundle savedInstanceState) {
		DebugLog.d(TAG,"_onRestoreInstanceState");
		
		this.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	public void _onDestroyed() {
		DebugLog.d(TAG,"_onDestroyed");
		
		this.onDestroy();
	}

	@Override
	public void _onNewIntent(Intent intent) {
		DebugLog.d(TAG,"_onNewIntent");
		
		this.onNewIntent(intent);
	}

	@Override
	public boolean _onKeyDown(int keyCode, KeyEvent event) {
		DebugLog.d(TAG,"_onKeyDown");
		
		return this.onKeyDown(keyCode, event);
	}

	@Override
	public void _onBackPressed() {
		DebugLog.d(TAG,"_onBackPressed");
		
		this.onBackPressed();
	}

	@Override
	public void _onAttachedToWindow() {
		DebugLog.d(TAG,"_onAttachedToWindow");
		
		this.onAttachedToWindow();
	}

	@Override
	public void _onConfigurationChanged(Configuration newConfig) {
		DebugLog.d(TAG,"_onConfigurationChanged");
		
		this.onConfigurationChanged(newConfig);
	}
	
	@Override
	public void setContentView(int layoutId) {
		DebugLog.d(TAG,"setContentView");
		
		if(null != mProxyActivity){
			mProxyActivity.setContentView(layoutId);
		} else{
			super.setContentView(layoutId);
		}
	}

	@Override
	public void setTheme(int resid) {
		DebugLog.d(TAG,"setTheme");
		
		if(null != mProxyActivity){
			mProxyActivity.setTheme(resid);
		} else{
			super.setTheme(resid);
		}
	}

	@Override
	public void setContentView(View view) {
		DebugLog.d(TAG,"setContentView");
		
		if(null != mProxyActivity){
			mProxyActivity.setContentView(view);
		} else{
			super.setContentView(view);
		}
	}

	@Override
	public View findViewById(int id) {
		DebugLog.d(TAG,"findViewById");
		
		if(null != mProxyActivity){
			return mProxyActivity.findViewById(id);
		} else{
			return super.findViewById(id);
		}
	}

	@Override
	public void setIntent(Intent newIntent) {
		DebugLog.d(TAG,"setIntent");
		
		if(null != mProxyActivity){
			mProxyActivity.setIntent(newIntent);
		} else{
			super.setIntent(newIntent);
		}
	}

	@Override
	public Window getWindow() {
		DebugLog.d(TAG,"getWindow");
		
		if(null != mProxyActivity){
			return mProxyActivity.getWindow();
		} else{
			return super.getWindow();
		}
	}

	@Override
	public void setRequestedOrientation(int requestedOrientation) {
		DebugLog.d(TAG,"setRequestedOrientation");
		
		if(null != mProxyActivity){
			mProxyActivity.setRequestedOrientation(requestedOrientation);
		} else{
			super.setRequestedOrientation(requestedOrientation);
		}
	}

	@Override
	public void sendBroadcast(Intent intent) {
		DebugLog.d(TAG,"sendBroadcast");
		
		if(null != mProxyActivity){
			mProxyActivity.sendBroadcast(intent);
		} else{
			super.sendBroadcast(intent);
		}
	}

	@Override
	public Object getSystemService(String name) {
		DebugLog.d(TAG,"getSystemService");
		
		if(null != mProxyActivity){
			return mProxyActivity.getSystemService(name);
		} else{
			return super.getSystemService(name);
		}
	}

	@Override
	public Resources getResources() {
		DebugLog.d(TAG,"getResources");
		
		if(null != mProxyActivity){
			return mProxyActivity.getResources();
		} else{
			return super.getResources();
		}
	}

	@Override
	public Context getApplicationContext() {
		DebugLog.d(TAG,"getApplicationContext");
		
		if(null != mProxyActivity){
			return mProxyActivity.getApplicationContext();
		} else {
			return super.getApplicationContext();
		}
	}
	
	@Override
	public ApplicationInfo getApplicationInfo() {
		DebugLog.d(TAG,"getApplicationContext");
		
		if(null != mProxyActivity){
			return mProxyActivity.getApplicationInfo();
		} else {
			return super.getApplicationInfo();
		}
	}

	@Override
	public PackageManager getPackageManager() {
		DebugLog.d(TAG,"getPackageManager");
		
		if(null != mProxyActivity){
			return mProxyActivity.getPackageManager();
		} else {
			return super.getPackageManager();
		}
	}
	
	
	/**
	 * 
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		DebugLog.d(TAG,"onCreate");
		
		if(null == mProxyActivity){
			super.onCreate(savedInstanceState);
		}
	}

	@Override
	protected void onStart() {
		DebugLog.d(TAG,"onStart");
		
		if(null == mProxyActivity){
			super.onStart();
		}
			
	}

	@Override
	protected void onResume() {
		DebugLog.d(TAG,"onResume");
		
		if(null == mProxyActivity){
			super.onResume();
		}
	}

	@Override
	protected void onPause() {
		DebugLog.d(TAG,"onPause");
		
		if(null == mProxyActivity){
			super.onPause();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		DebugLog.d(TAG,"onSaveInstanceState");
		
		if(null == mProxyActivity){
			super.onSaveInstanceState(outState);
		}
	}

	@Override
	protected void onStop() {
		DebugLog.d(TAG,"onStop");
		
		if(null == mProxyActivity){
			super.onStop();
		}
	}

	@Override
	protected void onDestroy() {
		DebugLog.d(TAG,"onDestroy");
		
		if(null == mProxyActivity){
			super.onDestroy();
		}
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		DebugLog.d(TAG,"onRestoreInstanceState");
		
		if(null == mProxyActivity){
			super.onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		DebugLog.d(TAG,"onNewIntent");
		
		if(null == mProxyActivity){
			super.onNewIntent(intent);
		}
	}
	
	@Override
	public AssetManager getAssets() {
		DebugLog.d(TAG,"getAssets");
		
		if(null != mProxyActivity){
			return mProxyActivity.getAssets();
		}
		
		return super.getAssets();
	}

	@Override
	public String getPackageName() {
		DebugLog.d(TAG,"getPackageName");
		
		if(null != mProxyActivity){
			mProxyActivity.getPackageName();
		}
		
		return super.getPackageName();
	}

	@Override
	public Context getBaseContext() {
		DebugLog.d(TAG,"getBaseContext");
		
		if(null != mProxyActivity){
			return mProxyActivity.getBaseContext();
		}
		
		return super.getBaseContext();
	}

	
	@Override
	public File getFilesDir() {
		DebugLog.d(TAG,"getFilesDir");
		
		if(null != mProxyActivity){
			mProxyActivity.getFilesDir();
		}
		
		return super.getFilesDir();
	}

	@Override
	public void attachProxyAcvity(AbstractProxyActivty proxyActivity) {
		DebugLog.d(TAG,"setProxyAcvity");
		
		mProxyActivity = proxyActivity;
	}

}
