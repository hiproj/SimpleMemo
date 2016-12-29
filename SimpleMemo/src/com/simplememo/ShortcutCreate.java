package com.simplememo;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by USER on 2016-11-28.
 */
public class ShortcutCreate {
	public static void createShortcut(Activity act, String runAct, String makeShortcutName, int icon) {
		Intent runIntent = new Intent(Intent.ACTION_VIEW);
		runIntent.addCategory(Intent.CATEGORY_DEFAULT);
		String pkg = runAct.substring(0, runAct.lastIndexOf("."));
		Log.d("d", "pkg : " + pkg);
		runIntent.setComponent(new ComponentName(pkg, runAct));
		//
		Intent shortcutIntent = new Intent();
		shortcutIntent.putExtra("android.intent.extra.shortcut.NAME", makeShortcutName);
		shortcutIntent.putExtra("android.intent.extra.shortcut.ICON_RESOURCE", Intent.ShortcutIconResource.fromContext(act, icon));
		//
		shortcutIntent.putExtra("android.intent.extra.shortcut.INTENT", runIntent);
		act.setResult(Activity.RESULT_OK, shortcutIntent);
		// 모든 런처에 바로가기 생성(권한 필요)
		// <uses-permission android:name="com.android.launcher.permission.INSTALL_SHORTCUT" />
		// <uses-permission android:name="com.android.launcher.permission.UNINSTALL_SHORTCUT" />
		// create all launcher - need permission
		// shortcutIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		// sendBroadcast(shortcutIntent);
	}
}
