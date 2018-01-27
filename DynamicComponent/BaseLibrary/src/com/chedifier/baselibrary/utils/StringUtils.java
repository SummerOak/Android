package com.chedifier.baselibrary.utils;

public class StringUtils {
	
	public static boolean isEmpty(String str) {
		if(null == str || "".equals(str) || str.equalsIgnoreCase("null")){
			return true;
		}

		return false;
	}
}
