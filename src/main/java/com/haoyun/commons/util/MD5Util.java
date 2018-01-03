package com.haoyun.commons.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	public static void main(String[] args) {
//		String s = "appid=wx66907630bdf30cca&body=好运棋牌-金币充值&mch_id=1432556102&nonce_str=25d1605bc3db444c9ef5b20a05b55276&notify_url=http://daili.3399hy.com/shop/paySuccess&out_trade_no=25d1605bc3db444c9ef5b20a05b55276&spbill_create_ip=49.65.120.251&total_fee=1000&trade_type=JSAPI&key=epu31on6kzfltur4taog7crbmxxe05cl";
		String s = "appId=wx66907630bdf30cca&nonceStr=ee0c9509a5224a3288aadecfa15e0e64&package=wx20170405165544431ec336900966833689&signType=MD5&timeStamp=1491382559&key=epu31on6kzfltur4taog7crbmxxe05cl";
		System.out.println(md5(s));
	}

	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String md5(String str) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes("UTF-8"));
			byte[] byteDigest = md.digest();
			int i;
			StringBuffer buf = new StringBuffer("");
			for (byte element : byteDigest) {
				i = element;
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			// 32位加密
			return buf.toString();
			// 16位的加密
			// return buf.toString().substring(8, 24);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

}
