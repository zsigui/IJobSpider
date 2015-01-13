package com.ijob.spider.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MatchUtil {
	public static String findFirst(String content, String regex, int getIndex) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(content);
		if (matcher.find()) {
			return matcher.group(getIndex);
		}
		return null;
	}
}
