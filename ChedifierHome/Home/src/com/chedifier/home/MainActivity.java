package com.chedifier.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.chedifier.plugin.PluginManager;
import com.chedifier.plugin.PluginManager.PluginHandler;
import com.chedifier.plugin.base.IPluginDescriptor;

public class MainActivity extends Activity {

	private Activity mActivity;
	private ListView mListView;
	private PluginListAdapter mAdapter;
	private PluginHandler mPluginHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mActivity = this;
		mListView = (ListView)findViewById(R.id.modulelist);
		mAdapter = new PluginListAdapter(mActivity);
		mAdapter.addPlugin(new LocalTestPlugin());
		
		mListView.setAdapter(mAdapter);
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				IPluginDescriptor plugin = mAdapter.getItem(position);
				if(plugin != null){
					plugin.start(mActivity);
				}
				
			}
		});
		
		mPluginHandler = new PluginHandler() {
			
			@Override
			public void onPluginLoaded(IPluginDescriptor desc) {
				if(mAdapter != null){
					desc.setHostExchanger(HostExchangerImpl.getInstance());
					mAdapter.addPlugin(desc);
					mAdapter.notifyDataSetChanged();
				}
			}
		};
		
		loadPlugins();
	}
	
	private void loadPlugins(){
		
		new Thread(new Runnable(){
			@Override
			public void run(){
				Looper.prepare();
				
				PluginManager.getInstance().loadPluginsInAsserts(mActivity, mPluginHandler);
				
				Looper.loop();
			}
		}).start();
	}


}
