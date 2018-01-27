package com.chedifier.home;

import android.content.Context;

public class SystemValues {

	public static String s_base_dir = "";
	
	public static void init(Context ctx){
		if(ctx != null){
			s_base_dir = ctx.getFilesDir().getAbsolutePath();
		}
		
	}
}
