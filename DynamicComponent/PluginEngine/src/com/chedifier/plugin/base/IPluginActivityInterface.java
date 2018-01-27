package com.chedifier.plugin.base;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;

import com.chedifier.plugin.AbstractProxyActivty;

public interface IPluginActivityInterface{
	
	public void attachProxyAcvity(AbstractProxyActivty proxyActivity);
	
	public void _onCreated(Bundle savedInstanceState);

	public void _onStarted();

	public void _onResumed();

	public void _onPaused();

	public void _onStop();

	public void _onSaveInstanceState(Bundle outState);

	public void _onRestoreInstanceState(Bundle savedInstanceState);

	public void _onDestroyed();

	public void _onNewIntent(Intent intent);

	public boolean _onKeyDown(int keyCode, KeyEvent event);

	public void _onBackPressed();

	public void _onAttachedToWindow();

	public void _onConfigurationChanged(Configuration newConfig);
}
