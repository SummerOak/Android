package com.chedifier.home;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.chedifier.baselibrary.utils.CommonUtils;
import com.chedifier.plugin.base.IPluginDescriptor;

public class PluginListAdapter extends BaseAdapter{
	
	private Context mContext;
	
	public PluginListAdapter(Context ctx){
		mContext = ctx;
	}

	private List<IPluginDescriptor> mModuleList;
	
	public synchronized void setModules(List<IPluginDescriptor> val){
		mModuleList = val;
	}
	
	public synchronized void addPlugin(IPluginDescriptor desc){
		if(mModuleList == null){
			mModuleList = new ArrayList<IPluginDescriptor>();
		}
		
		mModuleList.add(desc);
	}
	
	@Override
	public int getCount() {
		return mModuleList==null?0:mModuleList.size();
	}

	@Override
	public IPluginDescriptor getItem(int arg0) {
		return CommonUtils.isEmptyList(mModuleList, arg0)?
				null:mModuleList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		return getItem(position).getPluginListItemView(mContext);
	}

}
