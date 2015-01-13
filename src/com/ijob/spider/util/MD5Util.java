package com.ijob.spider.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;


public class MD5Util {

	
	public static byte[] genDigest(String content, String charset) {
		try {
			return genDigest(content.getBytes(charset));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static byte[] genDigest(byte[] content) {
		if (content != null) {
			try {
				MessageDigest md = MessageDigest.getInstance("MD5");
				md.update(content);
				return md.digest();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String digestInHex(String content, String charset) {
		return TextUtils.bytes2Hex(genDigest(content, charset));
	}
}
