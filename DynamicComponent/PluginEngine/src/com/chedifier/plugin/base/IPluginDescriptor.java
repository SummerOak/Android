package com.chedifier.plugin.base;

import java.io.File;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.view.View;

public interface IPluginDescriptor {
	
	public abstract boolean init(Context hostContext,Context pluginContext);
	
	public abstract void setHostExchanger(IHostExchanger iExchanger);
	
	public abstract View getPluginListItemView(Context ctx);
	
	public abstract boolean start(Context ctx);
	
	public abstract ClassLoader getClassLoader();

    public abstract AssetManager getAssets();

    public abstract Resources getResources();

    public abstract Theme getTheme();
    
    public abstract Context getBaseContext();

	public abstract Context getApplicationContext();
	
	public abstract ApplicationInfo getApplicationInfo();

	public File getFilesDir();
}
