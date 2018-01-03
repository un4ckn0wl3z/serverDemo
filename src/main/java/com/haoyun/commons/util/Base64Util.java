package com.haoyun.commons.util;

import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;

public class Base64Util {

	// 编码
	public static String encode(String code){
		return Base64.encodeBase64String(code.getBytes());
	}
	
	// 解码
	public static String decode(String code){
		try {
			return new String(Base64.decodeBase64(code),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "";
		}
	}
	
}
