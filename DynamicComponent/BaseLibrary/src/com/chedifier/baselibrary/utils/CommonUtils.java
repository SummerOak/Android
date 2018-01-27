package com.chedifier.baselibrary.utils;

import java.util.List;

public class CommonUtils {

	public static boolean isEmptyList(List<?> list, int len) {
		return null == list || list.size() < len;
	}
	
}
