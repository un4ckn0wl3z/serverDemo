package com.haoyun.commons.util;

public class IDUtil {

	public static String gen(String type) {
		return type + UUIDGenerator.getUUID();
	}
}
