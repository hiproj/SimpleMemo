package com.simplememo;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by USER on 2016-12-02.
 */
public class ActDBList extends Activity {

	ListView lvLoadList;
	ArrayList<String> mainArrayList;
	ArrayAdapter<String> arrayAdapter;



	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (!NetworkMgr.isNetWork(this)) {
			Toast.makeText(this, "인터넷 연결 상태를 확인하세요.", Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		setContentView(R.layout.db_list);
		lvLoadList = (ListView) findViewById(R.id.lvLoadList);
		reloadMainList();
		//
		lvLoadList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

				final int selPos = position;
				// final String dbSelData = memoDataList.get(selPos);
				// final String dbSelMemoData = dbSelData.split(" / ")[2];				// 
				// Log.d("d", dbSelData);
				
				final String dbSelData = mainArrayList.get(selPos).split(":", 2)[1];
				final String dbSelMemoData = StringMgr.extTagDataDefText(dbSelData, MemoData.TAG_MEMO_DATA);
				
				AlertDialog.Builder builder = new AlertDialog.Builder(ActDBList.this);
				builder
						.setMessage("어떻게 할까요?\n→ " + dbSelMemoData.split(MemoData.TAG_MEMO_END)[0].replaceAll(MemoData.TAG_MEMO_SPLITER, "\n") + " ←")
						.setCancelable(false)
						.setPositiveButton("추가하기",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// memo addMemoItem
										memoData.parseAddMemoDataPack(dbSelMemoData);
										//
										// history addMemoItem
										final String dbSelHistoryData = StringMgr.extTagDataDefText(dbSelData, MemoData.TAG_HISTORY);
										memoData.addHistoryData(dbSelHistoryData);
									}
								})
						.setNegativeButton("덮어쓰기",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// memo overwrite
										memoData.memoDataClear();
										memoData.parseAddMemoDataPack(dbSelMemoData);
										//
										// history overwrite
										memoData.historyClear();
										final String dbSelHistoryData = StringMgr.extTagDataDefText(dbSelData, MemoData.TAG_HISTORY);
										memoData.addHistoryData(dbSelHistoryData);
										finish();
										return;
									}
								})
						.setNeutralButton("취소",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										return;
									}
								});
						
				builder.setCancelable(true);
				builder.create().show();

			}
		});

		lvLoadList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				final int selPos = position;
				final String getFullData = mainArrayList.get(selPos);
				String[] dbIdAndMemo = getFullData.split(":", 2);
				final String getId = dbIdAndMemo[0];
				final String getMemoData = dbIdAndMemo[1];

				AlertDialog.Builder builder = new AlertDialog.Builder(ActDBList.this);
				builder
						.setMessage("삭제합니까?\n(삭제시 복구불가)→ " + arrayAdapter.getItem(selPos).toString() + " ←")
						.setCancelable(false)
						.setPositiveButton("삭제함",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog, int which) {
										memoData.dbDeleteMemoData(getId);
										reloadMainList();
									}
								})
						.setNegativeButton("취소",
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

	}

	MemoData memoData = MemoData.getInstance();

	private ArrayList<String> loadDbIdAndMemoDataList() {
		final ArrayList<String> dataList = new ArrayList<String>();
		String[][] dbAllDataList = memoData.dbSelectAllArrayCharToStr();
		for (int i = 0; i < dbAllDataList.length; i++) {
			dataList.add(dbAllDataList[i][0] + ":" + dbAllDataList[i][2]);
		}
		return dataList;
	}


	private void reloadMainList() {
		mainArrayList = loadDbIdAndMemoDataList();
		ArrayList<String> dbList = (ArrayList<String>) mainArrayList.clone();

		for (int i = 0; i < dbList.size(); i++) {
			String[] dbIdAndData = dbList.get(i).split(":", 2);
			String memo = dbIdAndData[0] + ":";
			String[] memoList = StringMgr.extTagDataDefText(dbIdAndData[1], MemoData.TAG_MEMO_DATA).split(MemoData.TAG_MEMO_SPLITER);
			for (int j = 0; j < memoList.length; j++) {
				memo += memoList[j].split(MemoData.TAG_MEMO_END, 2)[0] + "\n";
			}
			if (memo.length() > 0) memo = memo.substring(0, memo.length() - 1);
			// memo = memo.replaceAll(MemoData.TAG_MEMO_SPLITER, "");

			dbList.set(i, memo);
		}
		arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_element, dbList);
		lvLoadList.setAdapter(arrayAdapter);
		arrayAdapter.notifyDataSetChanged();
	}
}
