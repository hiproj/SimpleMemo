package com.simplememo;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by USER on 2016-11-28.
 */
public class ActShortcutCreateNowMemoStart extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ShortcutCreate.createShortcut(this, "com.simplememo.ActAddNowMemoStart", getResources().getString(R.string.now_memo), R.drawable.fast_memo_icon);
		finish();
	}
}
