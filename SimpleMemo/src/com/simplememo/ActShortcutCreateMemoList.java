package com.simplememo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by USER on 2016-11-28.
 */
public class ActShortcutCreateMemoList extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShortcutCreate.createShortcut(this, "com.simplememo.ActMemoList", getResources().getString(R.string.memo_list), R.drawable.list_icon);
		finish();
	}
}
