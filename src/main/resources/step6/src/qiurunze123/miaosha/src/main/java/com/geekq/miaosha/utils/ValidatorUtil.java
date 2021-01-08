package com.geekq.miaosha.utils; // (rank 616) copied from https://github.com/qiurunze123/miaosha/blob/59c6c0562d7095df52799e83764110951bb75a71/src/main/java/com/geekq/miaosha/utils/ValidatorUtil.java

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ValidatorUtil {
	
	private static final Pattern mobile_pattern = Pattern.compile("1\\d{10}");
	
	public static boolean isMobile(String src) {
		if(StringUtils.isEmpty(src)) {
			return false;
		}
		Matcher m = mobile_pattern.matcher(src);
		return m.matches();
	}
	

}
