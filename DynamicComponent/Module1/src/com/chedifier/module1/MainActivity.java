package com.chedifier.module1;

import android.os.Bundle;

import com.chedifier.plugin.base.IPluginActivityInterface;
import com.chedifier.plugin.base.PluginActivity;

public class MainActivity extends PluginActivity implements IPluginActivityInterface{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}
}
