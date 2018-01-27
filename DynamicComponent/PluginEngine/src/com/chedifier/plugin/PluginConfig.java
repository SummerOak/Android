package com.chedifier.plugin;

import java.io.File;

import android.content.Context;

import com.chedifier.baselibrary.utils.FileUtils;

public class PluginConfig {
	
	public static final String META_DATA_DESCRIPTOR_CLASS_PATH = "META_DATA_DESCRIPTOR_CLASS_PATH";
	
	public static String s_default_plugin_path = "";
	
	public static void init(Context ctx){
		if(ctx != null){
			s_default_plugin_path = ctx.getFilesDir().getAbsolutePath() + File.separator + "plugins";
			FileUtils.makeDirs(s_default_plugin_path);
		}
	}
}
