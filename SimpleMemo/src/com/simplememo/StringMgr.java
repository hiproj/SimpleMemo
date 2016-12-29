package com.simplememo;

/**
 * Created by USER on 2016-12-01.
 */
public class StringMgr {

	public static String extTagDataDefText(String str, String tag) {
		String ext = extTagData(str, tag);
		return (ext == null) ? str : ext;
	}

	public static String extTagData(String str, String tag) {
		if (str.contains(tag)) {
			int start = str.indexOf(tag) + tag.length();
			int end = str.indexOf(tag, start + 1);
			if (end == -1) return null;
			return str.substring(start, end);
		} else {
			return null;
		}
	}
}
