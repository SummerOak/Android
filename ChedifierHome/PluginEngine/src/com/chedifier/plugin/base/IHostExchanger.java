package com.chedifier.plugin.base;

public interface IHostExchanger {

	public abstract Object doExchange(Event eEvent,Object... params);
	
	
	public static enum Event{
		/**
		 * 
		 */
		E_START_ACTIVITY,
	}
}
