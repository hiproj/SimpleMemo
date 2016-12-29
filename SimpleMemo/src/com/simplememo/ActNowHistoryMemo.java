package com.simplememo;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by USER on 2016-12-06.
 */
public class ActNowHistoryMemo extends Activity{
	EditText editText1;
	MemoData memoData = MemoData.getInstance();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.now_history_memo);

		((TextView) findViewById(R.id.tvCreateDateTime)).setText("만든시간 : " + StringMgr.extTagData(memoData.getNowHistoryItem(), MemoData.TAG_CREATE_DATE_TIME));
		((TextView) findViewById(R.id.tvModifyDateTime)).setText("수정시간 : " + StringMgr.extTagData(memoData.getNowHistoryItem(), MemoData.TAG_MODIFY_DATE_TIME));

		editText1 = (EditText) findViewById(R.id.editText1);
		editText1.setText(memoData.getNowHistoryItem().split(MemoData.TAG_MEMO_END)[0]);
		LinearLayout llRestore = (LinearLayout) findViewById(R.id.llRestore);
		llRestore.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Debug.d("복원하기");
				memoData.restoreNowHistorySelect();
				finish();
				ActHistoryList.act.finish();

			}
		});

	}


}
