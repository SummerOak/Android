package com.chedifier.module1;

import android.content.Context;

import com.chedifier.plugin.base.IHostExchanger;

public class GloableConstants {

	private static IHostExchanger sHostExchanger;
	private static Context sHostContext;
	private static Context sPluginContext;
	
	public static void setHostExchanger(IHostExchanger val){
		sHostExchanger = val;
	}
	
	public static IHostExchanger getHostExchanger(){
		return sHostExchanger;
	}
	
	public static void setHostContext(Context val){
		sHostContext = val;
	}
	
	public static Context getHostContext(){
		return sHostContext;
	}
	
	public static void setPluginContext(Context val){
		sPluginContext = val;
	}
	
	public static Context getPluginContext(){
		return sPluginContext;
	}
	
	
	public static void release(){
		sHostExchanger = null;
		sHostContext = null;
		sPluginContext = null;
	}
}
