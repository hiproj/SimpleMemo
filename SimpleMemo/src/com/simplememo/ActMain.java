package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;
import java.util.Arrays;

public class ActMain extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		startActivity(new Intent(this, ActMemoList.class));
		finish();
		// setContentView(R.layout.activity_act_main);
	}
}
