package com.simplememo;

import android.util.Log;

/**
 * Created by USER on 2016-11-30.
 */
public class Debug {
	public static void printMethodName(Object obj) {
//		int len = Thread.currentThread().getStackTrace().length;
//
//		for (int i = 0; i < ; i++)
//			Log.d(c.getClass().getSimpleName(), i + " : " + Thread.currentThread().getStackTrace()[i].getMethodName());


//		StackTraceElement[] steList = new Exception().getStackTrace();
//		for (int i = 0; i < steList.length; i++) {
//			Log.d(c.getClass().getSimpleName(), i + " : " + steList[i].getMethodName());
//		}
		Log.d(obj.getClass().getSimpleName(), new Exception().getStackTrace()[2].getMethodName());

	}

	public static void d() {
		d("mOrder");
	}

	public static void d(String log) {
		StackTraceElement[] steList = new Exception().getStackTrace();
		for (int i = 1; i < 2; i++) {
			String pkgs[] = steList[i].getClassName().split("\\.");
			String simpleClassName = pkgs[pkgs.length - 1];
			Log.d(simpleClassName + "." + steList[i].getMethodName(), log);
		}
	}

}
