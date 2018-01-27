package com.chedifier.plugin;

import java.io.File;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.KeyEvent;

public class ProxyActivity extends AbstractProxyActivty {
	
	private ProxyActivityImpl mImpl = new ProxyActivityImpl(this);
	
	@Override
	public void onAttachedToWindow() {
		super.onAttachedToWindow();
		
		
		if (null != mPluginActivity) {
			mPluginActivity._onAttachedToWindow();
		}
    }
	
	@Override
	protected void onCreate(Bundle arg0) {

		mImpl.launchPluginActivity(getIntent());
		
		super.onCreate(arg0);

		if (null != mPluginActivity) {
			mPluginActivity._onCreated(arg0);
		}
		
	}

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);

		if (null != mPluginActivity) {
			mPluginActivity._onNewIntent(intent);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (null != mPluginActivity) {
			mPluginActivity._onPaused();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (null != mPluginActivity) {
			mPluginActivity._onResumed();
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		if (null != mPluginActivity) {
			mPluginActivity._onSaveInstanceState(outState);
		}

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onStart() {
		super.onStart();

		if (null != mPluginActivity) {
			mPluginActivity._onStarted();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (null != mPluginActivity) {
			mPluginActivity._onStop();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();

		if (null != mPluginActivity) {
			mPluginActivity._onDestroyed();
		}

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		if (null != mPluginActivity) {
			mPluginActivity._onRestoreInstanceState(savedInstanceState);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if (null != mPluginActivity) {
			mPluginActivity._onConfigurationChanged(newConfig);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (null != mPluginActivity) {
			return mPluginActivity._onKeyDown(keyCode, event);
		}

		return super.onKeyDown(keyCode, event);
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (null != mPluginActivity) {
			mPluginActivity._onBackPressed();
		}
	}
	
	/****************************************/

	@Override
    public AssetManager getAssets() {
        return mImpl.getAssets() == null ? super.getAssets() : mImpl.getAssets();
    }

    @Override
    public Resources getResources() {
        return mImpl.getResources() == null ? super.getResources() : mImpl.getResources();
    }

    @Override
    public Theme getTheme() {
        return mImpl.getTheme() == null ? super.getTheme() : mImpl.getTheme();
    }

    @Override
    public ClassLoader getClassLoader() {
        return mImpl.getClassLoader() == null ?super.getClassLoader():mImpl.getClassLoader();
    }

	@Override
	public String getPackageName() {
		return mImpl.getPackageName()== null?super.getPackageName():mImpl.getPackageName();
	}

	@Override
	public Context getBaseContext() {
		return mImpl.getBaseContext() == null?super.getBaseContext():mImpl.getBaseContext();
	}

	@Override
	public Context getApplicationContext() {
		return mImpl.getApplicationContext() == null?super.getApplicationContext():mImpl.getApplicationContext();
	}
	
	@Override
    public ApplicationInfo getApplicationInfo() {
        return mImpl.getApplicationInfo() == null?super.getApplicationInfo():mImpl.getApplicationInfo();
    }

	@Override
	public File getFilesDir() {
		return mImpl.getFilesDir()==null?super.getFilesDir():mImpl.getFilesDir();
	}
	
}
