package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by USER on 2016-11-28.
 */
public class ActAddNowMemoStart extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		MemoData memoData = MemoData.getInstance();
		memoData.setNowMode(MemoData.MODE_MEMO_ADD);
		memoData.setNowSelect(memoData.getSize());
		startActivity(new Intent(this, ActNowMemo.class));
		finish();
	}
}