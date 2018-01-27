package com.chedifier.home;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.chedifier.baselibrary.utils.DebugLog;
import com.chedifier.plugin.ProxyActivity;
import com.chedifier.plugin.ProxyActivityImpl;
import com.chedifier.plugin.base.IHostExchanger;
import com.chedifier.plugin.base.IPluginActivityInterface;

public class HostExchangerImpl implements IHostExchanger{
	private static final String TAG = "HostExchangerImpl";
	
	private static HostExchangerImpl sInstance;
	public synchronized static HostExchangerImpl getInstance(){
		if(sInstance == null){
			sInstance = new HostExchangerImpl();
		}
		
		return sInstance;
	}
	
	private HostExchangerImpl(){
		;
	}

	@Override
	public Object doExchange(Event eEvent, Object... params) {
		
		switch (eEvent) {
		case E_START_ACTIVITY:
			
			return startProxyActivity(params);
		

		default:
			break;
		}
		
		return null;
	}

	private boolean startProxyActivity(Object... params){
		Context ctx = params!=null&&params.length>0?(Context)params[0]:null;
		if(ctx == null){
			return false;
		}
		String packageName = params!=null&&params.length>1?
				(String)params[1]:null;
				
		String activityName = params!=null&&params.length>2?
				(String)params[2]:null;
				
		Intent it = new Intent(ctx, ProxyActivity.class);
		it.putExtra(ProxyActivityImpl.EXTRA_PACKAGE_NAME, packageName);
		it.putExtra(ProxyActivityImpl.EXTRA_ACTIVITY_NAME, activityName);
		DebugLog.d(TAG, "packageName = " + packageName + "activityName = " + activityName);
		ctx.startActivity(it);
		
		return true;
	}
}
