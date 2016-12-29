package com.simplememo;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ActNowMemo extends Activity {
	private EditText editText1;
	private MemoData memoData = MemoData.getInstance();
	private TextView tvCreateDateTime, tvModifyDateTime;
	private FrameLayout flHistoryCount;
	private ImageView ivHistoryCount;
	private TextView tvHistoryCount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_memo);
		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setSingleLine(false);
		tvCreateDateTime = (TextView) findViewById(R.id.tvCreateDateTime);
		tvModifyDateTime = (TextView) findViewById(R.id.tvModifyDateTime);
		//
		flHistoryCount = (FrameLayout) findViewById(R.id.flHistoryCount);
		ivHistoryCount = (ImageView) findViewById(R.id.ivHistoryCount);
		tvHistoryCount = (TextView) findViewById(R.id.tvHistoryCount);
		// editText1.setSingleLine(false);
		if (memoData.getNowMode().equals(MemoData.MODE_EDIT)) {
			// editText1.setFocusable(true);
			tvCreateDateTime.setText("만든시간 : " + memoData.getNowSelectCreateDateTime());
			tvModifyDateTime.setText("수정시간 : " + memoData.getNowSelectModifyDateTime());
			if (memoData.getNowSelectHistoryCount() > 0) {
				flHistoryCount.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						memoData.setNowMode(MemoData.MODE_HISTORY_LIST);
						startActivity(new Intent(ActNowMemo.this, ActHistoryList.class));
					}
				});
			}
			final int getInptType = editText1.getInputType();
			editText1.setVisibility(View.INVISIBLE);
			editText1.setInputType(InputType.TYPE_NULL);
			editText1.post(new Runnable() {
				@Override
				public void run() {
					new Handler().postDelayed(new Runnable() {
						@Override
						public void run() {
							editText1.setInputType(getInptType);
							editText1.setVisibility(View.VISIBLE);
						}
					}, 150);
				}
			});
		}

		findViewById(R.id.llSave).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMemo();
			}
		});
		findViewById(R.id.llSaveAndFin).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				setMemo();
				finish();
			}
		});

		final LinearLayout llSendDB = (LinearLayout) findViewById(R.id.llSendDB);
		llSendDB.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				final String inputStr = editText1.getText().toString();
				new AsyncTask<Void, Void, Void>(){

					@Override
					protected void onPreExecute() {
						super.onPreExecute();
						llSendDB.setEnabled(false);
					}

					@Override
					protected Void doInBackground(Void... params) {
						memoData.dbInsertMemoData(inputStr);
						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {
						super.onPostExecute(aVoid);
						llSendDB.setEnabled(true);
						Toast.makeText(ActNowMemo.this, "전송 됐습니다.\n→" + inputStr + "←", Toast.LENGTH_SHORT).show();
					}
				}.execute();
			}
		});
	}

	@Override
	protected void onStart() {
		super.onStart();
		if (memoData.getNowMode().equals(MemoData.MODE_EDIT) || memoData.getNowMode().equals(MemoData.MODE_HISTORY_LIST)) {
			editText1.setText(memoData.getNowSelectMemo());
			tvHistoryCount.setText("내역 수 : " + memoData.getNowSelectHistoryCount());
			flHistoryCount.setVisibility(View.VISIBLE);
		}
	}

	private void keyBoardHide() {
		InputMethodManager immhide = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
		immhide.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	private void setMemo() {
		String mode = memoData.getNowMode(); // 현재 모드 가져오기

		// 메모 추가 모드
		if (mode.equals(MemoData.MODE_MEMO_ADD)) {
			memoData.setNowSelect(memoData.getSize()); // 셀렉트를 마지막 위치로
			memoData.addBlank(); // 빈값 추가(추가 준비)
			memoData.setNowMode(MemoData.MODE_MEMO_ADD_EDIT); // 메모 추가 편집 모드
		}

		String originalMemo = memoData.getNowSelectMemo();
		String inputMemo = editText1.getText().toString();
		String diffMemo = inputMemo.replace(originalMemo, "");

		memoData.modifyNowData(inputMemo);
		memoData.saveInFile();

		Toast.makeText(ActNowMemo.this, "저장 됐습니다.\n→" + diffMemo + "←", Toast.LENGTH_SHORT).show();
	}

	@Override
	protected void onPause() {
		super.onPause();

		final String inputStr = editText1.getText().toString();
		if (!inputStr.equals("")
				&& !inputStr.equals(memoData.getNowSelectMemo()))
			setMemo();
		// finish();
	}

	@Override
	public void finish() {

//		if (memoData.getNowMode().equals(MemoData.MODE_MEMO_ADD_EDIT)) {
//			Intent intent = new Intent(this, ActMemoList.class);
//			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//			startActivity(intent);
//		}
		super.finish();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
}
