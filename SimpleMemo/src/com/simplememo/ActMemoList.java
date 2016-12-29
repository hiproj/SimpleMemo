package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

public class ActMemoList extends Activity implements ListUpdate{

	private final String TAG_CLASS_NAME = ActMemoList.this.getClass().getSimpleName();

	MemoData memoData = MemoData.getInstance();
	ListView lvMemoList;
	ArrayAdapter arrayAdapter;
	// int nowSelect = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Debug.d("start");
		// memoData.setContext(this);
		setContentView(R.layout.memo_list);
		lvMemoList = (ListView) findViewById(R.id.listView1);
		lvMemoList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				memoData.setNowSelect(position);
				memoData.setNowMode(MemoData.MODE_EDIT);
				startActivityForResult(new Intent(ActMemoList.this, ActNowMemo.class), 0);
				// startActivity(new Intent(ActMemoList.this, ActMemoView.class));
			}
		});

		lvMemoList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int selPos = position;
				AlertDialog.Builder builder = new AlertDialog.Builder(ActMemoList.this);
				builder
						.setMessage("삭제 하시겠습니까?\n→ " + arrayAdapter.getItem(selPos) + " ←")
						.setCancelable(false)
						.setPositiveButton("삭제함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										memoData.removeMemo(selPos);
										reloadList();
									}
								})
						.setNegativeButton("안함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
				builder.setCancelable(true);
				builder.create().show();
				return false;
			}
		});

		findViewById(R.id.llAddMemo).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActAddNowMemoStart.class));
			}
		});

		final LinearLayout llServerSave = (LinearLayout) findViewById(R.id.llServerSave);
		llServerSave.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				llServerSave.setEnabled(false);

				String memoDataPack = memoData.getMemoDataPack();
				String historyDataPack = memoData.getHistoryDataPack();

				String dataPack = MemoData.TAG_MEMO_DATA + memoDataPack + MemoData.TAG_MEMO_DATA;
				dataPack += MemoData.TAG_HISTORY + historyDataPack + MemoData.TAG_HISTORY;

				memoData.dbInsertMemoData(dataPack);
				String titleList = arrayAdapter.getItem(0).toString();
				for (int i = 1; i < arrayAdapter.getCount(); i++)
					titleList += "\n" + arrayAdapter.getItem(i).toString();

				Toast.makeText(ActMemoList.this, "모두 전송 됐습니다.\n→" + titleList + "←", Toast.LENGTH_SHORT).show();

				llServerSave.setEnabled(true);
			}
		});

		findViewById(R.id.llServerLoad).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(ActMemoList.this, ActDBList.class));
			}
		});

		reloadList();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void reloadList() {
		Debug.d("reload");
		String[] memoTitles = memoData.getMemoTitles();
		if (memoTitles == null)
			memoTitles = new String[0];
		arrayAdapter = new ArrayAdapter(this, R.layout.list_element, memoTitles);
		lvMemoList.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
		Debug.d(
				"memoTitles.len : " + memoTitles.length + "\n"
				+ "lvMemoList.getCount() : " + lvMemoList.getCount()
		);

	}

	@Override
	protected void onResume() {
		Debug.d("resume");
		super.onResume();
		reloadList();
	}

	@Override
	protected void onPause() {
		Debug.d("pause");
		memoData.saveInFile();
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		Debug.d("destory");
		super.onDestroy();
		Log.d(TAG_CLASS_NAME, new Exception().getStackTrace()[0].getMethodName());
	}
}
